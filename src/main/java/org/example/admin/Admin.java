package org.example.admin;

import jakarta.persistence.*;
import org.example.user.User;

@Entity
@Table(name = "admins")
public class Admin extends User {
    
    public Admin() {
        super("", "", "");
    }

    public Admin(String name, String email, String password)
    {
        super(name, email, password);
    }
    
    public Admin(int id, String name, String email, String password)
    {
        super(name, email, password);
        setId(id);
    }

    @Override
    public String getRole()
    {
        return "ADMIN";
    }
}
