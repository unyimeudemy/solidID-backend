package com.unyime.solidID.services;

import com.unyime.solidID.domain.AuthenticationResponse;
import com.unyime.solidID.domain.entities.IdentityUsageRecordEntity;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.domain.entities.UserOrganizationEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface UserService {

    AuthenticationResponse signUp(UserEntity userEntity);

    AuthenticationResponse signIn(UserEntity userEntity);

    Optional<UserEntity> getProfile(String currentUserEmail);

    Optional<UserOrganizationEntity> addOrganization(UserOrganizationEntity userOrganizationEntity);

    Boolean checkIfOrgIsAlreadyRegisteredWithUser(String currentUserEmail, String orgEmail);

    List<UserOrganizationEntity> getAllUserOrg(String userEmail);

    List<IdentityUsageRecordEntity> getIdentityUsageRecord(String userEmail);
}
