package org.example.order;

import org.example.stock.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{clientId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void insertItem(@RequestBody Item item,
                           @PathVariable int clientId) {
        orderService.insertItem(item, clientId);
    }
    
    @PostMapping("/{clientId}/checkout")
    public ResponseEntity<Order> createOrderFromCart(@PathVariable int clientId) {
        Order order = orderService.createOrderFromCart(clientId);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    
    @PostMapping("/{orderId}/payment")
    public ResponseEntity<Map<String, Object>> processPayment(
            @PathVariable int orderId,
            @RequestBody Map<String, Object> paymentDetails) {
        
        String paymentType = (String) paymentDetails.get("paymentType");
        float amount = Float.parseFloat(paymentDetails.get("amount").toString());
        
        boolean success = orderService.processPayment(orderId, paymentType, amount);
        
        if (success) {
            return ResponseEntity.ok(Map.of(
                "message", "Payment processed successfully",
                "status", "PAID",
                "orderId", orderId
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Payment failed",
                "orderId", orderId
            ));
        }
    }
    
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable int orderId,
            @RequestBody Map<String, String> statusUpdate) {
        
        String status = statusUpdate.get("status");
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable int id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
    
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Order>> getOrdersByClientId(@PathVariable int clientId) {
        List<Order> clientOrders = orderService.getOrdersByClientId(clientId);
        return ResponseEntity.ok(clientOrders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable int id) {
        Order order = orderService.getOrder(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }
}
