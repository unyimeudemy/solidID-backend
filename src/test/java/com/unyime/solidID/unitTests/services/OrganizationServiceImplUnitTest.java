package com.unyime.solidID.unitTests.services;


import com.unyime.solidID.TestDataUtility;
import com.unyime.solidID.controllers.UserController;
import com.unyime.solidID.domain.AuthenticationResponse;
import com.unyime.solidID.domain.dto.UserDto;
import com.unyime.solidID.domain.entities.OrganizationEntity;
import com.unyime.solidID.domain.entities.UserEntity;
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

import javax.swing.text.html.Option;
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

    @Test
    public void testThatOrgAccCanSignInAndGetToken(){
        OrganizationEntity organizationEntity = TestDataUtility.createTestOrgEntity();
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        AuthenticationResponse response = AuthenticationResponse.builder()
                        .token("JwtToken")
                        .build();

        when(organizationRepository.findByEmail(organizationEntity.getEmail()))
                .thenReturn(Optional.of(organizationEntity));

        when(passwordEncoder.matches(
                        organizationEntity.getPassword(), userEntity.getPassword()
                )
        ).thenReturn(true);

        when(jwtServiceImpl.generateToken(organizationEntity)).thenReturn("JwtToken");

        AuthenticationResponse result = underTest.signIn(organizationEntity);
        assertThat(result).isEqualTo(response);

    }

    @Test
    public void testThatOrgAccCannotLogInIfEmailIsInvalid(){
        OrganizationEntity organizationEntity = TestDataUtility.createTestOrgEntity();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("Email or password not correct")
                .build();

        when(organizationRepository.findByEmail(organizationEntity.getEmail()))
                .thenReturn(Optional.empty());

        AuthenticationResponse result = underTest.signIn(organizationEntity);
        assertThat(result).isEqualTo(response);
    }

    @Test
    public void testThatOrgAccCannotLogInIfPasswordIsNotCorrect(){
        OrganizationEntity organizationEntity = TestDataUtility.createTestOrgEntity();
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("Email or password not correct")
                .build();

        when(organizationRepository.findByEmail(organizationEntity.getEmail()))
                .thenReturn(Optional.of(organizationEntity));

        when(passwordEncoder.matches(
                        organizationEntity.getPassword(), userEntity.getPassword()
                )
        ).thenReturn(false);

        AuthenticationResponse result = underTest.signIn(organizationEntity);
        assertThat(result).isEqualTo(response);

    }

    @Test
    public void testThatCurrentUseCanGetOrgViaOrgEmail(){
        OrganizationEntity organizationEntity = TestDataUtility.createTestOrgEntity();

        when(organizationRepository.findByEmail(organizationEntity.getEmail()))
                .thenReturn(Optional.of(organizationEntity));

        Optional<OrganizationEntity> result = underTest.getOrg(organizationEntity.getEmail());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(organizationEntity);
    }

    @Test
    public void testThatOrgCanBeGottenViaCurrentUserToken(){
        OrganizationEntity organizationEntity = TestDataUtility.createTestOrgEntity();
        String accessToken = "Valid jwt token";
        when(jwtServiceImpl.extractUsername(accessToken))
                .thenReturn(organizationEntity.getEmail());

        when(organizationRepository.findByEmail(organizationEntity.getEmail()))
                .thenReturn(Optional.of(organizationEntity));

        Optional<OrganizationEntity> result = underTest.getOrgWithJwtToken(accessToken);
        assertThat(result).isPresent();
        assertThat(result).isEqualTo(Optional.of(organizationEntity));
    }

    @Test
    public void testThatRepAccIsValid(){
        OrganizationEntity organizationEntity = TestDataUtility.createTestOrgEntity();
        UserDto userDto = UserDto.builder()
                .email(organizationEntity.getRepEmail())
                .password(organizationEntity.getRepPassword())
                .build();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("Valid jwt Token")
                .build();

        when(userController.signin(userDto))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        AuthenticationResponse result = underTest
                .checkReferenceAccount(
                        organizationEntity.getRepEmail(),
                        organizationEntity.getRepPassword()
                );

        assertThat(result).isEqualTo(response);
    }

    @Test
    public void testThatRepAccIsInValid(){
        OrganizationEntity organizationEntity = TestDataUtility.createTestOrgEntity();
        UserDto userDto = UserDto.builder()
                .email(organizationEntity.getRepEmail())
                .password(organizationEntity.getRepPassword())
                .build();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("Reference account can not be accessed")
                .build();

        when(userController.signin(userDto))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        AuthenticationResponse result = underTest
                .checkReferenceAccount(
                        organizationEntity.getRepEmail(),
                        organizationEntity.getRepPassword()
                );

        assertThat(result).isEqualTo(response);
    }

}
