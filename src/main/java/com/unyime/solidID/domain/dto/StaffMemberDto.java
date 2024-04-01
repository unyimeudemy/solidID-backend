package com.unyime.solidID.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffMemberDto {

    private Integer id;

    private String staffName;

    private String staffEmail;

    private String staffRole;

    private String staffID;

    private String orgName;

    private String orgEmail;
}
