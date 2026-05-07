package br.com.careplus.becare.dto.response;

import br.com.careplus.becare.enums.BadgeType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data @Builder
public class BadgeResponse {
    private Long id;
    private Long userId;
    private String userName;
    private BadgeType type;
    private String description;
    private LocalDateTime earnedAt;
}
