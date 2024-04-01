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
@Table(name = "userOrganization")public class UserOrganizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userOrganization_id_seq")
    private Integer id;

    private String staffId;

    private String orgEmail;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "staffMember_id")
    private StaffMemberEntity staffMemberEntity;
    
}
