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
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<Item> items;
    
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
        this.items.add(item);
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
}