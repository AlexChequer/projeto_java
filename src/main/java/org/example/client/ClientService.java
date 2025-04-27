package org.example.client;

import org.example.common.exception.EmailRequiredException;
import org.example.common.exception.NameRequiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    
    private final Map<Integer, Cart> cartCache = new ConcurrentHashMap<>();

    public List<Client> getClients() {
        List<Client> clients = clientRepository.findAll();
        return clients;
    }

    @Transactional
    public Client saveClient(Client client) {
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new NameRequiredException("Client name cannot be empty");
        }
        
        if (client.getEmail() == null || client.getEmail().trim().isEmpty()) {
            throw new EmailRequiredException("Client email cannot be empty");
        }
        
        if (client.getId() > 0 && cartCache.containsKey(client.getId())) {
            Client savedClient = clientRepository.save(client);
            savedClient.setCart(cartCache.get(client.getId()));
            return savedClient;
        }
        
        return clientRepository.save(client);
    }

    public Client getClient(Integer id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        if (cartCache.containsKey(id)) {
            client.setCart(cartCache.get(id));
        } else {
            cartCache.put(id, client.getCart());
        }
        
        return client;
    }

    public void removeClient(Integer id) {
        cartCache.remove(id);
        clientRepository.deleteById(id);
    }

    public Client updateClient(Integer id, Client client) {
        Client client_found = this.getClient(id);
        if (client == null) {
            return null;
        }
        
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new NameRequiredException("Client name cannot be empty");
        }
        
        if (client.getEmail() == null || client.getEmail().trim().isEmpty()) {
            throw new EmailRequiredException("Client email cannot be empty");
        }
        
        Cart cart = client_found.getCart();
        client.setId(id);
        client.setCart(cart);
        
        Client savedClient = clientRepository.save(client);
        
        cartCache.put(id, cart);
        
        return savedClient;
    }
    
    public void updateCartCache(Integer clientId, Cart cart) {
        cartCache.put(clientId, cart);
    }
}
