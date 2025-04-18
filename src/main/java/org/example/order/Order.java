package org.example.order;

import jakarta.persistence.*;
import org.example.client.Client;
import org.example.stock.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany
    private Client client;
    @ManyToOne
    private List<Item> items;
    private double total;
    private String paymentType;
    private LocalDateTime paymentDate;
    private float paymentAmount;

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
}