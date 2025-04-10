package com.stepup.service.impl;

import com.stepup.dtos.requests.FavoriteItemDTO;
import com.stepup.entity.Favorite;
import com.stepup.entity.FavoriteItem;
import com.stepup.entity.ProductVariant;
import com.stepup.entity.User;
import com.stepup.repository.FavoriteItemRepository;
import com.stepup.repository.FavoriteRepository;
import com.stepup.repository.ProductVariantRepository;
import com.stepup.repository.UserRepository;
import com.stepup.service.IFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteServiceImpl implements IFavoriteService {
    @Autowired
    private FavoriteRepository favRepo;
    @Autowired
    private FavoriteItemRepository favItemRepo;
    @Autowired
    private ProductVariantRepository productVariantRepo;
    @Autowired
    private UserRepository userRepo;

    @Override
    public Optional<Favorite> getFavorite(int id) {
        return Optional.empty();
    }

    @Override
    public void deleteFavorite(Long id) {

    }

    @Override
    public Favorite saveFavorite(Favorite favorite) {
        return null;
    }

    @Override
    public Optional<Favorite> getFavoriteByUserId(Long userId) {
        return Optional.empty();
    }

    @Override
    public List<FavoriteItem> getFavoriteItemByUser(User user) {
        Favorite favorite = favRepo.findByUserId(user.getId()).orElseGet(() ->{
            Favorite fav = new Favorite();
            fav.setUser(user);
            return favRepo.save(fav);
        });
        return favItemRepo.findByFavoriteId(favorite.getId());
    }

    @Override
    public String addtoFavorite(Long UserId, FavoriteItemDTO favoriteDTO) {
        Favorite favorite = favRepo.findByUserId(UserId).orElseGet(() -> {
            Favorite fav = new Favorite();
            fav.setUser(userRepo.findById(UserId).get());
            return favRepo.save(fav);
        });
        Optional<ProductVariant> product = productVariantRepo.findById(favoriteDTO.getProductVariantId());
        if(product.isEmpty()){
            return "Sản phẩm không tồn tại hoặc đã bị xóa";
        }

        FavoriteItem favoriteItem = new FavoriteItem();
        favoriteItem.setFavorite(favorite);
        favoriteItem.setProductVariant(productVariantRepo.findById(favoriteDTO.getProductVariantId()).get());
        favoriteItem.setCreatedAt(LocalDateTime.now());
        favItemRepo.save(favoriteItem);
        return "Đã thêm sản phẩm vào danh mục yêu thích";
    }

    @Override
    public String removefromFavorite(Long UserId, Long FavoriteItemId) {
        Favorite favorite = favRepo.findByUserId(UserId).orElseGet(()->{
            Favorite fav = new Favorite();
            fav.setUser(userRepo.findById(UserId).get());
            return favRepo.save(fav);
        });

        FavoriteItem favoriteItem = favItemRepo.findById(FavoriteItemId).orElse(null);
        if(favoriteItem == null){
            return "Sản phẩm đã bị xóa hoặc không tồn tại ";
        }
        favItemRepo.deleteById(FavoriteItemId);
        return "Sản phẩm đã bị xóa thành công";
    }


}
