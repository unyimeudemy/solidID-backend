package com.unyime.solidID.services;

import com.unyime.solidID.domain.entities.StaffMemberEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface StaffMemberService {

    Optional<StaffMemberEntity> addMember(String reqHeader, StaffMemberEntity staffMemberEntity);

    Optional<StaffMemberEntity>  getMember(String staffEmail);
}
