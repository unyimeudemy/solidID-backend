package com.unyime.solidID.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "identityUrls")
public class IdentityURLEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "identity_id_seq")
    @SequenceGenerator(name = "identity_id_seq", sequenceName = "identity_id_seq", allocationSize = 1)
    private Integer id;

    private String encodedEmail;

    private String key;

    private String orgEmail;

    private LocalDateTime date;

}
