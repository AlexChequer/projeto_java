package org.example;

import java.time.LocalDateTime;

public class Transaction {
    private String paymentType;
    private LocalDateTime paymentDate;
    private float paymentAmount;

    public boolean processarPagamento(Client cliente, double paymentAmount) {

        return true;
    }
}
