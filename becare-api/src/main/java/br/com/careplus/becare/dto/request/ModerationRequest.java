package br.com.careplus.becare.dto.request;

import br.com.careplus.becare.enums.PostStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** DTO para moderação manual de um post. */
@Data
public class ModerationRequest {

    @NotNull(message = "O status de moderação é obrigatório.")
    private PostStatus status;

    @Size(max = 300, message = "A nota de moderação pode ter no máximo 300 caracteres.")
    private String note;
}
