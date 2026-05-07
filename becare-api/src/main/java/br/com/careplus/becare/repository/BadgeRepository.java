package br.com.careplus.becare.repository;

import br.com.careplus.becare.entity.Badge;
import br.com.careplus.becare.enums.BadgeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findByUserIdOrderByEarnedAtDesc(Long userId);
    boolean existsByUserIdAndType(Long userId, BadgeType type);
}
