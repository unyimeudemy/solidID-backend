package com.unyime.solidID.unitTests.services;

import com.unyime.solidID.TestDataUtility;
import com.unyime.solidID.domain.VerificationResponse;
import com.unyime.solidID.domain.entities.*;
import com.unyime.solidID.repository.IdentityRepository;
import com.unyime.solidID.repository.IdentityUsageRecordRepository;
import com.unyime.solidID.repository.UserOrganizationRepository;
import com.unyime.solidID.repository.UserRepository;
import com.unyime.solidID.services.impl.IdentityServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@ExtendWith(MockitoExtension.class)
public class IdentityServiceImplUnitTest {

    @Mock
    private IdentityRepository identityRepository;

    @Mock
    private UserOrganizationRepository userOrganizationRepository;

    @Mock
    private IdentityUsageRecordRepository identityUsageRecordRepository;


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private IdentityServiceImpl underTest;

    @Test
    public void testThatIdentityTokenCanBeGenerated(){
        String keyStr = "123456";
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        OrganizationEntity organizationEntity = TestDataUtility.createTestOrgEntity();


        IdentityURLEntity identityURLEntity = IdentityURLEntity.builder()
                .encodedEmail(userEntity.getEmail())
                .key(keyStr)
                .orgEmail(organizationEntity.getEmail())
                .build();

        when(identityRepository.save(any(IdentityURLEntity.class))).thenReturn(identityURLEntity);

        String result = underTest.generate(userEntity.getEmail(), organizationEntity.getEmail());
        assertThat(result).isEqualTo(identityURLEntity.getKey());
    }


    @Test
    public void testThatTokenCanBeVerifiedAndUserDetailsReturnedWhenProfileHasBeenChosen(){
        String key = "123456";
        UserEntity verifiedUser = TestDataUtility.createTestUserEntity();
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        IdentityURLEntity identityURLEntity = TestDataUtility.createIdentityURLEntity();
        identityURLEntity.setOrgEmail("Profile");
        VerificationResponse response =  VerificationResponse.builder()
                .firstName(verifiedUser.getFirstName())
                .lastName(verifiedUser.getLastName())
                .otherName(verifiedUser.getOtherName())
                .stateOfOrigin(verifiedUser.getStateOfOrigin())
                .email(verifiedUser.getEmail())
                .image(verifiedUser.getImage())
                .nationality(verifiedUser.getNationality())
                .build();

        when(identityRepository.findByKey(key))
                .thenReturn(Optional.of(identityURLEntity));

        when(userRepository.findByEmail(identityURLEntity.getEncodedEmail()))
                .thenReturn(Optional.of(userEntity));

        Optional<VerificationResponse> result = underTest
                .verify(userEntity.getEmail(), key);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(response);
    }


    @Test
    public void testThatTokenCanBeVerifiedAndUserDetailsReturnedWhenOrgHasBeenChosen(){
        String key = "123456";
        UserOrganizationEntity userOrganizationEntity = TestDataUtility.createTestUserOrgEntity();
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        IdentityURLEntity identityURLEntity = TestDataUtility.createIdentityURLEntity();
        identityURLEntity.setOrgEmail("Not Profile");
        VerificationResponse response = VerificationResponse.builder()
                .firstName(userOrganizationEntity.getStaffName())
                .staffRole(userOrganizationEntity.getStaffRole())
                .orgName(userOrganizationEntity.getOrgName())
                .staffId(userOrganizationEntity.getStaffId())
                .build();

        when(identityRepository.findByKey(key)).thenReturn(Optional.of(identityURLEntity));

        when(userOrganizationRepository.findByUserEmailAndOrgEmail(
                identityURLEntity.getEncodedEmail(),
                identityURLEntity.getOrgEmail()
        )).thenReturn(Optional.of(userOrganizationEntity));

        Optional<VerificationResponse> result = underTest
                .verify(userEntity.getEmail(), key);

        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo(response.getFirstName());
        assertThat(result.get()).isEqualTo(response);
    }

    @Test
    public void testThatNullIsReturnedIfThereIsNoKeyToVerify(){
        String key = "123456";
        IdentityURLEntity identityURLEntity = TestDataUtility.createIdentityURLEntity();
        identityURLEntity.setOrgEmail("Profile or Org");
        UserEntity userEntity = TestDataUtility.createTestUserEntity();


        when(identityRepository.findByKey(key)).thenReturn(Optional.empty());

        Optional<VerificationResponse> result = underTest
                .verify(userEntity.getEmail(), key);

        assertThat(result).isEmpty();
    }
}
