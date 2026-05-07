package br.com.careplus.becare.controller;

import br.com.careplus.becare.dto.response.WeeklyReportResponse;
import br.com.careplus.becare.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints REST para relatórios semanais do BeCare.
 */
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Relatórios", description = "Relatórios semanais de bem-estar do BeCare")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/generate/{userId}")
    @Operation(summary = "Gerar relatório semanal para o beneficiário (semana atual)")
    public ResponseEntity<WeeklyReportResponse> generate(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.generateCurrentWeek(userId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Histórico de relatórios de um beneficiário")
    public ResponseEntity<List<WeeklyReportResponse>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.findByUser(userId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar relatório por ID")
    public ResponseEntity<WeeklyReportResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.findById(id));
    }
}
