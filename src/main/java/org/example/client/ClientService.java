package org.example.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getClients() {
        return clientRepository.findAll();
    }


    public Client saveClient(Client client) {

        return clientRepository.save(client);
    }


    public Client getClient(Integer id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    public void removeClient(Integer id) {
        clientRepository.deleteById(id);
    }

    public Client updateClient(Integer id, Client client) {
        Client client_found = this.getClient(id);
        if (client == null) {
            return null;
        }
        return clientRepository.save(client);
    }
}
