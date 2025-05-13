package com.stepup.service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.stepup.dtos.responses.ReviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    @Autowired
    public ReviewRedisService(RedisTemplate<String, Object> redisTemplate, ObjectMapper redisObjectMapper) {
        this.redisTemplate = redisTemplate;
        this.redisObjectMapper = redisObjectMapper;
    }

    private String getKeyFrom(Long productId) {
        return String.format("reviews:product:%d", productId);
    }

    public List<ReviewResponse> getReviews(Long productId) throws JsonProcessingException {
        String key = this.getKeyFrom(productId);
        String json = (String) redisTemplate.opsForValue().get(key);
        return json != null ?
                redisObjectMapper.readValue(json, new TypeReference<List<ReviewResponse>>() {})
                : null;
    }

    public void saveReviews(List<ReviewResponse> reviews, Long productId) throws JsonProcessingException {
        String key = this.getKeyFrom(productId);
        String json = redisObjectMapper.writeValueAsString(reviews);
        redisTemplate.opsForValue().set(key, json);
    }

    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    public void invalidateCache(Long productId) {
        String key = this.getKeyFrom(productId);
        redisTemplate.delete(key);
    }
}