package br.com.careplus.becare.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Springdoc OpenAPI (Swagger UI).
 *
 * <p>Documentação acessível em: <a href="http://localhost:8080/swagger-ui.html">
 * http://localhost:8080/swagger-ui.html</a></p>
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI becareOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BeCare API")
                        .description("""
                                **BeCare** é o módulo social e gamificado do app Care Plus.
                                
                                Beneficiários publicam microações saudáveis, acumulam insígnias e participam
                                de desafios colaborativos alinhados aos 7 Pilares de bem-estar:
                                Prevenção, Alimentação, Hidratação, Atividade Física, Saúde Mental,
                                Engajamento Social e Sono.
                                
                                **Sprint 3 – SOA e WebServices** | FIAP 2025
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Grupo BeCare – FIAP")
                                .email("rm553190@fiap.com.br"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
