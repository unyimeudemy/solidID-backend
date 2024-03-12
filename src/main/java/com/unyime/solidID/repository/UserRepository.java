package com.unyime.solidID.repository;

import com.unyime.solidID.domain.entities.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

     Optional<UserEntity> findByEmail(String email);
}
