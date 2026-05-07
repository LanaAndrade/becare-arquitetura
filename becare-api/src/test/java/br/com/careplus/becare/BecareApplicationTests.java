package br.com.careplus.becare;

import br.com.careplus.becare.dto.request.UserRequest;
import br.com.careplus.becare.dto.response.UserResponse;
import br.com.careplus.becare.enums.BadgeType;
import br.com.careplus.becare.enums.PostStatus;
import br.com.careplus.becare.repository.PillarRepository;
import br.com.careplus.becare.service.BadgeService;
import br.com.careplus.becare.service.PostService;
import br.com.careplus.becare.service.UserService;
import br.com.careplus.becare.dto.request.PostRequest;
import br.com.careplus.becare.dto.request.ModerationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Testes de integração da BeCare API.
 *
 * <p>Utiliza banco H2 em memória e contexto Spring completo
 * para validar os principais fluxos de negócio.</p>
 */
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BecareApplicationTests {

    @Autowired UserService    userService;
    @Autowired PostService    postService;
    @Autowired BadgeService   badgeService;
    @Autowired PillarRepository pillarRepository;

    // ── Helpers ──────────────────────────────────────────────────────────────

    private UserResponse criarUsuario(String nome, String email, String cpf) {
        UserRequest req = new UserRequest();
        req.setName(nome);
        req.setEmail(email);
        req.setCpf(cpf);
        req.setBirthDate(LocalDate.of(1995, 6, 15));
        return userService.create(req);
    }

    // ── Testes de Usuário ─────────────────────────────────────────────────

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void deveCriarUsuario() {
        UserResponse user = criarUsuario("Ana Lima", "ana@becare.com", "123.456.789-00");
        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo("Ana Lima");
        assertThat(user.getActive()).isTrue();
    }

    @Test
    @DisplayName("Não deve criar usuário com e-mail duplicado")
    void naoDeveCriarUsuarioComEmailDuplicado() {
        criarUsuario("Ana Lima", "ana@becare.com", "123.456.789-00");
        assertThatThrownBy(() -> criarUsuario("Ana Souza", "ana@becare.com", "987.654.321-00"))
                .hasMessageContaining("e-mail");
    }

    @Test
    @DisplayName("Deve desativar usuário")
    void deveDesativarUsuario() {
        UserResponse user = criarUsuario("Carlos", "carlos@becare.com", "111.222.333-44");
        userService.deactivate(user.getId());
        assertThat(userService.findById(user.getId()).getActive()).isFalse();
    }

    // ── Testes de Post ────────────────────────────────────────────────────

    @Test
    @DisplayName("Deve criar post e iniciar com status PENDING")
    void deveCriarPostComStatusPending() {
        UserResponse user   = criarUsuario("Bia", "bia@becare.com", "555.666.777-88");
        Long pillarId = pillarRepository.findAll().get(0).getId();

        PostRequest req = new PostRequest();
        req.setUserId(user.getId());
        req.setPillarId(pillarId);
        req.setDescription("Tomei 2 litros de água hoje!");

        var post = postService.create(req);
        assertThat(post.getStatus()).isEqualTo(PostStatus.PENDING);
    }

    @Test
    @DisplayName("Deve aprovar post na moderação")
    void deveAprovarPost() {
        UserResponse user = criarUsuario("Diego", "diego@becare.com", "999.888.777-66");
        Long pillarId = pillarRepository.findAll().get(0).getId();

        PostRequest req = new PostRequest();
        req.setUserId(user.getId());
        req.setPillarId(pillarId);
        req.setDescription("Fui correr 5km hoje de manhã!");

        var post = postService.create(req);

        ModerationRequest mod = new ModerationRequest();
        mod.setStatus(PostStatus.APPROVED);
        mod.setNote("Conteúdo aprovado.");

        var approved = postService.moderate(post.getId(), mod);
        assertThat(approved.getStatus()).isEqualTo(PostStatus.APPROVED);
    }

    // ── Testes de Insígnia ────────────────────────────────────────────────

    @Test
    @DisplayName("Deve conceder insígnia ao usuário")
    void deveConcederInsignia() {
        UserResponse user = criarUsuario("Eva", "eva@becare.com", "444.333.222-11");
        var badge = badgeService.award(user.getId(), BadgeType.FIRST_POST);
        assertThat(badge.getType()).isEqualTo(BadgeType.FIRST_POST);
        assertThat(badge.getUserId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("Não deve conceder insígnia duplicada")
    void naoDeveConcederInsigniaDuplicada() {
        UserResponse user = criarUsuario("Fábio", "fabio@becare.com", "000.111.222-33");
        badgeService.award(user.getId(), BadgeType.FIRST_POST);
        assertThatThrownBy(() -> badgeService.award(user.getId(), BadgeType.FIRST_POST))
                .hasMessageContaining("insígnia");
    }

    @Test
    @DisplayName("Contexto Spring carrega corretamente")
    void contextLoads() {
        assertThat(userService).isNotNull();
        assertThat(postService).isNotNull();
    }
}
