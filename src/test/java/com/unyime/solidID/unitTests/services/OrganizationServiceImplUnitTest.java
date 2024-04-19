package com.unyime.solidID.unitTests.services;


import com.unyime.solidID.TestDataUtility;
import com.unyime.solidID.controllers.UserController;
import com.unyime.solidID.domain.AuthenticationResponse;
import com.unyime.solidID.domain.dto.UserDto;
import com.unyime.solidID.domain.entities.OrganizationEntity;
import com.unyime.solidID.repository.OrganizationRepository;
import com.unyime.solidID.services.impl.JwtServiceImpl;
import com.unyime.solidID.services.impl.OrganizationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrganizationServiceImplUnitTest {

    @Mock
    private UserController userController;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtServiceImpl jwtServiceImpl;

    @InjectMocks
    private OrganizationServiceImpl underTest;


    @Test
    public void testThatNewOrgCanRegistered(){
        UserDto userDto = TestDataUtility.createTestUserDto();
        OrganizationEntity organizationEntity = TestDataUtility.createTestOrgEntity();
        UserDto userDtoForSignIn = UserDto.builder()
                .email(organizationEntity.getRepEmail())
                .password(organizationEntity.getRepPassword())
                .build();
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .token("jwt token")
                .build();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("Generated token")
                .build();


        when(userController.signin(userDtoForSignIn))
                .thenReturn(new ResponseEntity<>(authenticationResponse, HttpStatus.OK));

        when(organizationRepository.findByEmail(organizationEntity.getEmail()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(organizationEntity.getPassword()))
                .thenReturn("Encoded Password");

        when(passwordEncoder.encode(organizationEntity.getRepPassword()))
                .thenReturn("Encoded Password");

        when(organizationRepository.save(organizationEntity)).thenReturn(null);

        when(jwtServiceImpl.generateToken(organizationEntity))
                .thenReturn("Generated token");

        AuthenticationResponse result = underTest.signUp(organizationEntity);

        assertThat(result).isEqualTo(response);
    }

    @Test
    public void testThatNewOrgCannotBeRegisteredIfOrgIsAlreadyRegistered(){
        OrganizationEntity organizationEntity = TestDataUtility.createTestOrgEntity();
        UserDto userDtoForSignIn = UserDto.builder()
                .email(organizationEntity.getRepEmail())
                .password(organizationEntity.getRepPassword())
                .build();
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .token("jwt token")
                .build();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("Email already exist")
                .build();


        when(userController.signin(userDtoForSignIn))
                .thenReturn(new ResponseEntity<>(authenticationResponse, HttpStatus.OK));

        when(organizationRepository.findByEmail(organizationEntity.getEmail()))
                .thenReturn(Optional.of(organizationEntity));

        AuthenticationResponse result = underTest.signUp(organizationEntity);

        assertThat(result).isEqualTo(response);
    }

    @Test
    public void testThatNewOrgCannotBeRegisteredIfRepUserAccDoesNotExist(){
        OrganizationEntity organizationEntity = TestDataUtility.createTestOrgEntity();
        UserDto userDtoForSignIn = UserDto.builder()
                .email(organizationEntity.getRepEmail())
                .password(organizationEntity.getRepPassword())
                .build();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("Reference account can not be accessed")
                .build();


        when(userController.signin(userDtoForSignIn))
                .thenReturn(new ResponseEntity<>( HttpStatus.NOT_FOUND));

        AuthenticationResponse result = underTest.signUp(organizationEntity);

        assertThat(result).isEqualTo(response);
    }
}
