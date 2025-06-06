package org.example.admin;

import org.example.admin.exception.AdminNotFoundException;
import org.example.common.exception.AtleastOneException;
import org.example.common.exception.EmailRequiredException;
import org.example.common.exception.NameRequiredException;
import org.example.dto.AdminUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.dto.AdminCreateDTO;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<Admin> saveAdmin(@RequestBody AdminCreateDTO admin)
    {
        if (admin.getName() == null)
        {
            throw new NameRequiredException("name is required");
        }
        if (admin.getEmail() == null)
        {
            throw new EmailRequiredException("email is required");
        }

        Admin newAdmin = new Admin();
        newAdmin.setName(admin.getName());
        newAdmin.setEmail(admin.getEmail());
        newAdmin.setPassword(admin.getPassword());

        Admin createdAdmin = adminService.saveAdmin(newAdmin);

        URI location = URI.create("/admin/" + createdAdmin.getId());
        return ResponseEntity.created(location).body(createdAdmin);
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
    public ResponseEntity<Admin> updateAdmin(@PathVariable int id, @RequestBody AdminUpdateDTO admin) {
        Admin existing = adminService.getAdmin(id);
        boolean hasEmail = false;
        boolean hasName = false;

        if (existing == null) {
            throw new AdminNotFoundException("current admin profile not found");
        }

        if (!(admin.getName() == null)) {
            hasName = true;
        }
        if (!(admin.getEmail() == null)) {
            hasEmail = true;
        }

        if (!hasName && !hasEmail) {
            throw new AtleastOneException("At least one field is necessary");
        }
        if (hasName) {
            existing.setName(admin.getName());
        }
        if (hasEmail) {
            existing.setEmail(admin.getEmail());
        }
        Admin updated = adminService.saveAdmin(existing);
        return ResponseEntity.ok(updated);
    }
}

