package com.unyime.solidID.repository;

import com.unyime.solidID.domain.entities.OrganizationEntity;
import com.unyime.solidID.domain.entities.StaffMemberEntity;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.domain.entities.UserOrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserOrganizationRepository extends JpaRepository<UserOrganizationEntity, Integer> {
    Optional<UserOrganizationEntity> findByOrgEmail(String orgEmail);

    List<UserOrganizationEntity> findByStaffEmail(String staffEmail);


    @Query(value = "SELECT u FROM UserOrganizationEntity u WHERE u.staffEmail = ?1 AND u.orgEmail = ?2")
    Optional<UserOrganizationEntity> findByUserEmailAndOrgEmail(String staffEmail, String orgEmail);

//    @Query(value = "SELECT o FROM UserOrganizationEntity o WHERE o.staffEmail = ?1 AND o.orgEmail = ?2")
//    Optional<UserOrganizationEntity> getOrgByUserEmail(String currentUserEmail, String orgEmail);
}
