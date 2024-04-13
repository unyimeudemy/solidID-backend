package com.unyime.solidID;

import com.unyime.solidID.domain.dto.UserDto;
import com.unyime.solidID.domain.dto.UserOrganizationDto;
import com.unyime.solidID.domain.entities.OrganizationEntity;
import com.unyime.solidID.domain.entities.Role;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.domain.entities.UserOrganizationEntity;

public final class TestDataUtility {

    private TestDataUtility(){}

    public static UserEntity createTestUserEntity(){
        return UserEntity.builder()
                .id(1)
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

    public static UserOrganizationEntity createTestUserOrgEntity(){
        return UserOrganizationEntity.builder()
                .orgName("org1")
                .orgEmail("org1@gmail.com")
                .staffName("unyime")
                .staffEmail("unyime@gmail.com")
                .staffRole("DSO")
                .build();

    }

    public static OrganizationEntity createTestOrgEntity() {
        return OrganizationEntity.builder()
                .id(1)
                .email("org1@gmail.com")
                .password("123456")
                .repEmail("unyime@gmail.com")
                .repPassword("123456")
                .organizationName("org1")
                .role(Role.ORG)
                .build();

    }
}
