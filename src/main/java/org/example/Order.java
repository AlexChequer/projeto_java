package org.example;

import java.util.List;

public class Order {
    private int id;
    private Client client;
    private List<Item> items;
    private double total;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return cliente;
    }

    public void setClient(Client cliente) {
        this.cliente = cliente;
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