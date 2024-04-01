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
public class OrganizationDto {


    private Integer id;

    private String email;

    private String password;

    private String organizationName;

    private String logo;

    private String repEmail;

    private String repPassword;

    private Role role;
}
