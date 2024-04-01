package com.unyime.solidID.services.impl;

import com.unyime.solidID.controllers.UserController;
import com.unyime.solidID.domain.AuthenticationResponse;
import com.unyime.solidID.domain.dto.UserDto;
import com.unyime.solidID.domain.entities.OrganizationEntity;
import com.unyime.solidID.repository.OrganizationRepository;
import com.unyime.solidID.services.OrganizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserController userController;
    private final JwtServiceImpl jwtServiceImpl;

    public OrganizationServiceImpl(
            OrganizationRepository organizationRepository,
            PasswordEncoder passwordEncoder,
            UserController userController,
            JwtServiceImpl jwtServiceImpl
    ) {
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
        this.userController = userController;
        this.jwtServiceImpl = jwtServiceImpl;
    }

    @Override
    public AuthenticationResponse signUp(OrganizationEntity organizationEntity) {
        String repEmail = organizationEntity.getRepEmail();
        String repPassword = organizationEntity.getRepPassword();
        AuthenticationResponse token = checkReferenceAccount(repEmail, repPassword);

        if(organizationRepository.findByEmail(organizationEntity.getEmail()).isPresent()){
            return AuthenticationResponse.builder()
                    .token("Email already exist")
                    .build();
        }

        if(!token.getToken().equals("Reference account can not be accessed")){
            String encodedUserPassword = passwordEncoder.encode(organizationEntity.getPassword());
            String encodedRepPassword = passwordEncoder.encode(organizationEntity.getRepPassword());
            organizationEntity.setPassword(encodedUserPassword);
            organizationEntity.setRepPassword(encodedRepPassword);
            organizationRepository.save(organizationEntity);
            var jwtToken = jwtServiceImpl.generateToken(organizationEntity);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
        return token;
    }

    @Override
    public AuthenticationResponse signIn(OrganizationEntity organizationEntity) {
        String email = organizationEntity.getEmail();
        String rawPassword = organizationEntity.getPassword();
        Optional<OrganizationEntity> currentUser = organizationRepository.findByEmail(email);
        if(currentUser.isPresent() && passwordEncoder.matches(rawPassword, currentUser.get().getPassword())){
            var jwtToken = jwtServiceImpl.generateToken(organizationEntity);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
        return AuthenticationResponse.builder()
                .token("Email or password not correct")
                .build();
    }


    private  AuthenticationResponse checkReferenceAccount(String repEmail, String repPassword){
        UserDto userDto = UserDto.builder()
                .email(repEmail)
                .password(repPassword)
                .build();

        try{
            ResponseEntity<AuthenticationResponse> token = userController.signin(userDto);
            return token.getBody();
        }catch(Exception e){
            System.out.println("💥💥💥 -> " + e.getMessage());
            return AuthenticationResponse.builder()
                    .token("Reference account can not be accessed")
                    .build();
        }
    }

}
