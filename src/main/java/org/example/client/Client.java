package org.example.client;

import org.example.user.User;
import javax.persistence.*;

@Entity
@Table(name = "clients")
public class Client extends User {

    protected Client() {
        super();
    }

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
