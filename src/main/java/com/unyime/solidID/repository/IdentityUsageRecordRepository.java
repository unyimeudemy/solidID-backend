package com.unyime.solidID.repository;

import com.unyime.solidID.domain.entities.IdentityUsageRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface IdentityUsageRecordRepository extends JpaRepository<IdentityUsageRecordEntity, Integer> {

    List<IdentityUsageRecordEntity> findByUserVerifiedEmail(String userVerifiedEmail);
}
