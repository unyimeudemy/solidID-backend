package com.unyime.solidID.services.impl;

import com.unyime.solidID.domain.entities.IdentityURLEntity;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.repository.IdentityRepository;
import com.unyime.solidID.repository.UserRepository;
import com.unyime.solidID.services.IdentityService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class IdentityServiceImpl implements IdentityService {

    private final IdentityRepository identityRepository;

    private final UserRepository userRepository;

    public IdentityServiceImpl(PasswordEncoder passwordEncoder, IdentityRepository identityRepository, UserRepository userRepository) {
        this.identityRepository = identityRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String generate(String currentUserEmail) {
        Random random = new Random();
        int key = random.nextInt(1000000);
        String keyStr = String.valueOf(key);

        IdentityURLEntity identityURLEntity = IdentityURLEntity.builder()
                .encodedEmail(currentUserEmail)
                .key(keyStr)
                .build();

        IdentityURLEntity savedUrl = identityRepository.save(identityURLEntity);
        return savedUrl.getKey();
    }

    @Override
    public Optional<UserEntity> verify(String key) {
        Optional<IdentityURLEntity> identityURLEntity = identityRepository.findByKey(key);
        identityRepository.deleteByKey(key);
        if(identityURLEntity.isPresent()){
            String userEmail = identityURLEntity.get().getEncodedEmail();
            return userRepository.findByEmail(userEmail);
        }else{
            return Optional.empty();
        }
    }
}
