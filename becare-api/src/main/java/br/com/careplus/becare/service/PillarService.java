package br.com.careplus.becare.service;

import br.com.careplus.becare.dto.response.PillarResponse;
import br.com.careplus.becare.entity.Pillar;
import br.com.careplus.becare.exception.ResourceNotFoundException;
import br.com.careplus.becare.repository.PillarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço responsável pela consulta dos 7 Pilares de bem-estar do BeCare.
 *
 * <p>Os pilares são dados de referência imutáveis inseridos via Flyway.
 * Esta camada garante que o controller não acesse o repositório diretamente,
 * mantendo a separação de responsabilidades da arquitetura em camadas.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PillarService {

    private final PillarRepository pillarRepository;

    @Transactional(readOnly = true)
    public List<PillarResponse> findAll() {
        return pillarRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PillarResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    /** Retorna a entidade Pillar ou lança ResourceNotFoundException — usado por outros serviços. */
    public Pillar getOrThrow(Long id) {
        return pillarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pilar", id));
    }

    // ── Helper ───────────────────────────────────────────────────────────────

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
