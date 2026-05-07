package br.com.careplus.becare.dto.request;

import br.com.careplus.becare.enums.Visibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

/** DTO de criação de um post/ação saudável. */
@Data
public class PostRequest {

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Long userId;

    @NotNull(message = "O ID do pilar é obrigatório.")
    private Long pillarId;

    @NotBlank(message = "A descrição é obrigatória.")
    @Size(min = 5, max = 500, message = "A descrição deve ter entre 5 e 500 caracteres.")
    private String description;

    @URL(message = "A URL da mídia é inválida.")
    private String mediaUrl;

    private Visibility visibility = Visibility.PUBLIC;
}
