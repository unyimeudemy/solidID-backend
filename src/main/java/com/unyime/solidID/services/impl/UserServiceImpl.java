package com.unyime.solidID.services.impl;

import com.unyime.solidID.domain.AuthenticationResponse;
import com.unyime.solidID.domain.entities.*;
import com.unyime.solidID.repository.IdentityUsageRecordRepository;
import com.unyime.solidID.repository.UserOrganizationRepository;
import com.unyime.solidID.repository.UserRepository;
import com.unyime.solidID.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserOrganizationRepository userOrganizationRepository;

    private final JwtServiceImpl jwtServiceimpl;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final OrganizationServiceImpl organizationServiceImpl;

    private final IdentityUsageRecordRepository identityUsageRecordRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtServiceImpl jwtServiceimpl, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, HttpServletRequest request, UserOrganizationRepository userOrganizationRepository, OrganizationServiceImpl organizationServiceImpl, IdentityUsageRecordRepository identityUsageRecordRepository) {
        this.userRepository = userRepository;
        this.jwtServiceimpl = jwtServiceimpl;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userOrganizationRepository = userOrganizationRepository;
        this.organizationServiceImpl = organizationServiceImpl;
        this.identityUsageRecordRepository = identityUsageRecordRepository;
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

    @Override
    public Optional<UserOrganizationEntity> addOrganization(UserOrganizationEntity userOrganizationEntity) {

        //--------VERIFIES IF COMPANY IS A REGISTERED COMPANY ON ORGANIZATION ENTITY
        Optional<OrganizationEntity> org = organizationServiceImpl.getOrg(userOrganizationEntity.getOrgEmail());
//        userOrganizationEntity.setOrgName(org.get().getOrganizationName());
        System.out.println("💥💥💥💥 -------> " + org.toString());
        UserOrganizationEntity savedUserOrganizationEntity =
                userOrganizationRepository.save(userOrganizationEntity);
        return Optional.of(savedUserOrganizationEntity);
    }

    @Override
    public Optional<UserOrganizationEntity> getOrganization(String orgEmail) {
//     Optional<OrganizationEntity> org = organizationServiceImpl.getOrg(orgEmail);

        System.out.println("💥💥💥 ---------------> userOrg repo" );

       return userOrganizationRepository.findByOrgEmail(orgEmail);
    }

    @Override
    public List<UserOrganizationEntity> getAllUserOrg(String userEmail) {
        return userOrganizationRepository.findByStaffEmail(userEmail);
    }

    @Override
    public List<IdentityUsageRecordEntity> getIdentityUsageRecord(String userEmail) {
        return identityUsageRecordRepository.findByUserVerifiedEmail(userEmail);
    }
}
