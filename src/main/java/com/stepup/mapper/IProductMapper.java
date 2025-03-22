package com.stepup.mapper;

import com.stepup.dtos.requests.ProductDTO;
import com.stepup.dtos.responses.ProductCardResponse;
import com.stepup.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IProductMapper {
    Product toProduct(ProductDTO productDTO);
    List<ProductCardResponse> toProductCard(List<Product> products);
}
