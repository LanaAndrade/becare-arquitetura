-- =============================================
--  BeCare – V1__init.sql
--  Criação do schema inicial do banco de dados
-- =============================================

-- Tabela de usuários/beneficiários
CREATE TABLE users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(150)        NOT NULL,
    email       VARCHAR(200)        NOT NULL UNIQUE,
    cpf         VARCHAR(14)         NOT NULL UNIQUE,
    birth_date  DATE                NOT NULL,
    active      BOOLEAN             NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de pilares de saúde (os 7 pilares do BeCare)
CREATE TABLE pillars (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)        NOT NULL UNIQUE,
    type        VARCHAR(50)         NOT NULL UNIQUE, -- enum PillarType
    description VARCHAR(500)        NOT NULL,
    icon_url    VARCHAR(300)
);

-- Tabela de posts (ações saudáveis publicadas)
CREATE TABLE posts (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT              NOT NULL,
    pillar_id       BIGINT              NOT NULL,
    description     VARCHAR(500)        NOT NULL,
    media_url       VARCHAR(500),
    status          VARCHAR(30)         NOT NULL DEFAULT 'PENDING', -- enum PostStatus
    moderation_note VARCHAR(300),
    visibility      VARCHAR(20)         NOT NULL DEFAULT 'PUBLIC',  -- enum Visibility
    created_at      TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_post_user   FOREIGN KEY (user_id)   REFERENCES users(id),
    CONSTRAINT fk_post_pillar FOREIGN KEY (pillar_id) REFERENCES pillars(id)
);

-- Tabela de insígnias/badges
CREATE TABLE badges (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT              NOT NULL,
    type        VARCHAR(80)         NOT NULL, -- enum BadgeType
    earned_at   TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(300),
    CONSTRAINT fk_badge_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Tabela de desafios colaborativos
CREATE TABLE challenges (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(200)        NOT NULL,
    description     VARCHAR(1000)       NOT NULL,
    pillar_id       BIGINT              NOT NULL,
    start_date      DATE                NOT NULL,
    end_date        DATE                NOT NULL,
    active          BOOLEAN             NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_challenge_pillar FOREIGN KEY (pillar_id) REFERENCES pillars(id)
);

-- Tabela de participantes nos desafios (N:N)
CREATE TABLE challenge_participants (
    challenge_id    BIGINT  NOT NULL,
    user_id         BIGINT  NOT NULL,
    joined_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (challenge_id, user_id),
    CONSTRAINT fk_cp_challenge FOREIGN KEY (challenge_id) REFERENCES challenges(id),
    CONSTRAINT fk_cp_user      FOREIGN KEY (user_id)      REFERENCES users(id)
);

-- Tabela de relatórios semanais
CREATE TABLE weekly_reports (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT          NOT NULL,
    week_start      DATE            NOT NULL,
    week_end        DATE            NOT NULL,
    total_posts     INT             NOT NULL DEFAULT 0,
    top_pillar      VARCHAR(50),    -- enum PillarType
    highlight_msg   VARCHAR(500),
    suggestion_msg  VARCHAR(500),
    sent_whatsapp   BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_report_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =============================================
--  Dados iniciais – os 7 Pilares do BeCare
-- =============================================
INSERT INTO pillars (name, type, description, icon_url) VALUES
('Prevenção',         'PREVENTION',       'Ações preventivas de saúde: check-ups, vacinas, exames.',                  'https://cdn.careplus.com.br/icons/prevention.svg'),
('Alimentação',       'NUTRITION',        'Hábitos alimentares saudáveis e equilíbrio nutricional.',                   'https://cdn.careplus.com.br/icons/nutrition.svg'),
('Hidratação',        'HYDRATION',        'Consumo adequado de água e líquidos ao longo do dia.',                      'https://cdn.careplus.com.br/icons/hydration.svg'),
('Atividade Física',  'PHYSICAL_ACTIVITY','Exercícios físicos, caminhadas, treinos e movimento.',                      'https://cdn.careplus.com.br/icons/activity.svg'),
('Saúde Mental',      'MENTAL_HEALTH',    'Mindfulness, meditação, equilíbrio emocional e bem-estar psicológico.',     'https://cdn.careplus.com.br/icons/mental.svg'),
('Engajamento Social','SOCIAL',           'Conexões sociais, atividades em grupo e comunidade.',                       'https://cdn.careplus.com.br/icons/social.svg'),
('Sono',              'SLEEP',            'Qualidade e quantidade adequada de descanso e sono reparador.',             'https://cdn.careplus.com.br/icons/sleep.svg');
