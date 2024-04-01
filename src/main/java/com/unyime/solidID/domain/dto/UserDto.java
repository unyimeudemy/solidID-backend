package com.unyime.solidID.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unyime.solidID.domain.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Integer id;

//    @JsonIgnore
    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private String otherName;

    private Integer age;

    private String nationality;

    private String stateOfOrigin;

    private String image;

    private Role role;
}
