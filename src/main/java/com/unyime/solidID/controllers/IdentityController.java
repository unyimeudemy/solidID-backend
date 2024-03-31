package com.unyime.solidID.controllers;


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

    private final Mapper<UserEntity, UserDto> userMapper;


    public IdentityController(IdentityService identityService, Mapper<UserEntity, UserDto> userMapper) {
        this.identityService = identityService;
        this.userMapper = userMapper;
    }


    @GetMapping(path = "/generate")
    public ResponseEntity<String> generateURL(Authentication authentication){
        String currentUserEmail = authentication.getName();
        String generatedURL = identityService.generate(currentUserEmail);
        return new ResponseEntity<>(generatedURL, HttpStatus.OK);
    }

    @PostMapping(path = "/verify")
    public ResponseEntity<UserDto> verify(@RequestBody VerificationDto verificationDto){
        Optional<UserEntity> userEntity = identityService.verify(verificationDto.getKey());
//        return new ResponseEntity<>(userMapper.mapTo(userEntity.get()), HttpStatus.FOUND);

        if(userEntity.isPresent()){
            UserDto currentUserDto = userMapper.mapTo(userEntity.get());
            return ResponseEntity.ok(currentUserDto);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
