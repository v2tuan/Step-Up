package com.stepup.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    private String sku;
    private int quantity;

    @ManyToMany
    @JoinTable(
            name = "productVariant_variantValue",
            joinColumns = @JoinColumn(name = "productVariant_id"),
            inverseJoinColumns = @JoinColumn(name = "variantValue_id")
    )
    @JsonManagedReference
    private List<VariantValue> variantValues;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productVariant")
    private List<OrderItem> orderItem;
}
