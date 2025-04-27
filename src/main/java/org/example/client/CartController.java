package org.example.client;

import org.example.stock.Item;
import org.example.stock.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ClientService clientService;
    
    @Autowired
    private ItemService itemService;

    @GetMapping("/{clientId}")
    public ResponseEntity<Map<String, Object>> getCart(@PathVariable int clientId) {
        Client client = clientService.getClient(clientId);
        Cart cart = client.getCart();
        
        Map<String, Object> response = new HashMap<>();
        response.put("items", cart.getItems());
        response.put("total", cart.getTotal());
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{clientId}/items")
    public ResponseEntity<Map<String, Object>> addItemToCart(
            @PathVariable int clientId,
            @RequestBody Map<String, Object> request) {
        
        int itemId = (int) request.get("itemId");
        int quantity = (int) request.get("quantity");
        
        if (quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be positive");
        }
        
        Client client = clientService.getClient(clientId);
        Item item = itemService.getItem(itemId);
        
        if (item.getStock() < quantity) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Not enough stock available. Available: " + item.getStock());
        }
        
        client.getCart().addItem(item, quantity);
        
        clientService.updateCartCache(clientId, client.getCart());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Item added to cart successfully");
        response.put("cartItems", client.getCart().getItems());
        response.put("total", client.getCart().getTotal());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @DeleteMapping("/{clientId}/items/{itemId}")
    public ResponseEntity<Map<String, Object>> removeItemFromCart(
            @PathVariable int clientId,
            @PathVariable int itemId) {
        
        Client client = clientService.getClient(clientId);
        boolean removed = client.getCart().removeItem(itemId);
        
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found in cart");
        }
        
        clientService.updateCartCache(clientId, client.getCart());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Item removed from cart successfully");
        response.put("cartItems", client.getCart().getItems());
        response.put("total", client.getCart().getTotal());
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{clientId}/items/{itemId}")
    public ResponseEntity<Map<String, Object>> updateCartItemQuantity(
            @PathVariable int clientId,
            @PathVariable int itemId,
            @RequestBody Map<String, Object> request) {
        
        Integer quantity = null;
        
        if (request.get("quantity") instanceof Integer) {
            quantity = (Integer) request.get("quantity");
        } else if (request.get("quantity") instanceof Double) {
            quantity = ((Double) request.get("quantity")).intValue();
        } else if (request.get("quantity") instanceof String) {
            try {
                quantity = Integer.parseInt((String) request.get("quantity"));
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid quantity format");
            }
        }
        
        if (quantity == null || quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be positive");
        }
        
        Client client = clientService.getClient(clientId);
        
        Item stockItem = itemService.getItem(itemId);
        
        if (stockItem.getStock() < quantity) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Not enough stock available. Available: " + stockItem.getStock());
        }
        
        boolean updated = client.getCart().updateItemQuantity(itemId, quantity);
        
        if (!updated) {
            boolean added = client.getCart().addItem(stockItem, quantity);
            
            if (!added) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found in cart");
            }
        }
        
        clientService.updateCartCache(clientId, client.getCart());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cart item quantity updated successfully");
        response.put("cartItems", client.getCart().getItems());
        response.put("total", client.getCart().getTotal());
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{clientId}")
    public ResponseEntity<Map<String, Object>> clearCart(@PathVariable int clientId) {
        Client client = clientService.getClient(clientId);
        client.getCart().clear();
        
        clientService.updateCartCache(clientId, client.getCart());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cart cleared successfully");
        
        return ResponseEntity.ok(response);
    }
}