package org.example.order;

import org.example.client.Client;
import org.example.client.ClientService;
import org.example.stock.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientService clientService;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        ClientService clientService) {
        this.orderRepository = orderRepository;
        this.clientService   = clientService;
    }

    public void insertItem(Item payload, int clientId) {
        Client client = clientService.getClient(clientId);

        Order order = null;
        List<Order> all = orderRepository.findAll();  // busca todos os pedidos do BD :contentReference[oaicite:0]{index=0}
        for (Order o : all) {
            if (o.getClient().getId() == clientId) {
                order = o;
                break;
            }
        }
        if (order == null) {
            order = new Order(clientId);
            order.setClient(client);
        }

        Item existing = order.getItem(payload.getId());
        if (existing != null) {
            existing.setStock(existing.getStock() + payload.getStock());
            existing.setPrice(
                    existing.getPrice() +
                            payload.getPrice() * payload.getStock()
            );
        } else {
            payload.setPrice(payload.getPrice() * payload.getStock());
            order.setItem(payload);
        }

        double soma = 0;
        for (Item it : order.getItems()) {
            soma += it.getPrice();
        }
        order.setTotal(soma);

        orderRepository.save(order);
    }

    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }

    public Map<Integer, Order> getOrders() {
        Map<Integer, Order> map = new HashMap<>();
        List<Order> all = orderRepository.findAll();
        for (Order o : all) {
            map.put(o.getClient().getId(), o);
        }
        return map;
    }

    public Order getOrder(int id) {
        return orderRepository.findById(id).orElse(null);
    }
}
