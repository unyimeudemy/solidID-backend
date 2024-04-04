package com.unyime.solidID.services;

import com.unyime.solidID.domain.VerificationResponse;
import com.unyime.solidID.domain.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface IdentityService {
    String generate(String currentUserEmail, String orgEmail);

    Optional<VerificationResponse> verify(String currentUserEmail, String key);
}
