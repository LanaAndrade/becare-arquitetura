package br.com.careplus.becare.vo;

import br.com.careplus.becare.enums.PillarType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Value Object que representa as estatísticas de um pilar para um beneficiário.
 *
 * <p>Objetos imutáveis utilizados nos relatórios semanais e análises de engajamento.
 * Por ser um VO, não possui identidade própria — a igualdade é determinada pelos valores.</p>
 */
@Getter
@AllArgsConstructor
@Builder
public final class PillarStats {

    private final PillarType pillarType;
    private final String pillarName;
    private final long postCount;
    private final double engagementPercent;

    /** Descrição formatada para exibição no relatório. */
    public String toSummary() {
        return String.format("%s: %d post(s) — %.1f%% do total semanal",
                pillarName, postCount, engagementPercent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PillarStats that)) return false;
        return pillarType == that.pillarType
                && postCount == that.postCount
                && Double.compare(engagementPercent, that.engagementPercent) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(pillarType, postCount, engagementPercent);
    }
}
