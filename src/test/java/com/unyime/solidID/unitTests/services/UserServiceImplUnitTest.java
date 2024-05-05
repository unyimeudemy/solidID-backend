package com.unyime.solidID.unitTests.services;


import com.unyime.solidID.TestDataUtility;
import com.unyime.solidID.domain.AuthenticationResponse;
import com.unyime.solidID.domain.entities.IdentityUsageRecordEntity;
import com.unyime.solidID.domain.entities.OrganizationEntity;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.domain.entities.UserOrganizationEntity;
import com.unyime.solidID.repository.IdentityUsageRecordRepository;
import com.unyime.solidID.repository.UserOrganizationRepository;
import com.unyime.solidID.repository.UserRepository;
import com.unyime.solidID.services.impl.JwtServiceImpl;
import com.unyime.solidID.services.impl.OrganizationServiceImpl;
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

import java.util.List;
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

    @Mock
    private OrganizationServiceImpl organizationServiceImpl;

    @Mock
    private UserOrganizationRepository userOrganizationRepository;

    @Mock
    private IdentityUsageRecordRepository identityUsageRecordRepository;
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

    @Test
    public void testThatCurrentUserCanGetHisProfileDetail(){
        UserEntity userEntity = TestDataUtility.createTestUserEntity();

        when(userRepository.findByEmail(userEntity.getEmail()))
                .thenReturn(Optional.of(userEntity));

        Optional<UserEntity> result = underTest.getProfile(userEntity.getEmail());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userEntity);
    }

    @Test
    public void testThatGetProfileReturnsNullWhenUserDoesNotExist(){

        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.empty());

        Optional<UserEntity> result = underTest.getProfile("IncorrectEmail");
        assertThat(result).isEmpty();
    }


    @Test
    public void testThatUserCanAddOrgToAccAndAddedOrgIsReturned(){
        OrganizationEntity organizationEntity = TestDataUtility.createTestOrgEntity();
        UserOrganizationEntity userOrganizationEntity =
                TestDataUtility.createTestUserOrgEntity();

        when(organizationServiceImpl.getOrg(userOrganizationEntity.getOrgEmail()))
                .thenReturn(Optional.of(organizationEntity));

        when(userOrganizationRepository.save(userOrganizationEntity))
                .thenReturn(userOrganizationEntity);

        Optional<UserOrganizationEntity> result =
                underTest.addOrganization(userOrganizationEntity);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userOrganizationEntity);
    }

//    @Test
//    public void testThatAddOrgReturnsNullIfOrgIsNotRegistered(){
//        UserOrganizationEntity userOrganizationEntity =
//                TestDataUtility.createTestUserOrgEntity();
//
//        when(organizationServiceImpl.getOrg(any(String.class)))
//                .thenReturn(Optional.empty());
//
//        Optional<UserOrganizationEntity> result =
//                underTest.addOrganization(userOrganizationEntity);
//
//        assertThat(result).isEmpty();
//    }

    @Test
    public void testThatAlreadyRegisteredOrgWithUserReturnsTrue(){
        UserOrganizationEntity userOrganizationEntity =
                TestDataUtility.createTestUserOrgEntity();

        when(userOrganizationRepository
                .findByUserEmailAndOrgEmail(
                        userOrganizationEntity.getStaffEmail(),
                        userOrganizationEntity.getOrgEmail()
                )
        ).thenReturn(Optional.of(userOrganizationEntity));

        Boolean result = underTest.checkIfOrgIsAlreadyRegisteredWithUser(
                userOrganizationEntity.getStaffEmail(),
                userOrganizationEntity.getOrgEmail()
        );

        assertThat(result).isTrue();
    }

    @Test
    public void testThatNoneRegisteredOrgWithUserReturnsFalse(){

        when(userOrganizationRepository
                .findByUserEmailAndOrgEmail(
                        any(String.class),
                        any(String.class)
                )
        ).thenReturn(Optional.empty());

        Boolean result = underTest.checkIfOrgIsAlreadyRegisteredWithUser(
                "any wrong user email",
                "any wrong org email"
        );

        assertThat(result).isFalse();
    }


    @Test
    public void testThatUserCanGetListOfAllOrgsHeIsAMember(){
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        UserOrganizationEntity userOrganizationEntity = TestDataUtility.createTestUserOrgEntity();
        List<UserOrganizationEntity> response = List.of(userOrganizationEntity);

        when(userOrganizationRepository.findByStaffEmail(userEntity.getEmail()))
                .thenReturn(List.of(userOrganizationEntity));

        List<UserOrganizationEntity> result = underTest.getAllUserOrg(userEntity.getEmail());
        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(response);
    }


    @Test
    public void TestThatUserCanGetIdentityUsageRecord(){
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        IdentityUsageRecordEntity identityUsageRecordEntity =
                TestDataUtility.createIdentityUsageRecordEntity();

        List<IdentityUsageRecordEntity> response = List.of(identityUsageRecordEntity);

        when(identityUsageRecordRepository.findByUserVerifiedEmail(userEntity.getEmail()))
                .thenReturn(List.of(identityUsageRecordEntity));

        List<IdentityUsageRecordEntity> result =
                underTest.getIdentityUsageRecord(userEntity.getEmail());

        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(response);

    }
}
