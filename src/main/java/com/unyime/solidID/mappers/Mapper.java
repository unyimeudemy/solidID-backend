package com.unyime.solidID.mappers;

public interface Mapper <Entities, DTOs>{

    DTOs mapTo(Entities entities);

    Entities mapFrom(DTOs DTOs);
}
