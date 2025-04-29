package org.example.client;


import org.example.client.exception.ClientNotFoundException;
import org.example.common.exception.AtleastOneException;
import org.example.common.exception.EmailRequiredException;
import org.example.common.exception.NameRequiredException;
import org.example.dto.ClientUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.dto.ClientCreateDTO;

import java.util.List;
import java.net.URI;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientservice;

    @GetMapping("/client")
    public ResponseEntity<List<Client>> getClients() {

        List<Client> clients = clientservice.getClients();

        if (clients.isEmpty()) {
            throw new ClientNotFoundException("no clients found");
        }
        return ResponseEntity.ok(clients);
    }

    @PostMapping("/client")
    public ResponseEntity<Client> saveClient(@RequestBody ClientCreateDTO clientDTO) {
        if (clientDTO.getName() == null) {
            throw new NameRequiredException("name cant be null");
        }

        if (clientDTO.getEmail() == null) {
            throw new EmailRequiredException("email cannot be null");
        }

        Client client = new Client(clientDTO.getName(), clientDTO.getEmail(), clientDTO.getPassword());

        Client newClient = clientservice.saveClient(client);

        URI location = URI.create("/client/" + newClient.getId());
        return ResponseEntity.created(location).body(newClient);
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<Client> getClient(@PathVariable Integer id) {
        Client client = clientservice.getClient(id);
        if (client == null) {
            throw new ClientNotFoundException("client not found");
        } else {
            return ResponseEntity.ok(client);
        }
    }

    @DeleteMapping("/client/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Integer id) {
        try {
            clientservice.removeClient(id);
            return ResponseEntity.noContent().build();
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            throw new ClientNotFoundException("client not found");
        }
    }

    @PutMapping("/client/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Integer id, @RequestBody ClientUpdateDTO client) {
        Client existing = clientservice.getClient(id);
        boolean hasName = false;
        boolean hasEmail = false;

        if (existing == null) {
            throw new ClientNotFoundException("Existing client not found");
        }

        if (!(client.getName() == null)) {
            hasName = true;
        }

        if (!(client.getEmail() == null)) {
            hasEmail = true;
        }

        if (!hasEmail && !hasName) {
            throw new AtleastOneException("At least one field is necessary");
        }

        if (hasName) {
            existing.setName(client.getName());
        }

        if (hasEmail) {
            existing.setEmail(client.getEmail());
        }

        Client updated = clientservice.saveClient(existing);
        return ResponseEntity.ok(updated);
    }
}

