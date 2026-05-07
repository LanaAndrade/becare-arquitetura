package br.com.careplus.becare.service;

import br.com.careplus.becare.dto.request.ModerationRequest;
import br.com.careplus.becare.dto.request.PostRequest;
import br.com.careplus.becare.dto.response.PostResponse;
import br.com.careplus.becare.entity.Pillar;
import br.com.careplus.becare.entity.Post;
import br.com.careplus.becare.entity.User;
import br.com.careplus.becare.enums.PostStatus;
import br.com.careplus.becare.exception.BusinessException;
import br.com.careplus.becare.exception.ResourceNotFoundException;
import br.com.careplus.becare.repository.PillarRepository;
import br.com.careplus.becare.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço responsável pela publicação e moderação de posts do BeCare.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository    postRepository;
    private final PillarRepository  pillarRepository;
    private final UserService       userService;

    @Transactional
    public PostResponse create(PostRequest request) {
        User   user   = userService.getOrThrow(request.getUserId());
        Pillar pillar = getPillarOrThrow(request.getPillarId());

        if (!user.getActive()) {
            throw new BusinessException("Usuário inativo não pode publicar posts.");
        }

        Post post = Post.builder()
                .user(user)
                .pillar(pillar)
                .description(request.getDescription())
                .mediaUrl(request.getMediaUrl())
                .visibility(request.getVisibility())
                .build();

        Post saved = postRepository.save(post);
        log.debug("Post {} criado pelo usuário {}.", saved.getId(), user.getId());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PostResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    /** Feed público: posts aprovados paginados. */
    @Transactional(readOnly = true)
    public Page<PostResponse> findFeed(Pageable pageable) {
        return postRepository.findByStatusOrderByCreatedAtDesc(PostStatus.APPROVED, pageable)
                .map(this::toResponse);
    }

    /** Posts de um usuário específico. */
    @Transactional(readOnly = true)
    public List<PostResponse> findByUser(Long userId) {
        userService.getOrThrow(userId); // valida existência
        return postRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponse).toList();
    }

    /** Moderação de post (aprovação ou rejeição). */
    @Transactional
    public PostResponse moderate(Long postId, ModerationRequest request) {
        Post post = getOrThrow(postId);

        if (post.getStatus() != PostStatus.PENDING) {
            throw new BusinessException("Apenas posts com status PENDING podem ser moderados.");
        }

        post.setStatus(request.getStatus());
        post.setModerationNote(request.getNote());

        log.info("Post {} moderado: {}", postId, request.getStatus());
        return toResponse(postRepository.save(post));
    }

    @Transactional
    public void delete(Long id) {
        Post post = getOrThrow(id);
        postRepository.delete(post);
        log.info("Post {} removido.", id);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    public Post getOrThrow(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", id));
    }

    private Pillar getPillarOrThrow(Long id) {
        return pillarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pilar", id));
    }

    PostResponse toResponse(Post p) {
        return PostResponse.builder()
                .id(p.getId())
                .userId(p.getUser().getId())
                .userName(p.getUser().getName())
                .pillarId(p.getPillar().getId())
                .pillarName(p.getPillar().getName())
                .description(p.getDescription())
                .mediaUrl(p.getMediaUrl())
                .status(p.getStatus())
                .moderationNote(p.getModerationNote())
                .visibility(p.getVisibility())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
