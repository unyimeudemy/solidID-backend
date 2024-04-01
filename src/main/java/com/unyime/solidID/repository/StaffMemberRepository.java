package com.unyime.solidID.repository;

import com.unyime.solidID.domain.entities.StaffMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface StaffMemberRepository extends JpaRepository<StaffMemberEntity, Integer> {
    Optional<StaffMemberEntity> findByStaffEmail(String staffEmail);
}
