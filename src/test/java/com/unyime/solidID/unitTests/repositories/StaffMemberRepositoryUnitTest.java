package com.unyime.solidID.unitTests.repositories;

import com.unyime.solidID.TestDataUtility;
import com.unyime.solidID.domain.entities.StaffMemberEntity;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.repository.StaffMemberRepository;
import com.unyime.solidID.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StaffMemberRepositoryUnitTest {

    @Mock
    public StaffMemberRepository underTest;

    @Test
    public void testThatFindByStaffEmailReturnsAStaffMember(){
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        StaffMemberEntity staffMemberEntity = TestDataUtility.createTestStaffMemberEntity();

        when(underTest.findByStaffEmail(userEntity.getEmail()))
                .thenReturn(Optional.of(staffMemberEntity));

        Optional<StaffMemberEntity> foundUser = underTest.findByStaffEmail(userEntity.getEmail());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isEqualTo(staffMemberEntity);
    }

    @Test
    public void testThatFindByEmailReturnsNullWhenEmailDoesNotExist(){
        UserEntity userEntity = TestDataUtility.createTestUserEntity();

        when(underTest.findByStaffEmail(userEntity.getEmail()))
                .thenReturn(Optional.empty());

        Optional<StaffMemberEntity> foundUser = underTest.findByStaffEmail(userEntity.getEmail());
        assertThat(foundUser).isEmpty();
    }
}
