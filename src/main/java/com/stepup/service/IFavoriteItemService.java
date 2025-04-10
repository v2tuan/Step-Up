package com.stepup.service;

import com.stepup.entity.FavoriteItem;

import java.util.List;

public interface IFavoriteItemService {
    List<FavoriteItem> getFavoriteItemsByFID(Long favoriteID);
    FavoriteItem saveFavoriteItem(FavoriteItem favoriteItem);
    void deleteFavoriteItem(Long favoriteID);
    Integer getFavoriteItemCount();


}
