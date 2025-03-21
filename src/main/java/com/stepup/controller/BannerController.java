package com.stepup.controller;

import com.stepup.dtos.responses.BannerResponse;
import com.stepup.dtos.responses.ResponseObject;
import com.stepup.entity.Banner;
import com.stepup.service.impl.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banner")
public class BannerController {
    @Autowired
    private BannerService bannerService;
    @GetMapping
    public ResponseEntity<?> getAllBanner() {
        List<BannerResponse> bannerResponses = bannerService.getAllBanner();
//        return ResponseEntity.ok().body(ResponseObject.builder()
//                .message("Get All Banner")
//                .status(HttpStatus.OK)
//                .data(bannerResponses)
//                .build());
        return ResponseEntity.ok().body(bannerResponses);
    }
}
