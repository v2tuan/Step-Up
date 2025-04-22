package com.stepup.dtos.responses;

import com.stepup.entity.Color;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteRespone {
    private long id;
    private String title;
    private Color color;
    private Double price ;

}
