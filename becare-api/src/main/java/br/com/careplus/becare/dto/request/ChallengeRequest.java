package br.com.careplus.becare.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/** DTO de criação de desafio colaborativo. */
@Data
public class ChallengeRequest {

    @NotBlank(message = "O título é obrigatório.")
    @Size(min = 5, max = 200, message = "O título deve ter entre 5 e 200 caracteres.")
    private String title;

    @NotBlank(message = "A descrição é obrigatória.")
    @Size(min = 10, max = 1000, message = "A descrição deve ter entre 10 e 1000 caracteres.")
    private String description;

    @NotNull(message = "O ID do pilar é obrigatório.")
    private Long pillarId;

    @NotNull(message = "A data de início é obrigatória.")
    private LocalDate startDate;

    @NotNull(message = "A data de término é obrigatória.")
    @Future(message = "A data de término deve ser no futuro.")
    private LocalDate endDate;
}
