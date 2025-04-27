package org.example.client;

import jakarta.persistence.*;
import org.example.common.exception.EmailRequiredException;
import org.example.common.exception.NameRequiredException;
import org.example.user.User;

@Entity
@Table(name = "clients")
public class Client extends User {
    
    public Client() {
        super("", "", "");
    }

    public Client(String name, String email, String password)
    {
        super(name, email, password);
    }
    
    public Client(int id, String name, String email, String password)
    {
        super(name, email, password);
        setId(id);
    }

    @Transient
    private Cart cart = new Cart();

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public String getRole()
    {
        return "CLIENT";
    }
}
