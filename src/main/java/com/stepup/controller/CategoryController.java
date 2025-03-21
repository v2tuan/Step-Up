package com.stepup.controller;

import com.stepup.dtos.requests.CategoryDTO;
import com.stepup.dtos.responses.ResponseObject;
import com.stepup.entity.Category;
import com.stepup.entity.Product;
import com.stepup.service.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        try {
            Category category = categoryService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message("Category retrieved successfully")
                    .status(HttpStatus.OK)
                    .data(category)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseObject.builder()
                            .message("Category not found")
                            .status(HttpStatus.NOT_FOUND)
                            .data(null)
                            .build()
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(errors.toString())
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        }
        try {
            Category newCategory = categoryService.createCategory(categoryDTO);
            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message("Category created successfully")
                    .status(HttpStatus.OK)
                    .data(newCategory)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseObject.builder()
                            .message("Error creating category: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .data(null)
                            .build()
            );
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message(errors.toString())
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        }

        try {
            Optional<Category> existingCategory = categoryService.findById(id);
            if (existingCategory.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ResponseObject.builder()
                                .message("Category with ID " + id + " not found!")
                                .status(HttpStatus.NOT_FOUND)
                                .data(null)
                                .build()
                );
            }

            Category updatedCategory = categoryService.updateCategory(id, categoryDTO);

            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message("Category updated successfully")
                    .status(HttpStatus.OK)
                    .data(updatedCategory)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseObject.builder()
                            .message("Error updating category: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .data(null)
                            .build()
            );
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Category deleted successfully")
                .status(HttpStatus.OK)
                .data(null)
                .build());
    }
}

