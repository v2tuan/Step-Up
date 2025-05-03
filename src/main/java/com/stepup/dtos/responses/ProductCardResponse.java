package com.stepup.dtos.responses;

import com.stepup.entity.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    private List<ColorResponse> colors = new ArrayList<>();
}
