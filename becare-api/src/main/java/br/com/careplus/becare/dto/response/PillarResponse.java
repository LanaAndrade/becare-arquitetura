package br.com.careplus.becare.dto.response;

import br.com.careplus.becare.enums.PillarType;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class PillarResponse {
    private Long id;
    private String name;
    private PillarType type;
    private String description;
    private String iconUrl;
}
