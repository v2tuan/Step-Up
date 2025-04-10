package com.stepup.service;

import com.stepup.dtos.requests.FavoriteItemDTO;
import com.stepup.entity.Favorite;
import com.stepup.entity.FavoriteItem;
import com.stepup.entity.User;

import java.util.List;
import java.util.Optional;

public interface IFavoriteService {

    Optional<Favorite> getFavorite(int id);
    void deleteFavorite(Long id);
    Favorite saveFavorite(Favorite favorite);
    Optional<Favorite> getFavoriteByUserId(Long userId);
    List<FavoriteItem> getFavoriteItemByUser(User user);
    String addtoFavorite (Long UserId, FavoriteItemDTO favoriteDTO);
    String removefromFavorite (Long UserId, Long FavoriteItemId);
}
