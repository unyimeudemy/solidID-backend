package com.unyime.solidID.services.impl;

import com.unyime.solidID.domain.AuthenticationResponse;
import com.unyime.solidID.domain.entities.Role;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.repository.UserRepository;
import com.unyime.solidID.services.JwtServiceImpl;
import com.unyime.solidID.services.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JwtServiceImpl jwtServiceimpl;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtServiceImpl jwtServiceimpl, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, HttpServletRequest request) {
        this.userRepository = userRepository;
        this.jwtServiceimpl = jwtServiceimpl;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthenticationResponse signUp(UserEntity userEntity) {
        var user = UserEntity.builder()
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .otherName(userEntity.getOtherName())
                .age(userEntity.getAge())
                .email(userEntity.getEmail())
                .password(passwordEncoder.encode(userEntity.getPassword()))
                .nationality(userEntity.getNationality())
                .stateOfOrigin(userEntity.getStateOfOrigin())
                .role(Role.USER)
                .image(userEntity.getImage())
                .build();

        if(userRepository.findByEmail(userEntity.getEmail()).isPresent()){
            return AuthenticationResponse.builder()
                    .token("Email already exist")
                    .build() ;
        }else {
            userRepository.save(user);
            var jwtToken = jwtServiceimpl.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
    }

    @Override
    public AuthenticationResponse signIn(UserEntity userEntity) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userEntity.getEmail(),
                            userEntity.getPassword()
                    )
            );
        }catch (Exception e){
            System.out.println("exception 💥💥💥 --> " + e.getMessage());
        }


        var user = userRepository.findByEmail(userEntity.getEmail()).orElseThrow();
        String jwtToken = jwtServiceimpl.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public Optional<UserEntity> getProfile(String currentUserName) {
        return userRepository.findByEmail(currentUserName);
    }
}
