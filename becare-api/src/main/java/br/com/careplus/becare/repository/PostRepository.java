package br.com.careplus.becare.repository;

import br.com.careplus.becare.entity.Post;
import br.com.careplus.becare.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /** Feed paginado: posts aprovados, mais recentes primeiro. */
    Page<Post> findByStatusOrderByCreatedAtDesc(PostStatus status, Pageable pageable);

    /** Posts de um usuário específico. */
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);

    /** Posts de um usuário em um período (para relatório semanal). */
    @Query("SELECT p FROM Post p WHERE p.user.id = :userId " +
           "AND p.createdAt BETWEEN :start AND :end " +
           "AND p.status = 'APPROVED'")
    List<Post> findApprovedByUserAndPeriod(
            @Param("userId") Long userId,
            @Param("start")  LocalDateTime start,
            @Param("end")    LocalDateTime end);

    /** Contagem por pilar para um usuário num período (VO helper). */
    @Query("SELECT p.pillar.type, COUNT(p) FROM Post p " +
           "WHERE p.user.id = :userId " +
           "AND p.createdAt BETWEEN :start AND :end " +
           "AND p.status = 'APPROVED' " +
           "GROUP BY p.pillar.type ORDER BY COUNT(p) DESC")
    List<Object[]> countByPillarForUserAndPeriod(
            @Param("userId") Long userId,
            @Param("start")  LocalDateTime start,
            @Param("end")    LocalDateTime end);
}
