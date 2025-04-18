package org.example.admin;

import org.example.user.User;
import javax.persistence.*;

@Entity
@Table(name = "admins")
public class Admin extends User {

    protected Admin() {
        super();
    }

    public Admin(int id, String name, String email, String password)
    {
        super(id, name, email, password);
    }

    public String getRole()
    {
        return "ADMIN";
    }

}
