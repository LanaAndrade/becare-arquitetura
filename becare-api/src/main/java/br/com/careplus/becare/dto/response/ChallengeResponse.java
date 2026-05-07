package br.com.careplus.becare.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder
public class ChallengeResponse {
    private Long id;
    private String title;
    private String description;
    private Long pillarId;
    private String pillarName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
    private int participantCount;
    private LocalDateTime createdAt;
}
