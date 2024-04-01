package com.unyime.solidID.mappers.Impl;

import com.unyime.solidID.domain.dto.StaffMemberDto;
import com.unyime.solidID.domain.entities.StaffMemberEntity;
import com.unyime.solidID.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StaffMemberMapperImpl implements Mapper<StaffMemberEntity, StaffMemberDto> {

    @Autowired
    private final ModelMapper modelMapper;

    public StaffMemberMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public StaffMemberDto mapTo(StaffMemberEntity staffMemberEntity) {
        return modelMapper.map(staffMemberEntity, StaffMemberDto.class);
    }

    @Override
    public StaffMemberEntity mapFrom(StaffMemberDto staffMemberDto) {
        return modelMapper.map(staffMemberDto, StaffMemberEntity.class);
    }
}
