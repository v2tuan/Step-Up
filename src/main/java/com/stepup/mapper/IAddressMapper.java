package com.stepup.mapper;

import com.stepup.dtos.responses.AddressResponse;
import com.stepup.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IAddressMapper {
    AddressResponse toAddressResponse(Address address);
}
