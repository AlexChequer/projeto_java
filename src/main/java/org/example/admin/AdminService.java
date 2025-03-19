package org.example.admin;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AdminService {

    HashMap<Integer, Admin> admins = new HashMap<>();

    public HashMap<Integer, Admin> getAdmins() {
        return admins;
    }

    public Admin getAdmin(int id)
    {
        return admins.get(id);
    }

    public Admin saveAdmin(Admin admin)
    {
        if (admin.getId() == 0)
        {
            int newId = admins.size() +1;
            admin.setId(newId);
        }

        admins.put(admin.getId(), admin);

        return admin;
    }

    public Admin deleteAdmin(int id)
    {
        return admins.remove(id);
    }
}
