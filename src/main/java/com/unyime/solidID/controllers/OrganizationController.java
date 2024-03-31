package com.unyime.solidID.controllers;


import com.unyime.solidID.domain.AuthenticationResponse;
import com.unyime.solidID.domain.dto.OrganizationDto;
import com.unyime.solidID.domain.entities.OrganizationEntity;
import com.unyime.solidID.mappers.Mapper;
import com.unyime.solidID.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/org")
public class OrganizationController {

    private final Mapper<OrganizationEntity, OrganizationDto> orgMapper;

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(Mapper<OrganizationEntity, OrganizationDto> orgMapper, OrganizationService organizationService) {
        this.orgMapper = orgMapper;
        this.organizationService = organizationService;
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<AuthenticationResponse> signUp(@RequestBody OrganizationDto organizationDto){
        OrganizationEntity organizationEntity = orgMapper.mapFrom(organizationDto);

        AuthenticationResponse signedUpOrg = organizationService.signUp(organizationEntity);
        if(signedUpOrg.getToken().equals("Email already exist")){
            return new ResponseEntity<>(signedUpOrg, HttpStatus.CONFLICT);
        }else {
            return new ResponseEntity<>(signedUpOrg, HttpStatus.CREATED);
        }

    }
}
