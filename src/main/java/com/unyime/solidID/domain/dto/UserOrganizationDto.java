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

    private String staffName;

    private String staffEmail;
}
