package com.stepup.mapper;

import com.stepup.dtos.requests.ProductDTO;
import com.stepup.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IProductMapper {
    Product toProduct(ProductDTO productDTO);
}
