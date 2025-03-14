package org.example.client;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ClientService {

    private HashMap<Integer, Client> clients = new HashMap<>();


    public HashMap<Integer, Client> getClients() {
        return clients;
    }


    public void saveClient(Client client) {
        if (client.getId() == 0) {
            // geração de ID
            int newId = clients.size() + 1;
            client.setId(newId);
        }
        clients.put(client.getId(), client);
    }


    public Client getClient(Integer id) {
        return clients.get(id);
    }


    public Client removeClient(Integer id) {
        return clients.remove(id);
    }

    public Client updateClient(Integer id, Client client) {
        if (clients.containsKey(id)) {
            // Força o ID do objeto a ser o mesmo da URL
            clients.put(id, client);
            return client;
        }
        return null;
    }
}
