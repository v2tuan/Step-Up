package com.stepup.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stepup.dtos.responses.ProductResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@AllArgsConstructor
@NotEmpty
@Service
public class ProductRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    //    @Value("${spring.data.redis.use-redis-cache}")
//    private boolean useRedisCache;
    private String getKeyFrom(Long keyword) {
        String key = String.format("product:%d", keyword);
        return key;
    }

    public ProductResponse getProduct(Long keyword) throws JsonProcessingException {

//        if(useRedisCache == false) {
//            return null;
//        }
        String key = this.getKeyFrom(keyword);
        String json = (String) redisTemplate.opsForValue().get(key);
        ProductResponse productResponse =
                json != null ?
                        redisObjectMapper.readValue(json, new TypeReference<ProductResponse>() {
                        })
                        : null;
        return productResponse;
    }

    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }


    //save to Redis
    public void saveProducts(ProductResponse productResponse,
                             Long keyword) throws JsonProcessingException {
        String key = this.getKeyFrom(keyword);
        String json = redisObjectMapper.writeValueAsString(productResponse);
        redisTemplate.opsForValue().set(key, json);
    }
}
