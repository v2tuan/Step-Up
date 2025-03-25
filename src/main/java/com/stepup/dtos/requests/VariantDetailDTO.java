package com.stepup.dtos.requests;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.stepup.entity.Color;
import com.stepup.entity.OrderItem;
import com.stepup.entity.Product;
import com.stepup.entity.Size;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class VariantDetailDTO {
    private String color;
    private String size;
    private int quantity;
}
