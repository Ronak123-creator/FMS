package com.backend.foodproject.controller;

import com.backend.foodproject.service.Implementation.QrServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/qr")
@RequiredArgsConstructor
public class QrController {

    @Value("${app.backend.base-url}")
    private String beBase;
    private final QrServiceImpl qrService;


    @GetMapping(value = "/menu.png", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> menuQr(@RequestParam (defaultValue = "512") int size) throws Exception {
        String url = beBase + "/api/food/qrmenu";
        return ResponseEntity.ok(qrService.qrPng(url,size));
    }
}
