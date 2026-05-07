package br.com.careplus.becare.dto.response;

import br.com.careplus.becare.enums.PillarType;
import br.com.careplus.becare.vo.PillarStats;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
public class WeeklyReportResponse {
    private Long id;
    private Long userId;
    private String userName;
    private LocalDate weekStart;
    private LocalDate weekEnd;
    private int totalPosts;
    private PillarType topPillar;
    private String highlightMsg;
    private String suggestionMsg;
    private Boolean sentWhatsapp;
    private List<PillarStats> pillarBreakdown;
    private LocalDateTime createdAt;
}
