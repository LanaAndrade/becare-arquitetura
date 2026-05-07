package br.com.careplus.becare.repository;

import br.com.careplus.becare.entity.Pillar;
import br.com.careplus.becare.enums.PillarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PillarRepository extends JpaRepository<Pillar, Long> {
    Optional<Pillar> findByType(PillarType type);
}
