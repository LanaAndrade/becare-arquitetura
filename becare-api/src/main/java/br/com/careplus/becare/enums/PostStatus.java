package br.com.careplus.becare.enums;

/**
 * Status de moderação de um Post.
 *
 * <ul>
 *   <li>PENDING  – aguardando análise da IA ou moderador humano</li>
 *   <li>APPROVED – conteúdo aprovado, visível nos feeds</li>
 *   <li>REJECTED – conteúdo rejeitado por violar as diretrizes</li>
 * </ul>
 */
public enum PostStatus {
    PENDING,
    APPROVED,
    REJECTED
}
