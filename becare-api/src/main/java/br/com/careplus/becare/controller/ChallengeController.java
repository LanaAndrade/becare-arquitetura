package br.com.careplus.becare.controller;

import br.com.careplus.becare.dto.request.ChallengeRequest;
import br.com.careplus.becare.dto.response.ChallengeResponse;
import br.com.careplus.becare.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Endpoints REST para desafios colaborativos do BeCare.
 */
@RestController
@RequestMapping("/api/v1/challenges")
@RequiredArgsConstructor
@Tag(name = "Desafios", description = "Desafios colaborativos do BeCare")
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping
    @Operation(summary = "Criar novo desafio colaborativo")
    public ResponseEntity<ChallengeResponse> create(@Valid @RequestBody ChallengeRequest request) {
        ChallengeResponse response = challengeService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os desafios")
    public ResponseEntity<List<ChallengeResponse>> findAll() {
        return ResponseEntity.ok(challengeService.findAll());
    }

    @GetMapping("/active")
    @Operation(summary = "Listar desafios ativos")
    public ResponseEntity<List<ChallengeResponse>> findActive() {
        return ResponseEntity.ok(challengeService.findActive());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar desafio por ID")
    public ResponseEntity<ChallengeResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(challengeService.findById(id));
    }

    @PostMapping("/{id}/join/{userId}")
    @Operation(summary = "Beneficiário entra em um desafio")
    public ResponseEntity<ChallengeResponse> join(
            @PathVariable Long id,
            @PathVariable Long userId) {
        return ResponseEntity.ok(challengeService.join(id, userId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar desafio")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        challengeService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
