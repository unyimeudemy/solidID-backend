package com.unyime.solidID.unitTests.repositories;


import com.unyime.solidID.domain.entities.IdentityURLEntity;
import com.unyime.solidID.repository.IdentityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class IdentityRepositoryUnitTest {

    @Mock
    private IdentityRepository underTest;


    @Test
    public void testThatTokenForUserToBeVerifiedIsPresentForVerification(){
        String key = "123456";
        IdentityURLEntity identity = IdentityURLEntity.builder()
                .key(key)
                .build();

        when(underTest.findByKey(key)).thenReturn(Optional.of(identity));

        Optional<IdentityURLEntity> result = underTest.findByKey(key);

        assertThat(result).isPresent();
        assertThat(result).isEqualTo(Optional.of(identity));

    }

    @Test
    public void testThatOptionalEmptyIsReturnIfTokenIsInvalidOrAlreadyVerified(){
        when(underTest.findByKey(any(String.class))).thenReturn(Optional.empty());

        Optional<IdentityURLEntity> result = underTest.findByKey("any invalid or already used token");
        assertThat(result).isEmpty();
    }
}
