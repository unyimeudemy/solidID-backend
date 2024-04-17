package com.unyime.solidID.unitTests.services;


import com.unyime.solidID.TestDataUtility;
import com.unyime.solidID.domain.AuthenticationResponse;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.repository.UserRepository;
import com.unyime.solidID.services.impl.JwtServiceImpl;
import com.unyime.solidID.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtServiceImpl jwtServiceImpl;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private UserServiceImpl underTest;

    @Test
    public void testThatUserCanSignupAndTokenIsReturned(){
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        userEntity.setPassword("123456abc");

        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("jwtToken")
                .build();

        when(userRepository.save(userEntity)).thenReturn(null);
        when(jwtServiceImpl.generateToken(userEntity)).thenReturn("jwtToken");
        when(passwordEncoder.encode(userEntity.getPassword())).thenReturn("123456abc");


        AuthenticationResponse result = underTest.signUp(userEntity);
        assertThat(result).isEqualTo(response);
    }


    @Test
    public void testThatErrorEmailAlreadyExistIsReturnedIfUserIsAlreadyExist(){
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("Email already exist")
                .build() ;

        when(userRepository.findByEmail(userEntity.getEmail()))
                .thenReturn(Optional.of(userEntity));

        AuthenticationResponse result = underTest.signUp(userEntity);
        assertThat(result).isEqualTo(response);
    }




    @Test
    public void testThatUserCanSignInAndTokenIsReturned(){
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("jwtToken")
                .build();

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        userEntity.getEmail(), userEntity.getPassword()
                );

        when(authenticationManager.authenticate(eq(token))).thenReturn(null);

        when(userRepository.findByEmail(userEntity.getEmail()))
                .thenReturn(Optional.of(userEntity));

        when(jwtServiceImpl.generateToken(userEntity)).thenReturn("jwtToken");

        AuthenticationResponse result = underTest.signIn(userEntity);
        assertThat(result).isEqualTo(response);
    }


    @Test
    public void testThatOnSignInAppropriateErrorMessageIsReturnedWhenEmailNotCorrect(){
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("Email or password not correct")
                .build();

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        userEntity.getEmail(), userEntity.getPassword()
                );

        when(authenticationManager.authenticate(Mockito.eq(token))).thenReturn(null);

        when(userRepository.findByEmail(userEntity.getEmail()))
                .thenReturn(Optional.empty());

        AuthenticationResponse result = underTest.signIn(userEntity);
        assertThat(result).isEqualTo(response);
    }


}
