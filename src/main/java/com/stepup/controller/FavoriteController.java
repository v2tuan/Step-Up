package com.stepup.controller;
import com.stepup.dtos.requests.FavoriteItemDTO;
import com.stepup.dtos.responses.FavoriteItemRespone;
import com.stepup.entity.FavoriteItem;
import com.stepup.entity.User;
import com.stepup.mapper.IFavoriteMapper;
import com.stepup.service.impl.FavoriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteServiceImpl favoriteService;

    @Autowired
    private IFavoriteMapper favoriteMapper;

    @GetMapping
    public List<FavoriteItemRespone> getAllFavoriteItems() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            User user = (User) principal;
            List<FavoriteItem> favoriteItems = favoriteService.getFavoriteItemByUser(user);
            return favoriteMapper.toFavoriteItemRespone(favoriteItems);
        }else
        {
            throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống");
        }

    }
    @PostMapping("/add")
    public String addFavoriteItem(@RequestBody FavoriteItemDTO favoriteItemDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            User user = (User) principal;
            return favoriteService.addtoFavorite(user.getId(),favoriteItemDTO);
        }
        else
        {
            throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống");
        }
    }

    @DeleteMapping("/remove/{id}")
    public String removeFavoriteItem(@PathVariable Long favoriteItemId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            User user = (User) principal;
            return favoriteService.removefromFavorite(user.getId(),favoriteItemId);
        }
        else{
            throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống");
        }
    }
}
