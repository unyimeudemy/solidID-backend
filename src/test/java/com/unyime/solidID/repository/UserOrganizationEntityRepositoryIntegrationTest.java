package com.unyime.solidID.repository;


import com.unyime.solidID.TestDataUtility;
import com.unyime.solidID.domain.dto.UserOrganizationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserOrganizationEntityRepositoryIntegrationTest {

    private final UserOrganizationRepository underTest;

    public UserOrganizationEntityRepositoryIntegrationTest(UserOrganizationRepository underTest) {
        this.underTest = underTest;
    }


//    @Test
//    public void TestThatFindByStaffEmailReturnsAListOfOrg(){
//        UserOrganizationDto userOrganizationEntity = TestDataUtility.createTestUserOrgEntity();
////        underTest.save(userOrganizationEntity)
//
//    }
}
