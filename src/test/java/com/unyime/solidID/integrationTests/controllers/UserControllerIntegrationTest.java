package com.unyime.solidID.integrationTests.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.unyime.solidID.TestDataUtility;
import com.unyime.solidID.domain.VerificationResponse;
import com.unyime.solidID.domain.dto.UserDto;
import com.unyime.solidID.domain.entities.OrganizationEntity;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.domain.entities.UserOrganizationEntity;
import com.unyime.solidID.services.IdentityService;
import com.unyime.solidID.services.OrganizationService;
import com.unyime.solidID.services.impl.JwtServiceImpl;
import com.unyime.solidID.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    private final ObjectMapper objectMapper;

    private final MockMvc mockMvc;

    private final UserService userService;

    private final OrganizationService organizationService;

    private final IdentityService identityService;


    @Autowired
    public UserControllerIntegrationTest(ObjectMapper objectMapper, MockMvc mockMvc, UserService userService, JwtServiceImpl jwtServiceImpl, OrganizationService organizationService, IdentityService identityService) {
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.organizationService = organizationService;
        this.identityService = identityService;
    }


    @Test
    public void testThatNewUserCanBeSavedAndJwtTokenReturnedWithHttpStatusCode201() throws Exception {
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        //should_returnHttp201AndJwtToken_when_saveNewUserIsCalled

        String userJson = objectMapper.writeValueAsString(userEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/user/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.token").exists()
        );
    }

    @Test
    public void testThatNewUserIsSavedAfterRegistration() throws Exception{
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        String userEntityJson = objectMapper.writeValueAsString(userEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/user/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userEntityJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

        Optional<UserEntity> savedUser = userService.getProfile(userEntity.getEmail());
            assertThat(savedUser).isPresent();
            assertThat(savedUser.get().getEmail()).isEqualTo(userEntity.getEmail());
            assertThat(savedUser.get().getFirstName()).isEqualTo(userEntity.getFirstName());
    }

    @Test
    public void testThatAlreadyExistingUserCanSignInAndJwtTokenIsReturnedWithHttp201() throws Exception{
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        userService.signUp(userEntity);

        UserDto userDto = TestDataUtility.createTestUserDto();
        String userDtoJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/user/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.token").exists()
        );
    }


    @Test
    @WithMockUser(username = "unyime@gmail.com", password = "123456", roles = "USER")
    public void testThatUserProfileCanBeAccessWithHttp201() throws Exception{
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        userService.signUp(userEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/user/profile")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(userEntity.getEmail())
        );
    }

    @Test
    @WithMockUser(username = "unyime@gmail.com", password = "123456", roles = "USER")
    public void testThatUserCanAddOrgWithHttp201() throws Exception{
        UserEntity userEntity = TestDataUtility.createTestUserEntity();
        userService.signUp(userEntity);

        OrganizationEntity organizationEntity = TestDataUtility.createTestOrgEntity();
        organizationService.signUp(organizationEntity);

        UserOrganizationEntity userOrganizationEntity = TestDataUtility.createTestUserOrgEntity();
        String userOrgEntityJson = objectMapper.writeValueAsString(userOrganizationEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/user/add-org")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userOrgEntityJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.staffEmail").value(userOrganizationEntity.getStaffEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.orgEmail").value(userOrganizationEntity.getOrgEmail())
        );
    }

}




