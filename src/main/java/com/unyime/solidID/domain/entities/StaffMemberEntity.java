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
@Table(name = "StaffMember")
public class StaffMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "staffMember_id_seq")
    @SequenceGenerator(name = "staffMember_id_seq", sequenceName = "staffMember_id_seq", allocationSize = 1)
    private Integer id;

    private String staffName;

    private String staffEmail;

    private String staffRole;

    private String staffID;

    private String orgName;

    private String orgEmail;
}
