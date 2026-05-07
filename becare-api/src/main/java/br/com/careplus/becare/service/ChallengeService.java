package br.com.careplus.becare.service;

import br.com.careplus.becare.dto.request.ChallengeRequest;
import br.com.careplus.becare.dto.response.ChallengeResponse;
import br.com.careplus.becare.entity.Challenge;
import br.com.careplus.becare.entity.Pillar;
import br.com.careplus.becare.entity.User;
import br.com.careplus.becare.exception.BusinessException;
import br.com.careplus.becare.exception.ResourceNotFoundException;
import br.com.careplus.becare.repository.ChallengeRepository;
import br.com.careplus.becare.repository.PillarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço de desafios colaborativos do BeCare.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final PillarRepository    pillarRepository;
    private final UserService         userService;

    @Transactional
    public ChallengeResponse create(ChallengeRequest request) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BusinessException("A data de término deve ser posterior à data de início.");
        }

        Pillar pillar = pillarRepository.findById(request.getPillarId())
                .orElseThrow(() -> new ResourceNotFoundException("Pilar", request.getPillarId()));

        Challenge challenge = Challenge.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .pillar(pillar)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        return toResponse(challengeRepository.save(challenge));
    }

    @Transactional(readOnly = true)
    public List<ChallengeResponse> findActive() {
        return challengeRepository.findByActiveTrue()
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ChallengeResponse> findAll() {
        return challengeRepository.findAll()
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ChallengeResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public ChallengeResponse join(Long challengeId, Long userId) {
        Challenge challenge = getOrThrow(challengeId);
        User user = userService.getOrThrow(userId);

        if (!challenge.getActive()) {
            throw new BusinessException("Este desafio não está ativo.");
        }

        boolean alreadyJoined = challenge.getParticipants().stream()
                .anyMatch(u -> u.getId().equals(userId));
        if (alreadyJoined) {
            throw new BusinessException("O usuário já está participando deste desafio.");
        }

        challenge.getParticipants().add(user);
        log.info("Usuário {} entrou no desafio {}.", userId, challengeId);
        return toResponse(challengeRepository.save(challenge));
    }

    @Transactional
    public void deactivate(Long id) {
        Challenge challenge = getOrThrow(id);
        challenge.setActive(false);
        challengeRepository.save(challenge);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Challenge getOrThrow(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Desafio", id));
    }

    private ChallengeResponse toResponse(Challenge c) {
        return ChallengeResponse.builder()
                .id(c.getId())
                .title(c.getTitle())
                .description(c.getDescription())
                .pillarId(c.getPillar().getId())
                .pillarName(c.getPillar().getName())
                .startDate(c.getStartDate())
                .endDate(c.getEndDate())
                .active(c.getActive())
                .participantCount(c.getParticipants().size())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
