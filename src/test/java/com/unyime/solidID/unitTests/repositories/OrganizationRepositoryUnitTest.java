package com.unyime.solidID.unitTests.repositories;

import com.unyime.solidID.TestDataUtility;
import com.unyime.solidID.domain.entities.OrganizationEntity;
import com.unyime.solidID.domain.entities.StaffMemberEntity;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.repository.OrganizationRepository;
import com.unyime.solidID.repository.StaffMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class OrganizationRepositoryUnitTest {

    @Mock
    public OrganizationRepository underTest;

    @Test
    public void testThatFindByStaffEmailReturnsAStaffMember(){
        OrganizationEntity organizationEntity = TestDataUtility.createTestOrgEntity();

        when(underTest.findByEmail(organizationEntity.getEmail()))
                .thenReturn(Optional.of(organizationEntity));

        Optional<OrganizationEntity> foundUser = underTest.findByEmail(organizationEntity.getEmail());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isEqualTo(organizationEntity);
    }

    @Test
    public void testThatFindByEmailReturnsNullWhenEmailDoesNotExist(){
        OrganizationEntity organizationEntity = TestDataUtility.createTestOrgEntity();

        when(underTest.findByEmail(organizationEntity.getEmail()))
                .thenReturn(Optional.empty());

        Optional<OrganizationEntity> foundUser = underTest.findByEmail(organizationEntity.getEmail());
        assertThat(foundUser).isEmpty();
    }
}
