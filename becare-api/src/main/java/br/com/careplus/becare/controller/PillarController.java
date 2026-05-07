package br.com.careplus.becare.controller;

import br.com.careplus.becare.dto.response.PillarResponse;
import br.com.careplus.becare.entity.Pillar;
import br.com.careplus.becare.exception.ResourceNotFoundException;
import br.com.careplus.becare.repository.PillarRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints REST para os 7 Pilares de bem-estar do BeCare.
 */
@RestController
@RequestMapping("/api/v1/pillars")
@RequiredArgsConstructor
@Tag(name = "Pilares", description = "Os 7 Pilares de bem-estar do BeCare")
public class PillarController {

    private final PillarRepository pillarRepository;

    @GetMapping
    @Operation(summary = "Listar todos os pilares")
    public ResponseEntity<List<PillarResponse>> findAll() {
        return ResponseEntity.ok(pillarRepository.findAll().stream()
                .map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pilar por ID")
    public ResponseEntity<PillarResponse> findById(@PathVariable Long id) {
        Pillar pillar = pillarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pilar", id));
        return ResponseEntity.ok(toResponse(pillar));
    }

    private PillarResponse toResponse(Pillar p) {
        return PillarResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .type(p.getType())
                .description(p.getDescription())
                .iconUrl(p.getIconUrl())
                .build();
    }
}
