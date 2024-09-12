package com.unyime.solidID.repository;

import com.unyime.solidID.domain.entities.StaffMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface StaffMemberRepository extends JpaRepository<StaffMemberEntity, Integer> {
    Optional<StaffMemberEntity> findByStaffEmail(String staffEmail);

    List<StaffMemberEntity> findByOrgEmail(String orgEmail);

    @Query("SELECT e FROM StaffMemberEntity e WHERE e.staffEmail = ?1 AND e.orgEmail = ?2")
    Optional<StaffMemberEntity> findByStaffEmailAndOrgEmail(String staffEmail, String orgEmail);

}
