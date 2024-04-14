package com.unyime.solidID.controllers;


import com.unyime.solidID.domain.TokenGenerationReqBody;
import com.unyime.solidID.domain.VerificationResponse;
import com.unyime.solidID.domain.dto.ErrorResponseDto;
import com.unyime.solidID.domain.dto.UserDto;
import com.unyime.solidID.domain.dto.VerificationDto;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.mappers.Mapper;
import com.unyime.solidID.services.IdentityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/identity")
public class IdentityController {

    private final IdentityService identityService;

//    private final Mapper<UserEntity, UserDto> userMapper;


    public IdentityController(IdentityService identityService, Mapper<UserEntity, UserDto> userMapper) {
        this.identityService = identityService;
//        this.userMapper = userMapper;
    }


    @PostMapping("/generate")
    public ResponseEntity<String> generateURL(
            Authentication authentication ,
            @RequestBody TokenGenerationReqBody tokenGenerationReqBody
            ){
        String orgEmail = tokenGenerationReqBody.getOrgEmail();
        String currentUserEmail = authentication.getName();
        String verificationToken = identityService.generate(currentUserEmail, orgEmail);
        return new ResponseEntity<>(verificationToken, HttpStatus.OK);
    }

    @PostMapping(path = "/verify")
    public ResponseEntity<?> verify(
            Authentication authentication ,
            @RequestBody VerificationDto verificationDto
    ){
        String currentUserEmail = authentication.getName();
        Optional<VerificationResponse> verifiedUser = identityService.verify(currentUserEmail, verificationDto.getKey());
//        return new ResponseEntity<>(userMapper.mapTo(userEntity.get()), HttpStatus.FOUND);

        if(verifiedUser.isPresent()){
            return new ResponseEntity<>(verifiedUser.get(), HttpStatus.OK);
        }else{
            ErrorResponseDto error = ErrorResponseDto.builder()
                    .errorMessage("Verification failed")
                    .build();
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
}
