package com.unyime.solidID.repository;


import com.unyime.solidID.TestDataUtility;
import com.unyime.solidID.domain.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserEntityIntegrationIntegrationTest {

    private final UserRepository underTest;

    @Autowired
    public UserEntityIntegrationIntegrationTest(UserRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatUserCanBeCreatedAndRecalled(){
        UserEntity user = TestDataUtility.createTestUser();
        underTest.save(user);

        Optional<UserEntity> savedUser = underTest.findByEmail(user.getEmail());
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get()).isEqualTo(user);
    }

}
