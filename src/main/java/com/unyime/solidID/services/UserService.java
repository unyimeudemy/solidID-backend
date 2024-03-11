package com.unyime.solidID.services;

import com.unyime.solidID.domain.AuthenticationResponse;
import com.unyime.solidID.domain.entities.UserEntity;

import java.util.Optional;

public interface UserService {

    AuthenticationResponse signUp(UserEntity userEntity);

    AuthenticationResponse signIn(UserEntity userEntity);

    Optional<UserEntity> getProfile(String currentUserEmail);
}
