package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class User implements Serializable {
    private String username;
    private String password;
    private List<Wallet> wallets;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.wallets = new ArrayList<>();
        this.wallets.add(new Wallet("Default Wallet"));
    }
}

