package org.example.client;

import org.example.stock.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private Map<Integer, CartItem> cartItems = new HashMap<>();
    private double total = 0.0;

    public List<CartItem> getItems() {
        return new ArrayList<>(cartItems.values());
    }
    
    public boolean addItem(Item item, int quantity) {
        if (item == null || quantity <= 0) {
            return false;
        }
        
        int itemId = item.getId();
        if (cartItems.containsKey(itemId)) {
            CartItem existingItem = cartItems.get(itemId);
            existingItem.increaseQuantity(quantity);
        } else {
            cartItems.put(itemId, new CartItem(item, quantity));
        }
        
        calculateTotal();
        return true;
    }
    
    public boolean removeItem(int itemId) {
        if (cartItems.containsKey(itemId)) {
            cartItems.remove(itemId);
            calculateTotal();
            return true;
        }
        return false;
    }
    
    public boolean updateItemQuantity(int itemId, int quantity) {
        if (cartItems.containsKey(itemId) && quantity > 0) {
            cartItems.get(itemId).setQuantity(quantity);
            calculateTotal();
            return true;
        }
        return false;
    }
    
    public void clear() {
        cartItems.clear();
        total = 0.0;
    }
    
    public double getTotal() {
        return total;
    }
    
    private void calculateTotal() {
        total = 0.0;
        for (CartItem item : cartItems.values()) {
            total += item.getSubtotal();
        }
    }
    
    public static class CartItem {
        private Item item;
        private int quantity;
        
        public CartItem(Item item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }
        
        public Item getItem() {
            return item;
        }
        
        public int getQuantity() {
            return quantity;
        }
        
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
        
        public void increaseQuantity(int amount) {
            this.quantity += amount;
        }
        
        public double getSubtotal() {
            return item.getPrice() * quantity;
        }
    }
}
