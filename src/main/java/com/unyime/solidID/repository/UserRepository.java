package com.unyime.solidID.repository;

import com.unyime.solidID.domain.entities.UserEntity;
import jakarta.persistence.Id;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    public Optional<UserEntity> findByEmail(String email);
}
