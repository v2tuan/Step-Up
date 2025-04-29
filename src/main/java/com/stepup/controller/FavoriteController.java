package com.stepup.controller;

import com.stepup.dtos.requests.FavoriteDTO;
import com.stepup.dtos.responses.FavoriteRespone;
import com.stepup.entity.*;
import com.stepup.mapper.IFavoriteMapper;
import com.stepup.repository.ProductRepository;
import com.stepup.repository.ProductVariantRepository;
import com.stepup.service.impl.FavoriteServiceImpl;
import com.stepup.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private ProductServiceImpl productService;



    @GetMapping
    public List<FavoriteRespone> getAllFavoriteItems() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            User user = (User) principal;
            List<Favorite> favorites = favoriteService.getFavoriteByUserId(user.getId());
            return favoriteMapper.toFavoriteRespone(favorites);
        }else
        {
            throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống");
        }

    }
    @PostMapping("/add")
    public String addFavoriteItem(@RequestBody FavoriteDTO favoriteDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            User user = (User) principal;
            return favoriteService.addtoFavorite(user,favoriteDTO);
        }
        else
        {
            throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống");
        }
    }

    @PostMapping("/add1")
    public String addFavoriteItem(@RequestParam long ProductId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            User user = (User) principal;
            Product product = productService.getProductById(ProductId).orElseThrow(() -> new RuntimeException("Product not found"));
            if (product.getColors() == null || product.getColors().isEmpty()) {
                throw new RuntimeException("No colors found for this product");
            }
            FavoriteDTO favoriteDTO = new FavoriteDTO();
            favoriteDTO.setColorId(product.getColors().get(0).getId());
            favoriteDTO.setPrice(product.getProductVariants().getFirst().getPrice());
            return favoriteService.addtoFavorite(user, favoriteDTO);
        }
        else
        {
            throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống");
        }
    }

    @DeleteMapping("/remove/{id}")
    public String removeFavoriteItem(@PathVariable("id") long favoriteItemId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            User user = (User) principal;
            return favoriteService.removefromFavorite(favoriteItemId);
        }
        else{
            throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống");
        }
    }

    @DeleteMapping("/remove1")
    public String removeFavoriteItemByColorID(@RequestParam long colorId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            User user = (User) principal;
            return favoriteService.removefromFavoriteByColorId(user, colorId);
        }
        else{
            throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống");
        }
    }

    @DeleteMapping("/remove")
    public String removeFavoriteItem1(@RequestParam long ProductId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            User user = (User) principal;
            Product product = productService.getProductById(ProductId).orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getColors() == null || product.getColors().isEmpty()) {
                throw new RuntimeException("No colors found for this product");
            }
            List<Color> colors = product.getColors();
            return favoriteService.deleteFavoriteByProduct(user , colors);
        }
        else{
            throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống");
        }
    }

    @PostMapping("/addAlltoCart")
    public String addFavoriteItemToCart(@RequestBody List<FavoriteDTO> favoriteDTO) {
        Object  principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            User user = (User) principal;
         //   return favoriteService.
            return " Add All to Cart Succesfully!";
        }
        else
        {
            throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống ");
        }

    }

    @GetMapping("/productVarient/{id}")
    public List<ProductVariant> getProductVarientBycolorId(@PathVariable Long id) {
        Object  principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            User user = (User) principal;
            return favoriteService.getProducVarientByColorId(id);
        }
        else
        {
            throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống ");
        }
    }

    @GetMapping("/productByColor/{id}")
    public Product getProductByColorId(@PathVariable long id) {
        Object  principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            User user = (User) principal;
            Product product = favoriteService.getProductByColorId(id);

            if (product == null) {
                throw new RuntimeException("Product Null ");
            }

            return product;
        }
        else
        {
            throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống ");
        }

    }

    @GetMapping("/check")
    public Boolean checkFav(@RequestParam("colorId") long ColorId){
        Object  principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            User user = (User) principal;
            return favoriteService.checkFavorite(user, ColorId);
        }
        else
        {
            throw new RuntimeException("Người dùng chưa đăng nhập vào hệ thống ");
        }

    }

}
