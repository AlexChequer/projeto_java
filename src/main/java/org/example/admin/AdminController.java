package org.example.admin;

import org.example.admin.exception.AdminNotFoundException;
import org.example.common.exception.EmailRequiredException;
import org.example.common.exception.NameRequiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.net.URI;

@RestController
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("/admin")
    public ResponseEntity<List<Admin>> getAdmins() {
        List<Admin> admins = adminService.getAdmins();

        if (admins.isEmpty())
        {
            throw new AdminNotFoundException("did not find any admins");
        }
        return ResponseEntity.ok(admins);
    };

    @PostMapping("/admin")
    public ResponseEntity<Admin> saveAdmin(@RequestBody Admin admin)
    {
        if (admin.getName() == null)
        {
            throw new NameRequiredException("name is required");
        }
        if (admin.getEmail() == null)
        {
            throw new EmailRequiredException("email is required");
        }

        Admin newAdmin = adminService.saveAdmin(admin);

        URI location = URI.create("/admin/" + admin.getId());
        return ResponseEntity.created(location).body(newAdmin);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<Admin> getAdmin(@PathVariable int id) {
        Admin admin = adminService.getAdmin(id);
        if (admin != null) {
            return ResponseEntity.ok(admin);
        } else {
            throw new AdminNotFoundException("admin not found");
        }
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable int id) {
        try {
            adminService.deleteAdmin(id);
            return ResponseEntity.noContent().build();
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            throw new AdminNotFoundException("admin not found");
        }
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable int id, @RequestBody Admin admin) {
        Admin existing = adminService.getAdmin(id);
        if (existing == null) {
            throw new AdminNotFoundException("admin not found");
        }
        admin.setId(id);
        Admin updated = adminService.saveAdmin(admin);
        return ResponseEntity.ok(updated);
    }
}

