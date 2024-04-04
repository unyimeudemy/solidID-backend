package com.unyime.solidID.mappers.Impl;

import com.unyime.solidID.domain.dto.IdentityUsageRecordDto;
import com.unyime.solidID.domain.entities.IdentityUsageRecordEntity;
import com.unyime.solidID.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class IdentityUsageRecordMapperImpl implements Mapper<IdentityUsageRecordEntity, IdentityUsageRecordDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public IdentityUsageRecordMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public IdentityUsageRecordDto mapTo(IdentityUsageRecordEntity identityUsageRecordEntity) {
        return modelMapper.map(identityUsageRecordEntity, IdentityUsageRecordDto.class);
    }

    @Override
    public IdentityUsageRecordEntity mapFrom(IdentityUsageRecordDto identityUsageRecordDto) {
        return modelMapper.map(identityUsageRecordDto, IdentityUsageRecordEntity.class);
    }
}
