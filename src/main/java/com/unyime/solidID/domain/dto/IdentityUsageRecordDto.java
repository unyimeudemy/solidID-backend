package com.unyime.solidID.domain.dto;


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
public class IdentityUsageRecordDto {

    private String currentUserEmail;

    private String userVerifiedEmail;

    private LocalDateTime date;
}
