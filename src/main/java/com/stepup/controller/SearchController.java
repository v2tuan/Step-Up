package com.stepup.controller;

import com.stepup.entity.Color;
import com.stepup.entity.Favorite;
import com.stepup.entity.Product;
import com.stepup.mapper.IProductMapper;
import com.stepup.dtos.responses.ProductCardResponse;
import com.stepup.repository.ColorRepository;
import com.stepup.service.impl.CategoryServiceImpl;
import com.stepup.service.impl.FavoriteServiceImpl;
import com.stepup.service.redis.ProductRedisService;
import com.stepup.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private FavoriteServiceImpl favoriteService;

    @Autowired
    private IProductMapper productMapper;
    @Autowired
    private CategoryServiceImpl categoryService;
    @Autowired
    private ColorRepository colorRepository;

    @GetMapping
    public ResponseEntity<?> searchProducts(@RequestParam("query") String query) {
        // Normalize query
        String searchQuery = query.trim().toLowerCase();

        // Search products with priority: category > name > description
        List<Product> products = productService.searchProductsByQuery(searchQuery);

        // Test user (same as provided code)
        long userId = 39;
        List<Favorite> favorites = favoriteService.getFavoriteByUserId(userId);

        // Get set of favorite color IDs
        Set<Long> favoriteColorIds = favorites.stream()
                .map(fav -> fav.getColor().getId())
                .collect(Collectors.toSet());

        // Map products to ProductCardResponse
        List<ProductCardResponse> productCardResponses = productMapper.toProductCard(products);

        // Set isFav flag based on favorite colors
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            ProductCardResponse response = productCardResponses.get(i);

            boolean isFav = product.getColors().stream()
                    .anyMatch(color -> favoriteColorIds.contains(color.getId()));

            response.setFav(isFav);
        }

        return ResponseEntity.ok().body(productCardResponses);
    }

    @GetMapping("suggestions")
    public ResponseEntity<List<String>> getSearchSuggestions() {
        List<String> productNames = productService.findAllProductNames();
        List<String> categoryNames = categoryService.findAllCategoryNames();
        Set<String> mergedSuggestions = new HashSet<>();
        mergedSuggestions.addAll(productNames);
        mergedSuggestions.addAll(categoryNames);
        return ResponseEntity.ok(new ArrayList<>(mergedSuggestions));
    }

    @GetMapping("/colors")
    public ResponseEntity<List<String>> getAllDistinctColors() {
        List<String> colors = colorRepository.findAllDistinctColorNames();
        return ResponseEntity.ok(colors);
    }

}
