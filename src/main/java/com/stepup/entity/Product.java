package com.stepup.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.stepup.listener.ProductListener;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EntityListeners(ProductListener.class) // Lắng nghe thay đổi để xóa cache redis khi có thay đổi
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String slug;
    private String description;
    private boolean isActive;

    @Column(name = "thumbnail", length = 300)
    private String thumbnail;

    private String video;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
//    @JsonBackReference
    @JsonIgnore
    private Category category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Color> colors = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Size> sizes = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ProductVariant> productVariants = new ArrayList<>();

    private Double rating;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                ", thumbnail='" + thumbnail + '\'' +
                ", video='" + video + '\'' +
                ", category=" + category +
                ", colors=" + colors +
                ", sizes=" + sizes +
                ", productVariants=" + productVariants +
                ", rating=" + rating +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}