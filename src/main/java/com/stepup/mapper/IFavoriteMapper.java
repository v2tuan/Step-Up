package com.stepup.mapper;

import com.stepup.dtos.responses.CartItemRespone;
import com.stepup.dtos.responses.FavoriteItemRespone;
import com.stepup.entity.CartItem;
import com.stepup.entity.FavoriteItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;

@Mapper (componentModel = "spring")
public interface IFavoriteMapper {

    @Mapping(target = "title", expression = "java(getTitle(favoriteItem))")
    FavoriteItemRespone toFavoriteItemRespone(FavoriteItem favoriteItem); // ánh xạ từng CartItem

    List<FavoriteItemRespone> toFavoriteItemRespone(List<FavoriteItem> favoriteItems);

    default String getTitle(FavoriteItem favoriteItem) {
        return favoriteItem.getProductVariant().getProduct().getName();
    }
}
