package org.example.client;

import org.example.user.User;

public class Client extends User {

    public Client(int id, String name, String email, String password)
    {
        super(id, name, email, password);
    }

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
