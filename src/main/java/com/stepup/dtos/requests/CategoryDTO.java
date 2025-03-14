package com.stepup.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data//toString
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
   // @NotEmpty(message = "Category's name cannot be empty")
    private Long id;
    private String name;
    private String slug;
    private String image;
}
