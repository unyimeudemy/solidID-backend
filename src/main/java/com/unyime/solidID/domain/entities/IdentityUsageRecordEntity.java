package com.unyime.solidID.domain.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "identityUsageRecord")
public class IdentityUsageRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "identityUsageRecord_id_seq")
    private Integer id;

    private String currentUserEmail;

    private String userVerifiedEmail;

    private LocalDateTime date;
}
