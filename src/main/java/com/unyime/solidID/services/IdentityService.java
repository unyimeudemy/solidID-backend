package com.unyime.solidID.services;

import com.unyime.solidID.domain.entities.UserEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public interface IdentityService {
    String generate(String currentUserEmail);

    Optional<UserEntity> verify(String key);
}
