package com.stepup.mapper;

import com.stepup.dtos.requests.ProductDTO;
import com.stepup.dtos.responses.ColorResponse;
import com.stepup.dtos.responses.ProductCardResponse;
import com.stepup.dtos.responses.ProductResponse;
import com.stepup.entity.Color;
import com.stepup.entity.Product;
import com.stepup.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface IProductMapper {
    @Mapping(target = "sizes", ignore = true)
    @Mapping(target = "colors", ignore = true)
    Product toProduct(ProductDTO productDTO);

    @Mapping(target = "imageUrl", expression = "java(getFirstColorImageUrl(product))")
    @Mapping(target = "price", expression = "java(getFirstVariantPrice(product))")
    @Mapping(target = "promotionPrice", expression = "java(getFirstVariantPromotionPrice(product))")
    @Mapping(target = "colors", expression = "java(getColors1(product))")
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

    default Double getFirstVariantPrice(Product product) {
        if (product.getProductVariants() != null && !product.getProductVariants().isEmpty()) {
            return product.getProductVariants().get(0).getPrice();
        }
        return null;
    }

    default Double getFirstVariantPromotionPrice(Product product) {
        if (product.getProductVariants() != null && !product.getProductVariants().isEmpty()) {
            return product.getProductVariants().get(0).getPromotionPrice();
        }
        return null;
    }
    default List<Color> getColors(Product product) {
        if (product.getColors() != null && !product.getColors().isEmpty()) {

        }
        return null;
    }
    default List<ColorResponse> getColors1(Product product) {
        if (product.getColors() != null && !product.getColors().isEmpty()) {
            return product.getColors().stream()
                    .map(color -> new ColorResponse(color.getId(), color.getName()))
                    .collect(Collectors.toList());
        }
        return null;
    }

}
