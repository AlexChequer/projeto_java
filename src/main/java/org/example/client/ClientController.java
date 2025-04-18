package org.example.client;


import org.example.client.exception.ClientNotFoundException;
import org.example.common.exception.EmailRequiredException;
import org.example.common.exception.NameRequiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientservice;

    @GetMapping("/client")
    public ResponseEntity<List<Client>> getClients() { // aq define q o response entity espera esse tipo

        List<Client> clients = clientservice.getClients();

        if (clients.isEmpty())
        {
            throw new ClientNotFoundException("no clients found");
        }
        return ResponseEntity.ok(clients);
    }

    @PostMapping("/client")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Client> saveClient(@RequestBody Client client) {
        if (client.getName() == null) {
            throw new NameRequiredException("name cant be null");
        }

        if (client.getEmail() == null) {
            throw new EmailRequiredException("email cannot be null");
        }

        Client newClient = clientservice.saveClient(client);

        URI location = URI.create("/client/" + newClient.getId());
        return ResponseEntity.created(location).body(newClient);
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<Client> getClient(@PathVariable Integer id) {
        Client client = clientservice.getClient(id);
        if (client == null)
        {
            throw new ClientNotFoundException("client not found");
        } else
        {
            return ResponseEntity.ok(client);
        }
    }

    @DeleteMapping("/client/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteClient(@PathVariable Integer id) {
        try {
            clientservice.removeClient(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ClientNotFoundException("not found");
        }
    }

    @PutMapping("client/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Integer id, @RequestBody Client client) {
        Client updatedClient = clientservice.updateClient(id, client);
        if (updatedClient == null)
        {
            throw new ClientNotFoundException("client not found");
        }
        return ResponseEntity.ok(updatedClient);
    }
}

