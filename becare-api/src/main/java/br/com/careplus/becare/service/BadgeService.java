package br.com.careplus.becare.service;

import br.com.careplus.becare.dto.response.BadgeResponse;
import br.com.careplus.becare.entity.Badge;
import br.com.careplus.becare.entity.User;
import br.com.careplus.becare.enums.BadgeType;
import br.com.careplus.becare.exception.BusinessException;
import br.com.careplus.becare.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço de concessão e consulta de insígnias do BeCare.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserService     userService;

    @Transactional
    public BadgeResponse award(Long userId, BadgeType type) {
        User user = userService.getOrThrow(userId);

        if (badgeRepository.existsByUserIdAndType(userId, type)) {
            throw new BusinessException("O usuário já possui a insígnia: " + type);
        }

        Badge badge = Badge.builder()
                .user(user)
                .type(type)
                .description(buildDescription(type))
                .build();

        log.info("Insígnia {} concedida ao usuário {}.", type, userId);
        return toResponse(badgeRepository.save(badge));
    }

    @Transactional(readOnly = true)
    public List<BadgeResponse> findByUser(Long userId) {
        userService.getOrThrow(userId);
        return badgeRepository.findByUserIdOrderByEarnedAtDesc(userId)
                .stream().map(this::toResponse).toList();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private String buildDescription(BadgeType type) {
        return switch (type) {
            case FIRST_POST         -> "Parabéns pelo seu primeiro post no BeCare!";
            case WEEK_STREAK_3      -> "3 semanas seguidas de publicações — você está em chama!";
            case WEEK_STREAK_8      -> "8 semanas de constância! Hábito formado.";
            case DAILY_CHAMPION     -> "Publicou todos os dias da semana. Incrível!";
            case HYDRATION_MASTER   -> "Mestre da hidratação: 7 posts de água em uma semana.";
            case SLEEP_GUARDIAN     -> "Guardião do sono: 5 registros de descanso saudável.";
            case SOCIAL_CONNECTOR   -> "Conector social: participou de 3 desafios colaborativos.";
            case PREVENTION_HERO    -> "Herói da prevenção: registrou um exame ou vacina.";
            case NUTRITION_STAR     -> "Estrela da nutrição: 10 posts de alimentação saudável.";
            case MENTAL_WELLNESS    -> "Mente sã: 5 registros de saúde mental e bem-estar.";
            case ACTIVE_MOVER       -> "Corpo em movimento: 15 posts de atividade física!";
            case COMMUNITY_BUILDER  -> "Construtor de comunidade: convidou 5 beneficiários.";
            case CHALLENGE_WINNER   -> "Campeão de desafio! Você venceu um desafio colaborativo.";
        };
    }

    private BadgeResponse toResponse(Badge b) {
        return BadgeResponse.builder()
                .id(b.getId())
                .userId(b.getUser().getId())
                .userName(b.getUser().getName())
                .type(b.getType())
                .description(b.getDescription())
                .earnedAt(b.getEarnedAt())
                .build();
    }
}
