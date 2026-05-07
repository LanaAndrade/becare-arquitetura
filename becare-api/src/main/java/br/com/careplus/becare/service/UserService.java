package br.com.careplus.becare.service;

import br.com.careplus.becare.dto.request.UserRequest;
import br.com.careplus.becare.dto.response.UserResponse;
import br.com.careplus.becare.entity.User;
import br.com.careplus.becare.exception.BusinessException;
import br.com.careplus.becare.exception.ResourceNotFoundException;
import br.com.careplus.becare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço de gerenciamento de beneficiários do BeCare.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse create(UserRequest request) {
        log.debug("Criando usuário: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Já existe um usuário cadastrado com o e-mail: " + request.getEmail());
        }
        if (userRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("Já existe um usuário cadastrado com o CPF: " + request.getCpf());
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .cpf(request.getCpf())
                .birthDate(request.getBirthDate())
                .build();

        return toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        User user = getOrThrow(id);

        // Verifica conflito de e-mail com outro usuário
        userRepository.findByEmail(request.getEmail())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> { throw new BusinessException("E-mail já utilizado por outro usuário."); });

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setBirthDate(request.getBirthDate());

        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void deactivate(Long id) {
        User user = getOrThrow(id);
        user.setActive(false);
        userRepository.save(user);
        log.info("Usuário {} desativado.", id);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    public User getOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));
    }

    private UserResponse toResponse(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .name(u.getName())
                .email(u.getEmail())
                .cpf(u.getCpf())
                .birthDate(u.getBirthDate())
                .active(u.getActive())
                .createdAt(u.getCreatedAt())
                .build();
    }
}
