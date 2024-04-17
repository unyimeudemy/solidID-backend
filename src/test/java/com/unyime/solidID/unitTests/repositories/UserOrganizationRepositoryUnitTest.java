package com.unyime.solidID.unitTests.repositories;


import com.unyime.solidID.TestDataUtility;
import com.unyime.solidID.domain.entities.OrganizationEntity;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.domain.entities.UserOrganizationEntity;
import com.unyime.solidID.repository.UserOrganizationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;



@ExtendWith(MockitoExtension.class)
public class UserOrganizationRepositoryUnitTest {

    @Mock
    private UserOrganizationRepository underTest;

    @Test
    public void testThatUserCanGetListOfOrgsUsingStaffEmail(){
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        UserOrganizationEntity userOrganizationEntity = TestDataUtility.createTestUserOrgEntity();

        final List<UserOrganizationEntity> userOrgList =
                List.of(userOrganizationEntity);

        when(underTest.findByStaffEmail(userEntity.getEmail())).thenReturn(userOrgList);

        List<UserOrganizationEntity> result = underTest.findByStaffEmail(userEntity.getEmail());
        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(userOrgList);
    }

    @Test
    public void testThatUserGetsAnEmptyListWhenThereIsNoRegisteredOrg(){
        UserEntity userEntity = TestDataUtility.createTestUserEntity();

        when(underTest.findByStaffEmail(userEntity.getEmail())).thenReturn(Collections.emptyList());
        List<UserOrganizationEntity> result = underTest.findByStaffEmail(userEntity.getEmail());
        assertThat(result).isEmpty();
    }

    @Test
    public void testThatUserCanGetUserOrgByUserEmailAndOrgEmail(){
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        OrganizationEntity organizationEntity = TestDataUtility.createTestOrgEntity();
        UserOrganizationEntity userOrganizationEntity = TestDataUtility.createTestUserOrgEntity();

        when(underTest.findByUserEmailAndOrgEmail(
                userEntity.getEmail(),
                organizationEntity.getEmail()
        )).thenReturn(Optional.of(userOrganizationEntity));

        Optional<UserOrganizationEntity> result = underTest
                .findByUserEmailAndOrgEmail(userEntity.getEmail(), organizationEntity.getEmail());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userOrganizationEntity);
    }


    @Test
    public void testThatEmptyListIsReturnedWhenUserHasNoRegisteredOrg(){
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        OrganizationEntity organizationEntity = TestDataUtility.createTestOrgEntity();

        when(underTest.findByUserEmailAndOrgEmail(
                userEntity.getEmail(),
                organizationEntity.getEmail()
        )).thenReturn(Optional.empty());

        Optional<UserOrganizationEntity> result = underTest.findByUserEmailAndOrgEmail(
                userEntity.getEmail(),
                organizationEntity.getEmail()
        );

        assertThat(result).isEmpty();
    }

}
