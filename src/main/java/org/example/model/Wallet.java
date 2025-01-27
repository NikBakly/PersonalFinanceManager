package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Wallet implements Serializable {
    private String name;
    private double balance;
    private final List<Operation> operations;

    public Wallet(String name) {
        this.name = name;
        this.balance = 0.0;
        this.operations = new ArrayList<>();
    }
}
