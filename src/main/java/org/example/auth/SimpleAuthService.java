package org.example.auth;

import org.example.admin.Admin;
import org.example.admin.AdminRepository;
import org.example.client.Client;
import org.example.client.ClientRepository;
import org.example.dto.SimpleLoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleAuthService {
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    public boolean validateCredentials(SimpleLoginDTO loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null || loginRequest.getUserType() == null) {
            return false;
        }
        
        if (loginRequest.getUserType().equals("CLIENT")) {
            return validateClientCredentials(loginRequest.getEmail(), loginRequest.getPassword());
        } else if (loginRequest.getUserType().equals("ADMIN")) {
            return validateAdminCredentials(loginRequest.getEmail(), loginRequest.getPassword());
        }
        
        return false;
    }
    
    private boolean validateClientCredentials(String email, String password) {
        Client client = clientRepository.findByEmail(email);
        return client != null && client.getPassword().equals(password);
    }
    
    private boolean validateAdminCredentials(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        return admin != null && admin.getPassword().equals(password);
    }
}