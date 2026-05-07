package br.com.careplus.becare.entity;

import br.com.careplus.becare.enums.PillarType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Relatório semanal gerado para um beneficiário.
 *
 * <p>Consolida as microações da semana, destaca o pilar mais praticado,
 * gera uma mensagem motivacional e sugestão para a próxima semana.
 * Pode ser enviado via WhatsApp Business API.</p>
 */
@Entity
@Table(name = "weekly_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "week_start", nullable = false)
    private LocalDate weekStart;

    @Column(name = "week_end", nullable = false)
    private LocalDate weekEnd;

    @Column(name = "total_posts", nullable = false)
    @Builder.Default
    private Integer totalPosts = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "top_pillar", length = 50)
    private PillarType topPillar;

    @Column(name = "highlight_msg", length = 500)
    private String highlightMsg;

    @Column(name = "suggestion_msg", length = 500)
    private String suggestionMsg;

    @Column(name = "sent_whatsapp", nullable = false)
    @Builder.Default
    private Boolean sentWhatsapp = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
