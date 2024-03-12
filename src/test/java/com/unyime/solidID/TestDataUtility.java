package com.unyime.solidID;

import com.unyime.solidID.domain.dto.UserDto;
import com.unyime.solidID.domain.entities.Role;
import com.unyime.solidID.domain.entities.UserEntity;

public final class TestDataUtility {

    private TestDataUtility(){}

    public static UserEntity createTestUser(){
        return UserEntity.builder()
                .firstName("unyime")
                .lastName("Udoh")
                .otherName("Emmanuel")
                .age(18)
                .email("unyime@gmail.com")
                .password("123456")
                .nationality("Nigerian")
                .stateOfOrigin("Akwa Ibom")
                .role(Role.USER)
                .image("image_url")
                .build();
    }

    public static UserEntity createTestUser1(){
        return UserEntity.builder()
                .firstName("unyime")
                .lastName("Udoh")
                .otherName("Emmanuel")
                .age(18)
                .email("unyime10@gmail.com")
                .password("123456")
                .nationality("Nigerian")
                .stateOfOrigin("Akwa Ibom")
                .role(Role.USER)
                .image("image_url")
                .build();
    }

    public static UserDto createTestUserDto() {
        return UserDto.builder()
                .firstName("unyime")
                .lastName("Udoh")
                .otherName("Emmanuel")
                .age(18)
                .email("unyime@gmail.com")
                .password("123456")
                .nationality("Nigerian")
                .stateOfOrigin("Akwa Ibom")
                .role(Role.USER)
                .image("image_url")
                .build();
    }
}
