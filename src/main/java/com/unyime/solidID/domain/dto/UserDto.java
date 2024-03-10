package com.unyime.solidID.domain.dto;

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

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private String otherName;

    private Integer age;

    private String Nationality;

    private String StateOfOrigin;

    private String image;

    private Role role;
}
