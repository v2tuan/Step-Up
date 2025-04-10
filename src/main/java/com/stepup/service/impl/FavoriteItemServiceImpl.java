package com.stepup.service.impl;

import com.stepup.entity.FavoriteItem;
import com.stepup.service.IFavoriteItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteItemServiceImpl implements IFavoriteItemService {


    @Override
    public List<FavoriteItem> getFavoriteItemsByFID(Long favoriteID) {
        return List.of();
    }

    @Override
    public FavoriteItem saveFavoriteItem(FavoriteItem favoriteItem) {
        return null;
    }

    @Override
    public void deleteFavoriteItem(Long favoriteID) {

    }

    @Override
    public Integer getFavoriteItemCount() {
        return 0;
    }
}
