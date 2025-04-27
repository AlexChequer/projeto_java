package org.example.dto;

import java.util.List;


public class OrderCreateDTO {
    private int clientId;
    private List<OrderItemDTO> items;
    private String paymentType;
    
    public OrderCreateDTO() {
    }
    
    public OrderCreateDTO(int clientId, List<OrderItemDTO> items, String paymentType) {
        this.clientId = clientId;
        this.items = items;
        this.paymentType = paymentType;
    }
    
    public int getClientId() {
        return clientId;
    }
    
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
    
    public List<OrderItemDTO> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
    
    public String getPaymentType() {
        return paymentType;
    }
    
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
    
    public static class OrderItemDTO {
        private int itemId;
        private int quantity;
        
        public OrderItemDTO() {
        }
        
        public OrderItemDTO(int itemId, int quantity) {
            this.itemId = itemId;
            this.quantity = quantity;
        }
        
        public int getItemId() {
            return itemId;
        }
        
        public void setItemId(int itemId) {
            this.itemId = itemId;
        }
        
        public int getQuantity() {
            return quantity;
        }
        
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}