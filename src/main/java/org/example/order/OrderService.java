package org.example.order;

import org.example.client.Client;
import org.example.client.ClientService;
import org.example.client.Cart;
import org.example.stock.Item;
import org.example.stock.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientService clientService;
    private final ItemService itemService;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        ClientService clientService,
                        ItemService itemService) {
        this.orderRepository = orderRepository;
        this.clientService = clientService;
        this.itemService = itemService;
    }

    public void insertItem(Item payload, int clientId) {
        Client client = clientService.getClient(clientId);

        Order order = findOrCreatePendingOrder(clientId, client);

        Item existingItem = order.getItem(payload.getId());
        if (existingItem != null) {
            Item stockItem = itemService.getItem(payload.getId());
            if (stockItem.getStock() < payload.getStock()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        "Not enough stock. Available: " + stockItem.getStock());
            }
            
            existingItem.setStock(existingItem.getStock() + payload.getStock());
            existingItem.setPrice(
                    existingItem.getPrice() +
                            payload.getPrice() * payload.getStock()
            );
        } else {
            Item stockItem = itemService.getItem(payload.getId());
            if (stockItem.getStock() < payload.getStock()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        "Not enough stock. Available: " + stockItem.getStock());
            }
            
            payload.setPrice(payload.getPrice() * payload.getStock());
            order.setItem(payload);
        }

        recalculateOrderTotal(order);
        orderRepository.save(order);
    }
    
    private Order findOrCreatePendingOrder(int clientId, Client client) {
        List<Order> allOrders = orderRepository.findAll();
        for (Order o : allOrders) {
            if (o.getClient() != null && 
                o.getClient().getId() == clientId && 
                "PENDING".equals(o.getStatus())) {
                return o;
            }
        }
        
        Order order = new Order();
        order.setClient(client);
        order.setStatus("PENDING");
        return order;
    }
    
    private void recalculateOrderTotal(Order order) {
        double total = 0;
        for (Item item : order.getItems()) {
            total += item.getPrice();
        }
        order.setTotal(total);
    }
    
    @Transactional
    public Order createOrderFromCart(int clientId) {
        Client client = clientService.getClient(clientId);
        Cart cart = client.getCart();
        
        if (cart.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }
        
        Order order = new Order();
        order.setClient(client);
        order.setStatus("PENDING");
        order.setTotal(cart.getTotal());
        
        Order savedOrder = orderRepository.save(order);
        
        for (Cart.CartItem cartItem : cart.getItems()) {
            Item originalItem = cartItem.getItem();
            Item stockItem = itemService.getItem(originalItem.getId());
            
            if (stockItem.getStock() < cartItem.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        "Not enough stock for item " + stockItem.getName() + 
                        ". Available: " + stockItem.getStock());
            }
            
            Item orderItem = new Item();
            orderItem.setName(originalItem.getName());
            orderItem.setPrice(originalItem.getPrice() * cartItem.getQuantity());
            orderItem.setStock(cartItem.getQuantity());
            if (originalItem.getCategory() != null) {
                orderItem.setCategory(originalItem.getCategory());
            }
            orderItem.setDescription(originalItem.getDescription());
            orderItem.setImageUrl(originalItem.getImageUrl());
            
            savedOrder.setItem(orderItem);
            
            stockItem.setStock(stockItem.getStock() - cartItem.getQuantity());
            itemService.putItem(stockItem.getId(), stockItem);
        }
        
        savedOrder = orderRepository.save(savedOrder);
        
        cart.clear();
        
        clientService.updateCartCache(clientId, cart);
        
        return savedOrder;
    }

    public boolean processPayment(int orderId, String paymentType, float amount) {
        Order order = getOrder(orderId);
        
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        
        boolean paymentProcessed = order.processPayment(paymentType, amount);
        if (paymentProcessed) {
            orderRepository.save(order);
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Payment amount insufficient. Required: " + order.getTotal());
        }
    }
    
    public Order updateOrderStatus(int orderId, String status) {
        Order order = getOrder(orderId);
        
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void deleteOrder(int id) {
        Order order = getOrder(id);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        
        if ("PAID".equals(order.getStatus())) {
            for (Item orderItem : order.getItems()) {
                Item stockItem = itemService.getItem(orderItem.getId());
                stockItem.setStock(stockItem.getStock() + orderItem.getStock());
                itemService.putItem(stockItem.getId(), stockItem);
            }
        }
        
        orderRepository.deleteById(id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public List<Order> getOrdersByClientId(int clientId) {
        List<Order> allOrders = orderRepository.findAll();
        List<Order> clientOrders = new ArrayList<>();
        
        for (Order order : allOrders) {
            if (order.getClient() != null && order.getClient().getId() == clientId) {
                clientOrders.add(order);
            }
        }
        
        return clientOrders;
    }

    public Order getOrder(int id) {
        return orderRepository.findById(id).orElse(null);
    }
}
