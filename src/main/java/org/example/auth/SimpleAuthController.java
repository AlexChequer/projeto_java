package org.example.auth;

import org.example.dto.SimpleLoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SimpleAuthController {

    @Autowired
    private SimpleAuthService simpleAuthService;
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Boolean>> login(@RequestBody SimpleLoginDTO loginRequest) {
        boolean isValid = simpleAuthService.validateCredentials(loginRequest);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", isValid);
        
        return ResponseEntity.ok(response);
    }
}