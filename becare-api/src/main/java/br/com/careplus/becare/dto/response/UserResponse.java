package br.com.careplus.becare.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** DTO de resposta com dados do usuário. */
@Data
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String cpf;
    private LocalDate birthDate;
    private Boolean active;
    private LocalDateTime createdAt;
}
