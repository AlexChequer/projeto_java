package org.example.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientservice;

    @GetMapping("/client")
    public HashMap<Integer, Client> getClients() {
        return clientservice.getClients();
    }

    @PostMapping("/client")
    @ResponseStatus(HttpStatus.CREATED)
    public String saveClient(@RequestBody Client client) {
        if (client.getName() == null) {
            return "Name cannot be null";
        }

        if (client.getEmail() == null) {
            return "Email cannot be null";
        }

        clientservice.saveClient(client);
        return "Cliente saved successfully";
    }

    @GetMapping("/client/{id}")
    public Client getClient(@PathVariable Integer id) {
        return clientservice.getClient(id);
    }

    @DeleteMapping("/client/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteClient(@PathVariable Integer id) {
        Client client = clientservice.removeClient(id);
        if (client != null) {
            return "Client removed successfully";
        }
        return "Client not found";
    }

    @PutMapping("client/{id}")
    public String updateClient(@PathVariable Integer id, @RequestBody Client client) {
        Client updatedClient = clientservice.updateClient(id, client);
        if (updatedClient != null) {
            return "Client updated successfully";
        }
        return "Client not found";
    }
}

