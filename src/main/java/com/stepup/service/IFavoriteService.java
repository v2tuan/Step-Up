package com.stepup.service;

import com.stepup.dtos.requests.FavoriteDTO;

import com.stepup.entity.*;

import java.util.List;
import java.util.Optional;

public interface IFavoriteService {

    void deleteFavorite(Long id);
    Favorite saveFavorite(Favorite favorite);
    List<Favorite> getFavoriteByUserId(Long userId);;
    String addtoFavorite (User user, FavoriteDTO favoriteDTO);
    String removefromFavorite (Long FavoriteItemId);
    List<ProductVariant> getProducVarientByColorId(Long colorId);
    Product getProductByColorId(Long colorId);
    String deleteFavoriteByProduct(User user ,List<Color> color);
    Boolean checkFavorite(User user, long ColorId);
    String removefromFavoriteByColorId(User user, long color);

}
