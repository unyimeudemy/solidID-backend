package com.unyime.solidID.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenGenerationReqBody {

//    @Builder.Default
    private String orgEmail;
}
