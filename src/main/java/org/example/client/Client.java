package org.example.client;

import jakarta.persistence.*;
import org.example.user.User;

@Entity
@Table(name = "clients")
public class Client extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    public Client(int id, String name, String email, String password)
    {
        super(id, name, email, password);
    }

    @Transient
    private Cart cart;

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getRole()
    {
        return "CLIENT";
    }


}
