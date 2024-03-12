package com.unyime.solidID.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.unyime.solidID.TestDataUtility;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.services.impl.JwtServiceImpl;
import com.unyime.solidID.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    private final ObjectMapper objectMapper;

    private final MockMvc mockMvc;

    private final UserService userService;

    private final JwtServiceImpl jwtServiceImpl;



    @Autowired
    public UserControllerIntegrationTest(ObjectMapper objectMapper, MockMvc mockMvc, UserService userService, JwtServiceImpl jwtServiceImpl) {
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.jwtServiceImpl = jwtServiceImpl;
    }


    @Test
    public void testThatSignupSuccessfullyReturnsHttp201() throws Exception {
        UserEntity testUserEntity = TestDataUtility.createTestUser();

        String userJson = objectMapper.writeValueAsString(testUserEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/user/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatSignInSuccessfullyReturnsToken() throws Exception {
        UserEntity userEntity = TestDataUtility.createTestUser1();

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
    public void testThatSignUpReturnsHttp409ConflictIfUserAlreadyExist() throws Exception {
        UserEntity testUserEntity = TestDataUtility.createTestUser();
        userService.signUp(testUserEntity);
        String testUserJson = objectMapper.writeValueAsString(testUserEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/user/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUserJson)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        );
    }

//    @Test
//    public void testThatGetProfileReturnsHttp200WithCurrentUser() throws Exception{
//
//        UserEntity testUserEntity = TestDataUtility.createTestUser();
//        userService.signUp(testUserEntity);
////        Optional<UserEntity> currentUserProfile = userService.getProfile( authentication);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.get("/api/v1/user/profile")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(
//                MockMvcResultMatchers.status().isFound()
//        );
//    }

}
