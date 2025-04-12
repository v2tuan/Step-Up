package com.stepup.dtos.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.stepup.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResponse {
    private String name;
    private String slug;
    private String description;
    private boolean isActive;
    private Double price;
    private Double promotionPrice;

    private String thumbnail;

    private String video;

    private List<Color> colors = new ArrayList<>();

    private List<Size> sizes = new ArrayList<>();

    private List<ProductVariant> productVariants = new ArrayList<>();

    private Double rating;

}
