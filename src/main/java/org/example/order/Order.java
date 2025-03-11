package org.example.order;

import org.example.client.Client;
import org.example.stock.Item;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int id;
    private Client client;
    private List<Item> items;
    private double total;
    private String paymentType;
    private LocalDateTime paymentDate;
    private float paymentAmount;

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

    public List<Item> getItem() {
        return items;
    }

    public void setItem(List<Item> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}