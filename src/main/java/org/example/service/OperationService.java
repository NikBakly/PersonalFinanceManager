package org.example.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.model.Operation;
import org.example.model.Wallet;
import org.example.util.OperationCategory;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class OperationService {

    public void addOperation(Operation operation, final Wallet wallet) {
        wallet.getOperations().add(operation);
        if (operation.getType() == OperationCategory.INCOME) {
            wallet.setBalance(wallet.getBalance() + operation.getAmount());
        } else {
            wallet.setBalance(wallet.getBalance() - operation.getAmount());
        }
    }

    public List<Operation> getOperationsByCategory(String category, final List<Operation> operations) {
        List<Operation> filtered = new ArrayList<>();
        for (Operation t : operations) {
            if (t.getCategory().equalsIgnoreCase(category)) {
                filtered.add(t);
            }
        }
        return filtered;
    }

    public double calculateTotalByType(OperationCategory type, final List<Operation> operations) {
        return operations.stream()
                .filter(t -> t.getType() == type)
                .mapToDouble(Operation::getAmount)
                .sum();
    }
}
