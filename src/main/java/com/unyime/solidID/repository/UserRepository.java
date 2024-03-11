package com.unyime.solidID.repository;

import com.unyime.solidID.domain.entities.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

     Optional<UserEntity> findByEmail(String email);
}
