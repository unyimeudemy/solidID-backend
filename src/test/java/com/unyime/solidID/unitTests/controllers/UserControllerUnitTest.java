package com.unyime.solidID.unitTests.controllers;


import com.unyime.solidID.TestDataUtility;
import com.unyime.solidID.controllers.UserController;
import com.unyime.solidID.domain.AuthenticationResponse;
import com.unyime.solidID.domain.dto.ErrorResponseDto;
import com.unyime.solidID.domain.dto.UserDto;
import com.unyime.solidID.domain.dto.UserOrganizationDto;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.domain.entities.UserOrganizationEntity;
import com.unyime.solidID.mappers.Mapper;
import com.unyime.solidID.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {
    @Mock
    private Mapper<UserEntity, UserDto> userMapper;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private Mapper<UserOrganizationEntity, UserOrganizationDto> userOrganizationMapper;

    @InjectMocks
    private UserController underTest;

    @Test
    public void testThatSignUpReturnsTokenWithHttp201(){
        UserDto userDto = TestDataUtility.createTestUserDto();
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("jwt token")
                .build();

        when(userMapper.mapFrom(userDto)).thenReturn(userEntity);
        when(userService.signUp(userEntity)).thenReturn(response);

        ResponseEntity<?> result = underTest.signup(userDto);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    public void testThatSignUpReturnsErrorMessageWithStatus409WhenAccountAlreadyExist(){
        UserDto userDto = TestDataUtility.createTestUserDto();
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        AuthenticationResponse signUpUser = AuthenticationResponse.builder()
                .token("Email already exist")
                .build();

        ErrorResponseDto response = ErrorResponseDto.builder()
                .errorMessage("Account already exist")
                .build();

        when(userMapper.mapFrom(userDto)).thenReturn(userEntity);
        when(userService.signUp(userEntity)).thenReturn(signUpUser);

        ResponseEntity<?> result = underTest.signup(userDto);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    public void testThatSignIpReturnsTokenWithHttp200(){
        UserDto userDto = TestDataUtility.createTestUserDto();
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("jwt token")
                .build();

        when(userMapper.mapFrom(userDto)).thenReturn(userEntity);
        when(userService.signIn(userEntity)).thenReturn(response);

        ResponseEntity<?> result = underTest.signin(userDto);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    public void testThatUserCanGetProfileDetailsWithHttp200(){
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        UserDto userDto = TestDataUtility.createTestUserDto();

        when(authentication.getName()).thenReturn(userEntity.getEmail());
        when(userService.getProfile(userEntity.getEmail()))
                .thenReturn(Optional.of(userEntity));
        when(userMapper.mapTo(userEntity)).thenReturn(userDto);

        ResponseEntity<?> result = underTest.getProfile(authentication);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(userDto);

    }

    @Test
    public void testThatGetProfileReturnsErrorMessageWithHttp404WhenUserDoesNotExist(){
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        ErrorResponseDto response = ErrorResponseDto.builder()
                .errorMessage("User does not exist")
                .build();

        when(authentication.getName()).thenReturn(userEntity.getEmail());
        when(userService.getProfile(userEntity.getEmail()))
                .thenReturn(Optional.empty());

        ResponseEntity<?> result = underTest.getProfile(authentication);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    public void testThatUserCanAddOrgAndGetItBackWithHttp200(){
        UserOrganizationEntity userOrganizationEntity = TestDataUtility.createTestUserOrgEntity();
        UserOrganizationDto userOrganizationDto = TestDataUtility.createTestUserOrgDto();

        when(userOrganizationMapper.mapFrom(userOrganizationDto))
                .thenReturn(userOrganizationEntity);
        when(userService.checkIfOrgIsAlreadyRegisteredWithUser(
                userOrganizationEntity.getStaffEmail(),
                userOrganizationEntity.getOrgEmail()
        )).thenReturn(false);

        when(userService.addOrganization(userOrganizationEntity))
                .thenReturn(Optional.of(userOrganizationEntity));

        when(userOrganizationMapper.mapTo(userOrganizationEntity))
                .thenReturn(userOrganizationDto);

        ResponseEntity<?> result = underTest.addOrganization(userOrganizationDto);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(userOrganizationDto);
    }

    @Test
    public void testThatAddOrgReturnsErrorMessageWithHttp409WhenOrgIsAlreadyRegisteredWithUser(){
        UserOrganizationEntity userOrganizationEntity = TestDataUtility.createTestUserOrgEntity();
        UserOrganizationDto userOrganizationDto = TestDataUtility.createTestUserOrgDto();
        ErrorResponseDto response = ErrorResponseDto.builder()
                .errorMessage("You are already registered with the organization")
                .build();

        when(userOrganizationMapper.mapFrom(userOrganizationDto))
                .thenReturn(userOrganizationEntity);

        when(userService.checkIfOrgIsAlreadyRegisteredWithUser(
                userOrganizationEntity.getStaffEmail(),
                userOrganizationEntity.getOrgEmail()
        )).thenReturn(true);

        ResponseEntity<?> result = underTest.addOrganization(userOrganizationDto);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(result.getBody()).isEqualTo(response);
    }
}
