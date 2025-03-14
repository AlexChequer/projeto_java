package org.example.order;

import org.example.client.Client;
import org.example.stock.Item;
import org.example.stock.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderservice;

    @PostMapping("/order/{clientId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void insertItem(@RequestBody Item item, @PathVariable int clientId) {
        orderservice.insertItem(item, clientId);
    }

    @DeleteMapping("/order/{id}")
    public void deleteOrder(@PathVariable int id) {
        orderservice.deleteOrder(id);
    }

    @GetMapping("/order")
    public HashMap<Integer, Order> getOrders() {
        return orderservice.getOrders();
    }

    @GetMapping("/order/{id}")
    public Order getOrder(@PathVariable int id) {
        return orderservice.getOrder(id);
    }

}
