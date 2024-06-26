package com.unyime.solidID.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserOrganizationDto {

    private Integer id;

    private String staffId;

    private String orgEmail;

    private String orgName;

    private String staffName;

    private String staffEmail;

    private String staffRole;

    private String profileImage;
}
