package com.unyime.solidID.unitTests.repositories;


import com.unyime.solidID.TestDataUtility;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;



import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryUnitTest {

    @Mock
    public  UserRepository underTest;

    @Test
    public void testThatFindByEmailReturnsAUser(){
        UserEntity userEntity = TestDataUtility.createTestUserEntity();

        when(underTest.findByEmail(userEntity.getEmail()))
                .thenReturn(Optional.of(userEntity));

        Optional<UserEntity> foundUser = underTest.findByEmail(userEntity.getEmail());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isEqualTo(userEntity);
    }

    @Test
    public void testThatFindByEmailReturnsNullWhenEmailDoesNotExist(){
        when(underTest.findByEmail("abc")).thenReturn(Optional.empty());

        Optional<UserEntity> foundUser = underTest.findByEmail("abc");
        assertThat(foundUser).isEqualTo(Optional.empty());
    }
}
