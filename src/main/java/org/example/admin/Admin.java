package org.example.admin;

import jakarta.persistence.*;
import org.example.user.User;

@Entity
@Table(name = "admins")
public class Admin extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Admin(int id, String name, String email, String password)
    {
        super(id, name, email, password);
    }

    public String getRole()
    {
        return "ADMIN";
    }

}
