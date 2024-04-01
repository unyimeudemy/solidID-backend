package com.unyime.solidID.services.impl;

import com.unyime.solidID.domain.entities.StaffMemberEntity;
import com.unyime.solidID.repository.StaffMemberRepository;
import com.unyime.solidID.services.StaffMemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StaffMemberServiceImpl implements StaffMemberService {

    private final StaffMemberRepository staffMemberRepository;

    private final JwtServiceImpl jwtServiceImpl;

    public StaffMemberServiceImpl(StaffMemberRepository staffMemberRepository, JwtServiceImpl jwtServiceImpl) {
        this.staffMemberRepository = staffMemberRepository;
        this.jwtServiceImpl = jwtServiceImpl;
    }

    @Override
    public Optional<StaffMemberEntity> addMember(String reqHeader, StaffMemberEntity staffMemberEntity) {
        if(!verifyToken(reqHeader, staffMemberEntity.getOrgEmail())){
            return Optional.empty();
        }
        StaffMemberEntity savedStaffMemberEntity = staffMemberRepository.save(staffMemberEntity);
        return Optional.of(savedStaffMemberEntity);
    }

    @Override
    public Optional<StaffMemberEntity> getMember(String staffEmail) {
        return staffMemberRepository.findByStaffEmail(staffEmail);
    }

    private Boolean verifyToken(String reqHeader, String orgEmail){
        final String jwt;
        final String userEmail;

        if (reqHeader != null && reqHeader.startsWith("Bearer ")){
            jwt = reqHeader.substring(7);
            userEmail = jwtServiceImpl.extractUsername(jwt);
            return userEmail.equals(orgEmail);
        }
        return false;
    }

}
