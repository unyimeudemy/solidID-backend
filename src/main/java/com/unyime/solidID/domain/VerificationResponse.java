package com.unyime.solidID.domain;

import com.unyime.solidID.domain.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationResponse {

    private String email;

    private String firstName;

    private String lastName;

    private String otherName;

    private String nationality;

    private String stateOfOrigin;

    private String image;

    private String staffId;

    private String orgName;

    private String staffRole;

}
