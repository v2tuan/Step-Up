package com.stepup.mapper;

import com.stepup.entity.Review;
import com.stepup.dtos.responses.ReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IReviewMapper {

    @Mapping(target = "userFullName", expression = "java(review.getUser().getFullName())")
    @Mapping(target = "productName", expression = "java(review.getProduct().getName())")
    @Mapping(target = "imageUrls", expression = "java(review.getImages().stream().map(com.stepup.entity.ReviewImage::getImageUrl).collect(java.util.stream.Collectors.toList()))")
    ReviewResponse toReviewResponse(Review review);
}