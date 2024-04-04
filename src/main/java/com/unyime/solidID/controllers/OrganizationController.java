package com.unyime.solidID.controllers;


import com.unyime.solidID.domain.AuthenticationResponse;
import com.unyime.solidID.domain.dto.OrganizationDto;
import com.unyime.solidID.domain.dto.StaffMemberDto;
import com.unyime.solidID.domain.entities.OrganizationEntity;
import com.unyime.solidID.domain.entities.StaffMemberEntity;
import com.unyime.solidID.mappers.Mapper;
import com.unyime.solidID.services.OrganizationService;
import com.unyime.solidID.services.StaffMemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/org")
public class OrganizationController {

    private final Mapper<OrganizationEntity, OrganizationDto> orgMapper;
    private final Mapper<StaffMemberEntity, StaffMemberDto> staffMemberMapper;
    private final OrganizationService organizationService;

    private final StaffMemberService staffMemberService;

    @Autowired
    public OrganizationController(Mapper<OrganizationEntity, OrganizationDto> orgMapper, Mapper<StaffMemberEntity, StaffMemberDto> staffMemberMapper, OrganizationService organizationService, StaffMemberService staffMemberService) {
        this.orgMapper = orgMapper;
        this.staffMemberMapper = staffMemberMapper;
        this.organizationService = organizationService;
        this.staffMemberService = staffMemberService;
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

    @PostMapping("/auth/signin")
    public ResponseEntity<AuthenticationResponse> signin(@RequestBody OrganizationDto organizationDto){
        System.out.println("-------------------> Entered controller");
        OrganizationEntity organizationEntity = orgMapper.mapFrom(organizationDto);
        AuthenticationResponse signedInOrg = organizationService.signIn(organizationEntity);
        return new ResponseEntity<>(signedInOrg, HttpStatus.OK);
    }

    @PostMapping("/add-member")
    public ResponseEntity<StaffMemberDto> addMember(@NonNull HttpServletRequest request, @RequestBody StaffMemberDto staffMemberDto){
        String reqHeader = request.getHeader("Authorization");
        StaffMemberEntity staffMemberEntity = staffMemberMapper.mapFrom(staffMemberDto);
        if(staffMemberService.getMember(staffMemberEntity.getStaffEmail()).isPresent()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Optional<StaffMemberEntity> savedStaffMemberEntity = staffMemberService.addMember(reqHeader, staffMemberEntity);
        if(savedStaffMemberEntity.isPresent()){
            StaffMemberDto savedStaffDto = staffMemberMapper.mapTo(savedStaffMemberEntity.get());
            return new ResponseEntity<>(savedStaffDto, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping(path = "/all-members")
    public List<StaffMemberDto> getMembers(){
        List<StaffMemberEntity> staffMemberList = staffMemberService.getMembers();
        return staffMemberList.stream().map(staffMemberMapper::mapTo).collect(Collectors.toList());
    }
}
