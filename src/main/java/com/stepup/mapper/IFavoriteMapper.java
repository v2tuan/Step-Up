package com.stepup.mapper;

import com.stepup.dtos.responses.FavoriteRespone;
import com.stepup.entity.Favorite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper (componentModel = "spring")
public interface IFavoriteMapper {
    @Mapping(target = "title", expression = "java(getTitle(favorite))")
    FavoriteRespone toFavoriteRespone(Favorite favorite); // ánh xạ từng CartItem

    List<FavoriteRespone> toFavoriteRespone(List<Favorite> favorites);

    default String getTitle(Favorite favorite) {
        return  favorite.getColor().getProduct().getName();
    }
}
