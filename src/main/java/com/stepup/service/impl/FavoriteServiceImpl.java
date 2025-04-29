package com.stepup.service.impl;

import com.stepup.dtos.requests.FavoriteDTO;
import com.stepup.entity.*;
import com.stepup.repository.ColorRepository;
import com.stepup.repository.FavoriteRepository;
import com.stepup.repository.ProductVariantRepository;
import com.stepup.repository.UserRepository;
import com.stepup.service.IFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteServiceImpl implements IFavoriteService {
    @Autowired
    private FavoriteRepository favRepo;
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ColorRepository colorRepo;
    @Autowired
    private ProductVariantRepository productVariantRepository;


    @Override
    public void deleteFavorite(Long id) {
       favRepo.deleteById(id);
    }

    @Override
    public Favorite saveFavorite(Favorite favorite) {
        return favRepo.save(favorite);
    }

    @Override
    public List<Favorite> getFavoriteByUserId(Long userId) {
        List<Favorite> favorites = new ArrayList<>();
        favorites = favRepo.findByUserId(userId);
        return favorites;
    }

    @Override
    public String addtoFavorite(User user, FavoriteDTO favoriteDTO) {
        Favorite favorite =  new Favorite();
        favorite.setUser(user);
        favorite.setColor(colorRepo.getReferenceById(favoriteDTO.getColorId()));
        favorite.setPrice(favoriteDTO.getPrice());
        favRepo.save(favorite);
        return "Add Favorite Successfully!";
    }

    @Override
    public String removefromFavorite(Long FavoriteId) {
        Favorite favoriteItem = favRepo.findById(FavoriteId).orElse(null);
        if(favoriteItem == null){
            return "Product is Unavailable! ";
        }
        favRepo.deleteById(FavoriteId);
        return "Remove Favorite Successfully!";
    }
    @Override
    public String removefromFavoriteByColorId(User user, long colorId) {
        Color color = colorRepo.getReferenceById(colorId);
        Favorite favoriteItem = favRepo.findByUserAndColor(user,color).orElse(null);
        if(favoriteItem == null){
            return "Product is Unavailable! ";
        }
        favRepo.deleteById(favoriteItem.getId());
        return "Remove Favorite Successfully!";
    }

    @Override
    public List<ProductVariant> getProducVarientByColorId(Long colorId) {
        return productVariantRepository.findByColor_Id(colorId);
    }

    @Override
    public Product getProductByColorId(Long colorId) {
        List<ProductVariant> productVariant = productVariantRepository.findByColor_Id(colorId);
        if(productVariant == null){
            return null;
        }
        else {
            Product product = productVariant.getFirst().getProduct();
            return product;
        }
    }

    @Override
    public String deleteFavoriteByProduct(User user, List<Color> color) {
        for (Color c : color) {
            Optional<Favorite> favorite = favRepo.findByUserAndColor(user, c);
            favorite.ifPresent(favRepo::delete);
        }
        return "Removed from favorites successfully";
    }

    @Override
    public Boolean checkFavorite(User user, long ColorId) {
        Color c= colorRepo.getById(ColorId);
        Optional<Favorite> favorite = favRepo.findByUserAndColor(user, c);
        return favorite.isPresent();
    }


}
