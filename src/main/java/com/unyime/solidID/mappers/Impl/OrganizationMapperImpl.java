package com.unyime.solidID.mappers.Impl;

import com.unyime.solidID.domain.dto.OrganizationDto;
import com.unyime.solidID.domain.entities.OrganizationEntity;
import com.unyime.solidID.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapperImpl implements Mapper<OrganizationEntity, OrganizationDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public OrganizationMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public OrganizationDto mapTo(OrganizationEntity organizationEntity) {
        return modelMapper.map(organizationEntity, OrganizationDto.class);
    }

    @Override
    public OrganizationEntity mapFrom(OrganizationDto organizationDto) {
        return modelMapper.map(organizationDto, OrganizationEntity.class);
    }
}
