package br.com.careplus.becare.dto.response;

import br.com.careplus.becare.enums.PostStatus;
import br.com.careplus.becare.enums.Visibility;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/** DTO de resposta com dados de um post. */
@Data
@Builder
public class PostResponse {
    private Long id;
    private Long userId;
    private String userName;
    private Long pillarId;
    private String pillarName;
    private String description;
    private String mediaUrl;
    private PostStatus status;
    private String moderationNote;
    private Visibility visibility;
    private LocalDateTime createdAt;
}
