package br.com.careplus.becare.controller;

import br.com.careplus.becare.dto.response.BadgeResponse;
import br.com.careplus.becare.enums.BadgeType;
import br.com.careplus.becare.service.BadgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints REST para insígnias/badges do BeCare.
 */
@RestController
@RequestMapping("/api/v1/badges")
@RequiredArgsConstructor
@Tag(name = "Insígnias", description = "Conquistas e gamificação do BeCare")
public class BadgeController {

    private final BadgeService badgeService;

    @PostMapping("/award/{userId}/{type}")
    @Operation(summary = "Conceder insígnia a um beneficiário")
    public ResponseEntity<BadgeResponse> award(
            @PathVariable Long userId,
            @PathVariable BadgeType type) {
        return ResponseEntity.ok(badgeService.award(userId, type));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Listar insígnias de um beneficiário")
    public ResponseEntity<List<BadgeResponse>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(badgeService.findByUser(userId));
    }
}
