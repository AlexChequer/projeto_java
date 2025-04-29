package org.example.dto;

import org.example.client.Cart;

public class ClientUpdateDTO {
    private String name;
    private String email;

    public ClientUpdateDTO(String name, String email, Cart cart) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
