package org.example.admin;

import org.example.user.User;

public class Admin extends User {

    public Admin(int id, String name, String email, String password)
    {
        super(id, name, email, password);
    }





    public String getRole()
    {
        return "ADMIN";
    }

}
