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
    @SequenceGenerator(name = "userOrganization_id_seq", sequenceName = "userOrganization_id_seq", allocationSize = 1)
    private Integer id;

    private String staffId;

    private String orgEmail;

    private String orgName;

    private String staffName;

    private String staffEmail;

    private String staffRole;

    private String profileImage;


}
