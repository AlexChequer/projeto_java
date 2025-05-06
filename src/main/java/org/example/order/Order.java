package org.example.order;

import jakarta.persistence.*;
import org.example.client.Client;
import org.example.stock.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    
    // Revertido para o mapeamento original compatível com o esquema do banco existente
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<Item> items;
    
    // Usado para rastrear quantidades sem criar tabela adicional
    @Transient
    private java.util.Map<Integer, Integer> itemQuantities = new java.util.HashMap<>();
    
    private double total;
    private String paymentType;
    private LocalDateTime paymentDate;
    private float paymentAmount;
    private String status = "PENDING"; // PENDING, PAID, SHIPPED, DELIVERED, CANCELLED

    public Order() {
        this.items = new ArrayList<>();
        this.total = 0;
    }

    public Order(int id) {
        this.id = id;
        this.items = new ArrayList<>();
        this.total = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client cliente) {
        this.client = cliente;
    }

    public List<Item> getItems() {
        return items;
    }

    public Item getItem(int id) {
        for (Item item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public void setItem(Item item) {
        // Guarda o ID original para referência
        int originalId = item.getId();
        
        // Cria uma cópia do item para o pedido
        Item orderItem = new Item();
        orderItem.setName(item.getName());
        orderItem.setPrice(item.getPrice());
        orderItem.setStock(item.getStock());
        orderItem.setCategory(item.getCategory());
        orderItem.setDescription(item.getDescription());
        orderItem.setImageUrl(item.getImageUrl());
        
        this.items.add(orderItem);
        
        // Armazena o ID original para rastreamento
        itemQuantities.put(originalId, itemQuantities.getOrDefault(originalId, 0) + 1);
    }
    
    public void addItem(Item item, int quantity) {
        // Guarda o ID original para referência
        int originalId = item.getId();
        
        // Cria uma cópia do item para o pedido com preço total já calculado
        Item orderItem = new Item();
        orderItem.setName(item.getName());
        orderItem.setPrice(item.getPrice() * quantity);
        orderItem.setStock(quantity); // Usamos o campo stock para representar a quantidade
        orderItem.setCategory(item.getCategory());
        orderItem.setDescription(item.getDescription());
        orderItem.setImageUrl(item.getImageUrl());
        
        this.items.add(orderItem);
        
        // Armazena o ID original para rastreamento
        itemQuantities.put(originalId, itemQuantities.getOrDefault(originalId, 0) + quantity);
    }
    
    public int getItemQuantity(int originalItemId) {
        return itemQuantities.getOrDefault(originalItemId, 0);
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public float getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(float paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public boolean processPayment(String paymentType, float amount) {
        if (amount >= this.total) {
            this.paymentType = paymentType;
            this.paymentAmount = amount;
            this.paymentDate = LocalDateTime.now();
            this.status = "PAID";
            return true;
        }
        return false;
    }
    
    public java.util.Map<Integer, Integer> getItemQuantities() {
        return this.itemQuantities;
    }
}