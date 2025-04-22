package com.stepup.dtos.requests;

public class FavoriteDTO {
    private Long colorId;

    private  Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getColorId() {
        return colorId;
    }

    public void setColorId(Long colorId) {
        this.colorId = colorId;
    }
}
