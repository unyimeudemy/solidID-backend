package com.unyime.solidID.repository;

import com.unyime.solidID.domain.entities.StaffMemberEntity;
import com.unyime.solidID.domain.entities.UserOrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserOrganizationRepository extends JpaRepository<UserOrganizationEntity, Integer> {
    Optional<UserOrganizationEntity> findByOrgEmail(String orgEmail);

}
