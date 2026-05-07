package br.com.careplus.becare.controller;

import br.com.careplus.becare.dto.response.PillarResponse;
import br.com.careplus.becare.service.PillarService;
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

    private final PillarService pillarService;

    @GetMapping
    @Operation(summary = "Listar todos os pilares")
    public ResponseEntity<List<PillarResponse>> findAll() {
        return ResponseEntity.ok(pillarService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pilar por ID")
    public ResponseEntity<PillarResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pillarService.findById(id));
    }
}
