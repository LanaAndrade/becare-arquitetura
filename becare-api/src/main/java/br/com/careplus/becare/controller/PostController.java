package br.com.careplus.becare.controller;

import br.com.careplus.becare.dto.request.ModerationRequest;
import br.com.careplus.becare.dto.request.PostRequest;
import br.com.careplus.becare.dto.response.PostResponse;
import br.com.careplus.becare.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Endpoints REST para publicação e moderação de posts do BeCare.
 */
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Publicação e moderação de ações saudáveis")
public class PostController {

    private final PostService postService;

    @PostMapping
    @Operation(summary = "Publicar nova ação saudável")
    public ResponseEntity<PostResponse> create(@Valid @RequestBody PostRequest request) {
        PostResponse response = postService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar post por ID")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @GetMapping("/feed")
    @Operation(summary = "Feed público: posts aprovados (paginado)")
    public ResponseEntity<Page<PostResponse>> feed(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(postService.findFeed(pageable));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Posts de um beneficiário")
    public ResponseEntity<List<PostResponse>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.findByUser(userId));
    }

    @PatchMapping("/{id}/moderate")
    @Operation(summary = "Moderar post (aprovar ou rejeitar)")
    public ResponseEntity<PostResponse> moderate(
            @PathVariable Long id,
            @Valid @RequestBody ModerationRequest request) {
        return ResponseEntity.ok(postService.moderate(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover post")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
