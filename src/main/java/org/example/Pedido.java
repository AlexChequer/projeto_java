package org.example;

import java.util.List;

public class Pedido {
    private int id;
    private Client cliente;
    private List<Item> produtos;
    private double total;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getCliente() {
        return cliente;
    }

    public void setCliente(Client cliente) {
        this.cliente = cliente;
    }

    public List<Item> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Item> produtos) {
        this.produtos = produtos;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}