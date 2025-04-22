package com.stepup.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCardResponse {
    private long id;

    private String name;

    private Double price;

    private String ImageUrl;

    private Double promotionPrice;

    private Double rating;

    private boolean fav;
}
