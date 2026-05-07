package br.com.careplus.becare.enums;

/**
 * Tipos de insígnia que um beneficiário pode conquistar no BeCare.
 *
 * <p>Insígnias são concedidas automaticamente pelo sistema ao atingir
 * critérios de engajamento semanais ou marcos específicos.</p>
 */
public enum BadgeType {
    // Engajamento geral
    FIRST_POST,            // Publicou o primeiro post
    WEEK_STREAK_3,         // 3 semanas consecutivas publicando
    WEEK_STREAK_8,         // 8 semanas consecutivas publicando
    DAILY_CHAMPION,        // Publicou todos os dias da semana

    // Pilares específicos
    HYDRATION_MASTER,      // 7 posts de hidratação em uma semana
    SLEEP_GUARDIAN,        // 5 posts de sono saudável
    SOCIAL_CONNECTOR,      // Participou de 3 desafios colaborativos
    PREVENTION_HERO,       // Registrou ação preventiva (exame, vacina)
    NUTRITION_STAR,        // 10 posts de alimentação saudável
    MENTAL_WELLNESS,       // 5 posts de saúde mental
    ACTIVE_MOVER,          // 15 posts de atividade física

    // Marcos da comunidade
    COMMUNITY_BUILDER,     // Convidou 5 beneficiários para desafios
    CHALLENGE_WINNER       // Venceu um desafio colaborativo
}
