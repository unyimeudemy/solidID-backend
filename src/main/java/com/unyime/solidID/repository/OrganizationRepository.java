package com.unyime.solidID.repository;

import com.unyime.solidID.domain.entities.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Integer> {

    Optional<OrganizationEntity> findByEmail(String email);

}
