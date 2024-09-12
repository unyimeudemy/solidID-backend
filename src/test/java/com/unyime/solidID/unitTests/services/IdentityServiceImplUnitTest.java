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


/**
 * Unit tests for the `IdentityServiceImpl` class, which is responsible for
 * generating and verifying identity tokens for users and organizations.
 *
 * The methods in this class include:
 * - Generating a new identity token which is done with the {@link IdentityServiceImpl#generate(String, String)}
 * - Verifying an identity token and retrieving user or organization details which is done with  {@link IdentityServiceImpl#verify(String, String)}
 * - A private method for record keeping whenever an identity is verified  keepRecordOfIdentityVerification(String, String)}
 */
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

    /**
     * Test that the {@link IdentityServiceImpl#generate(String, String)}
     * method can generate a new identity token for a given user and organization.
     *
     * The generate method uses java.util.Random to generate numbers between 0 and 999,999.
     * And the generated token is stored with {@link IdentityRepository#save(Object)}. After
     * this, another trying to verify the identity will fetch and use token with
     * {@link IdentityServiceImpl#verify(String, String)}
     */
    @Test
    public void testThatIdentityTokenCanBeGenerated(){
        String keyStr = "123456";
        UserEntity userEntity = TestDataUtility.createTestUserEntity();

        // Assuming the chosen profile is an organization not personal
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


    /**
     * This test that the {@link IdentityServiceImpl#verify(String, String)} method can verify an
     * identity token and return the appropriate user details when a personal profile has been chosen.
     *
     * The verify method basically handles two types of profile options. First is the personal
     * profile option which is called "profile" and the second is an email of an organization
     * of which the user is a member.
     *
     * The "Profile" option returns the user's personal profile like state of origin and nationality,
     * while the organization option returns the user's jpb role and otheer details in the specified
     * organization.
     *
     * In this test method, it is assumed the user chose "profile".
     */
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

        //check if key to be verified was generated
        when(identityRepository.findByKey(key))
                .thenReturn(Optional.of(identityURLEntity));

        //uses the email of the user that generated the token to get necessary detail
        when(userRepository.findByEmail(identityURLEntity.getEncodedEmail()))
                .thenReturn(Optional.of(userEntity));

        Optional<VerificationResponse> result = underTest
                .verify(userEntity.getEmail(), key);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(response);
    }


    /**
     * This test continues from {@link #testThatTokenCanBeVerifiedAndUserDetailsReturnedWhenProfileHasBeenChosen()}
     * to test that the {@link IdentityServiceImpl#verify(String, String)} method can verify an
     * identity token and return the appropriate user employment details when an organization has
     * been chosen.
     */
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


    /**
     * This test continues from {@link #testThatTokenCanBeVerifiedAndUserDetailsReturnedWhenOrgHasBeenChosen()}
     * to test that the {@link IdentityServiceImpl#verify(String, String)} returns nothing when
     * attempting to verify an invalid token.
     */
    @Test
    public void testThatNullIsReturnedIfThereIsNoValidKeyToVerify(){
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
