package com.aws.certification_practice.utils;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class ResponseUtils {

    public ResponseEntity<?> buildResponse(int status, String message) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", status);
        responseBody.put("message", message);
        return ResponseEntity.status(status).body(responseBody);
    }

    public ResponseEntity<?> buildResponse(int status, String message, Object data) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", status);
        responseBody.put("message", message);
        responseBody.put("data", data);
        return ResponseEntity.status(status).body(responseBody);
    }

}
