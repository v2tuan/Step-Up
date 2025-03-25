package com.stepup.mapper;

import com.stepup.dtos.requests.ProductDTO;
import com.stepup.dtos.responses.ProductCardResponse;
import com.stepup.dtos.responses.ProductResponse;
import com.stepup.entity.Color;
import com.stepup.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IProductMapper {
    @Mapping(target = "sizes", ignore = true)
    @Mapping(target = "colors", ignore = true)
    Product toProduct(ProductDTO productDTO);

    @Mapping(target = "imageUrl", expression = "java(getFirstColorImageUrl(product))")
    ProductCardResponse toProductCardResponse(Product product);
    List<ProductCardResponse> toProductCard(List<Product> products);

    ProductResponse toProductResponse(Product product);

    // Custom method để lấy ảnh đầu tiên
    default String getFirstColorImageUrl(Product product) {
        if (product.getColors() != null && !product.getColors().isEmpty()) {
            Color firstColor = product.getColors().get(0);
            if (firstColor.getColorImages() != null && !firstColor.getColorImages().isEmpty()) {
                return firstColor.getColorImages().get(0).getImageUrl();
            }
        }
        return null;
    }
}
