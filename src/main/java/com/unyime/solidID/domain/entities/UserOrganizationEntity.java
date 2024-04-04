package com.unyime.solidID.domain.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "userOrganization")
public class UserOrganizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userOrganization_id_seq")
    private Integer id;

    private String staffId;

    private String orgEmail;

    private String orgName;

    private String staffName;

    private String staffEmail;

    private String staffRole;
}
