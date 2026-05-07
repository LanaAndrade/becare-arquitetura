package br.com.careplus.becare.entity;

import br.com.careplus.becare.enums.PillarType;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade que representa um dos 7 Pilares de bem-estar do BeCare.
 * Os dados são populados pelo Flyway (V1__init.sql) e não sofrem CRUD pelo app.
 */
@Entity
@Table(name = "pillars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pillar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private PillarType type;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(name = "icon_url", length = 300)
    private String iconUrl;
}
