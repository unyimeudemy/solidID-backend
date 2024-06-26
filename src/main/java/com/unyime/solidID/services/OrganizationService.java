package com.unyime.solidID.services;


import com.unyime.solidID.domain.AuthenticationResponse;
import com.unyime.solidID.domain.dto.OrganizationDto;
import com.unyime.solidID.domain.entities.OrganizationEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface OrganizationService {
    AuthenticationResponse signUp(OrganizationEntity organizationEntity);

    AuthenticationResponse signIn(OrganizationEntity organizationEntity);

    Optional<OrganizationEntity> getOrg(String email);

    Optional<OrganizationEntity> getOrgWithJwtToken(String accessToken);

//    Optional<OrganizationEntity> getOrgByUserEmail(String currrentUserEmail, String orgEmail);
}
