package com.unyime.solidID.controllers;

import com.unyime.solidID.domain.AuthenticationResponse;
import com.unyime.solidID.domain.dto.IdentityUsageRecordDto;
import com.unyime.solidID.domain.dto.UserDto;
import com.unyime.solidID.domain.dto.UserOrganizationDto;
import com.unyime.solidID.domain.entities.IdentityUsageRecordEntity;
import com.unyime.solidID.domain.entities.UserEntity;
import com.unyime.solidID.domain.entities.UserOrganizationEntity;
import com.unyime.solidID.mappers.Mapper;
import com.unyime.solidID.services.UserService;
import com.unyime.solidID.services.impl.JwtServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    private final Mapper<UserOrganizationEntity, UserOrganizationDto> userOrganizationMapper;

    private final Mapper<UserEntity, UserDto> usermapper;

    private final Mapper<IdentityUsageRecordEntity, IdentityUsageRecordDto> identityUsageRecordMapper;

    private final JwtServiceImpl jwtServiceImpl;

    public UserController(UserService userService, Mapper<UserOrganizationEntity, UserOrganizationDto> userOrganizationMapper, Mapper<UserEntity, UserDto> usermapper, Mapper<IdentityUsageRecordEntity, IdentityUsageRecordDto> identityUsageRecordMapper, JwtServiceImpl jwtServiceImpl) {
        this.userService = userService;
        this.userOrganizationMapper = userOrganizationMapper;
        this.usermapper = usermapper;
        this.identityUsageRecordMapper = identityUsageRecordMapper;
        this.jwtServiceImpl = jwtServiceImpl;
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

    @PostMapping(path = "/add-org")
    public ResponseEntity<UserOrganizationDto> addOrganization(
            @RequestBody UserOrganizationDto userOrganizationDto
    ){
        UserOrganizationEntity organizationEntity = userOrganizationMapper
                .mapFrom(userOrganizationDto);

        // ---------VERIFIES IF USER HAS ALREADY REGISTERED ORGANIZATION ON USER ORGANIZATON ENTITY-----------

//        Optional<UserOrganizationEntity> org = userService.getOrganization(organizationEntity.getOrgEmail());
//        if(org.isPresent() && org.get().equals(organizationEntity)){
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }

        Optional<UserOrganizationEntity> savedUserOrganizationEntity =
                userService.addOrganization(organizationEntity);

        UserOrganizationDto savedUserOrg = userOrganizationMapper
                .mapTo(savedUserOrganizationEntity.get());

        return new ResponseEntity<>(savedUserOrg, HttpStatus.OK);
    }

    @GetMapping(path = "/users-orgs")
    public List<UserOrganizationDto> getAllUserOrgs(@NonNull HttpServletRequest request){
        String userEmail = getUserEmail(request);
        List<UserOrganizationEntity> userOrgsList = userService.getAllUserOrg(userEmail);
        return userOrgsList.stream().map(userOrganizationMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/history")
    public List<IdentityUsageRecordDto> getIdentityUsageRecord(@NonNull HttpServletRequest request){
        String userEmail = getUserEmail(request);
        List<IdentityUsageRecordEntity> record = userService.getIdentityUsageRecord(userEmail);
        return record.stream().map(identityUsageRecordMapper::mapTo).collect(Collectors.toList());
    }

    private String getUserEmail(HttpServletRequest request){
        String userEmail = "";
        String jwt = "";
        String reqHeader = request.getHeader("Authorization");
        if (reqHeader != null && reqHeader.startsWith("Bearer ")){
            jwt = reqHeader.substring(7);
            userEmail = jwtServiceImpl.extractUsername(jwt);
        }
        return userEmail;
    }

}
