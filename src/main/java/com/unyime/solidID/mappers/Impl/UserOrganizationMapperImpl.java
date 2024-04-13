package com.unyime.solidID.mappers.Impl;

import com.unyime.solidID.domain.dto.UserOrganizationDto;
import com.unyime.solidID.domain.entities.UserOrganizationEntity;
import com.unyime.solidID.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class UserOrganizationMapperImpl implements Mapper<UserOrganizationEntity, UserOrganizationDto> {

    private final ModelMapper modelMapper;

    public UserOrganizationMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public UserOrganizationDto mapTo(UserOrganizationEntity userOrganizationEntity) {
        return modelMapper.map(userOrganizationEntity, UserOrganizationDto.class);
    }

    @Override
    public UserOrganizationEntity mapFrom(UserOrganizationDto userOrganizationDto) {
        return modelMapper.map(userOrganizationDto, UserOrganizationEntity.class);
    }
}
