package org.example.order;

import org.example.client.Client;
import org.example.client.ClientService;
import org.example.stock.Item;
import org.example.stock.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class OrderService {
    private HashMap<Integer, Order> orders = new HashMap<>();

    @Autowired
    private ClientService clientservice;

    public void insertItem (Item item, int clientId) {
        Client client = clientservice.getClient(clientId);
        Order order = orders.get(client.getId());
        if (order == null) {
            order = new Order(clientId);
            order.setClient(client);
        }
        Item existingItem = order.getItem(item.getId());
        if (existingItem != null) {
            existingItem.setStock(existingItem.getStock() + item.getStock());
            existingItem.setPrice(existingItem.getPrice() + item.getPrice()*item.getStock());
        } else {
            Item newItem = item;
            newItem.setPrice(item.getStock()*item.getPrice());
            order.setItem(newItem);
        }

        orders.put(client.getId(), order);
    }

    public void deleteOrder(int id) {
        for (int i: orders.keySet()) {
            if (i == id) {
                orders.remove(i);
            }
        }
    }

    public HashMap<Integer, Order> getOrders() {
        return this.orders;
    }

    public Order getOrder(int id) {
        for (int i: this.orders.keySet()) {
            if (i == id) {
                return orders.get(i);
            }
        }
        return null;
    }
}
