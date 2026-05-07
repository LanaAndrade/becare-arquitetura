package br.com.careplus.becare.service;

import br.com.careplus.becare.dto.response.WeeklyReportResponse;
import br.com.careplus.becare.entity.User;
import br.com.careplus.becare.entity.WeeklyReport;
import br.com.careplus.becare.enums.PillarType;
import br.com.careplus.becare.exception.BusinessException;
import br.com.careplus.becare.exception.ResourceNotFoundException;
import br.com.careplus.becare.repository.PostRepository;
import br.com.careplus.becare.repository.WeeklyReportRepository;
import br.com.careplus.becare.vo.PillarStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço de geração e consulta de relatórios semanais do BeCare.
 *
 * <p>O relatório consolida as microações aprovadas da semana,
 * identifica o pilar mais praticado e gera mensagens motivacionais
 * personalizadas para o beneficiário.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final WeeklyReportRepository reportRepository;
    private final PostRepository         postRepository;
    private final UserService            userService;

    /**
     * Gera (ou recupera) o relatório semanal do beneficiário referente à semana atual.
     */
    @Transactional
    public WeeklyReportResponse generateCurrentWeek(Long userId) {
        User user = userService.getOrThrow(userId);

        LocalDate weekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate weekEnd   = weekStart.plusDays(6);

        // Evita duplicata
        return reportRepository.findByUserIdAndWeekStart(userId, weekStart)
                .map(r -> toResponse(r, buildPillarStats(userId, weekStart, weekEnd)))
                .orElseGet(() -> {
                    WeeklyReport report = buildReport(user, weekStart, weekEnd);
                    return toResponse(reportRepository.save(report),
                            buildPillarStats(userId, weekStart, weekEnd));
                });
    }

    @Transactional(readOnly = true)
    public List<WeeklyReportResponse> findByUser(Long userId) {
        userService.getOrThrow(userId);
        return reportRepository.findByUserIdOrderByWeekStartDesc(userId)
                .stream()
                .map(r -> toResponse(r, buildPillarStats(
                        userId, r.getWeekStart(), r.getWeekEnd())))
                .toList();
    }

    @Transactional(readOnly = true)
    public WeeklyReportResponse findById(Long id) {
        WeeklyReport report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Relatório", id));
        return toResponse(report,
                buildPillarStats(report.getUser().getId(),
                        report.getWeekStart(), report.getWeekEnd()));
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private WeeklyReport buildReport(User user, LocalDate weekStart, LocalDate weekEnd) {
        Long userId = user.getId();
        LocalDateTime start = weekStart.atStartOfDay();
        LocalDateTime end   = weekEnd.atTime(LocalTime.MAX);

        List<Object[]> raw = postRepository.countByPillarForUserAndPeriod(userId, start, end);

        PillarType topPillar = raw.isEmpty() ? null : (PillarType) raw.get(0)[0];
        long totalPosts = raw.stream().mapToLong(r -> (Long) r[1]).sum();

        String highlight  = buildHighlight(user.getName(), topPillar, totalPosts);
        String suggestion = buildSuggestion(topPillar);

        return WeeklyReport.builder()
                .user(user)
                .weekStart(weekStart)
                .weekEnd(weekEnd)
                .totalPosts((int) totalPosts)
                .topPillar(topPillar)
                .highlightMsg(highlight)
                .suggestionMsg(suggestion)
                .build();
    }

    private List<PillarStats> buildPillarStats(Long userId, LocalDate weekStart, LocalDate weekEnd) {
        LocalDateTime start = weekStart.atStartOfDay();
        LocalDateTime end   = weekEnd.atTime(LocalTime.MAX);

        List<Object[]> raw = postRepository.countByPillarForUserAndPeriod(userId, start, end);
        long total = raw.stream().mapToLong(r -> (Long) r[1]).sum();
        if (total == 0) return new ArrayList<>();

        return raw.stream().map(r -> {
            PillarType type  = (PillarType) r[0];
            long count       = (Long) r[1];
            double pct       = (count * 100.0) / total;
            return PillarStats.builder()
                    .pillarType(type)
                    .pillarName(type.name())
                    .postCount(count)
                    .engagementPercent(pct)
                    .build();
        }).toList();
    }

    private String buildHighlight(String name, PillarType top, long total) {
        if (total == 0) {
            return String.format("Olá, %s! Que tal publicar sua primeira ação saudável esta semana?", name);
        }
        return String.format("Ótima semana, %s! Você registrou %d ação(ões) saudável(is) e seu destaque foi: %s.",
                name, total, top != null ? top.name() : "Geral");
    }

    private String buildSuggestion(PillarType top) {
        if (top == null) return "Comece sua jornada BeCare publicando qualquer ação saudável!";
        return switch (top) {
            case SLEEP            -> "Incrível foco no sono! Que tal explorar mais a Atividade Física na próxima semana?";
            case HYDRATION        -> "Hidratação em dia! Tente adicionar mais posts de Saúde Mental.";
            case NUTRITION        -> "Alimentação saudável em destaque! Considere registrar também momentos de Engajamento Social.";
            case PHYSICAL_ACTIVITY-> "Corpo ativo! Na próxima semana, foque também no Sono reparador.";
            case MENTAL_HEALTH    -> "Mente equilibrada! Que tal explorar mais a Alimentação?";
            case SOCIAL           -> "Conexões sociais em alta! Não esqueça de registrar sua hidratação diária.";
            case PREVENTION       -> "Prevenção em foco! Combine com posts de Atividade Física na próxima semana.";
        };
    }

    private WeeklyReportResponse toResponse(WeeklyReport r, List<PillarStats> stats) {
        return WeeklyReportResponse.builder()
                .id(r.getId())
                .userId(r.getUser().getId())
                .userName(r.getUser().getName())
                .weekStart(r.getWeekStart())
                .weekEnd(r.getWeekEnd())
                .totalPosts(r.getTotalPosts())
                .topPillar(r.getTopPillar())
                .highlightMsg(r.getHighlightMsg())
                .suggestionMsg(r.getSuggestionMsg())
                .sentWhatsapp(r.getSentWhatsapp())
                .pillarBreakdown(stats)
                .createdAt(r.getCreatedAt())
                .build();
    }
}
