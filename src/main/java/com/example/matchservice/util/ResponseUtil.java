package com.example.matchservice.util;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ResponseUtil {

    public static ResponseEntity<Map<String, String>> success(String message) {
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", message
        ));
    }

}
