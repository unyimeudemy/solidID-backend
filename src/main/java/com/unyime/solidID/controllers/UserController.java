package com.unyime.solidID.controllers;

import com.unyime.solidID.domain.AuthenticationResponse;
import com.unyime.solidID.domain.dto.UserDto;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.mappers.Mapper;
import com.unyime.solidID.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public Mapper<UserEntity, UserDto> usermapper;

    public UserController(UserService userService, Mapper<UserEntity, com.unyime.solidID.domain.dto.UserDto> usermapper) {
        this.userService = userService;
        this.usermapper = usermapper;
    }

    @PostMapping(path = "/auth/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody UserDto userDto){
        UserEntity userEntity = usermapper.mapFrom(userDto);
        AuthenticationResponse signedUpUser = userService.signUp(userEntity);
        if(signedUpUser.getToken().equals("Email already exist")){
            return new ResponseEntity<>(signedUpUser, HttpStatus.CONFLICT);
        }else {
            return new ResponseEntity<>(signedUpUser, HttpStatus.CREATED);
        }
    }

    @PostMapping(path = "/auth/signin")
    public ResponseEntity<AuthenticationResponse> signin(@RequestBody UserDto userDto){
        UserEntity userEntity = usermapper.mapFrom(userDto);
        AuthenticationResponse signedInUser = userService.signIn(userEntity);
        return  new ResponseEntity<>(signedInUser, HttpStatus.OK);
    }

    @GetMapping(path = "/profile")
    public ResponseEntity<UserDto> getProfile(Authentication authentication){
        String currentUserEmail = authentication.getName();
        Optional<UserEntity> currentUser = userService.getProfile(currentUserEmail);
        if(currentUser.isPresent()){
            UserDto currentUserDto = usermapper.mapTo(currentUser.get());
            return ResponseEntity.ok(currentUserDto);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
