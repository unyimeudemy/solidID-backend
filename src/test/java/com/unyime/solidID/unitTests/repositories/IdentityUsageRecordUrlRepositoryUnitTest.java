package com.unyime.solidID.unitTests.repositories;

import com.unyime.solidID.TestDataUtility;
import com.unyime.solidID.domain.entities.IdentityUsageRecordEntity;
import com.unyime.solidID.domain.entities.OrganizationEntity;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.repository.IdentityUsageRecordRepository;
import com.unyime.solidID.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class IdentityUsageRecordUrlRepositoryUnitTest {

    @Mock
    public IdentityUsageRecordRepository underTest;

    @Test
    public void testThatUserGetsListOfIdentityUsageViaVerifiedEmail(){
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        IdentityUsageRecordEntity identityUsageRecordEntity =
                TestDataUtility.createIdentityUsageRecordEntity();

        List<IdentityUsageRecordEntity> record = List.of(
                identityUsageRecordEntity
        );

        when(underTest.findByUserVerifiedEmail(userEntity.getEmail()))
                .thenReturn(record);

        List<IdentityUsageRecordEntity> result =
                underTest.findByUserVerifiedEmail(userEntity.getEmail());

        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(record);
    }

    @Test
    public void testThatUserGetsEmptyListIfEmailWasNotVerified(){
        UserEntity userEntity = TestDataUtility.createTestUserEntity();

        when(underTest.findByUserVerifiedEmail(userEntity.getEmail()))
                .thenReturn(Collections.emptyList());

        List<IdentityUsageRecordEntity> result =
                underTest.findByUserVerifiedEmail(userEntity.getEmail());

        assertThat(result).isEmpty();
    }

}
