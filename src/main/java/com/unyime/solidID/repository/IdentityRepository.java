package com.unyime.solidID.repository;

import com.unyime.solidID.domain.entities.IdentityURLEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IdentityRepository extends JpaRepository<IdentityURLEntity, Integer> {
    Optional<IdentityURLEntity> findByKey(String key);

    void deleteByKey(String key);
}
