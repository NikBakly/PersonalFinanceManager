

package org.example.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.model.Operation;
import org.example.model.User;
import org.example.model.Wallet;
import org.example.util.AlertManager;
import org.example.util.OperationCategory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class FinanceManager {

    private final OperationService operationService;
    private final Map<String, Double> categoryBudgets = new HashMap<>(); // Хранения бюджетов по категориям

    public void addOperation(User user, String type, String category, double amount) {
        OperationCategory operationCategory = parseOperationType(type);
        Wallet wallet = user.getWallets().get(0);

        if (operationCategory == OperationCategory.EXPENSE) {
            Double budgetLimit = categoryBudgets.getOrDefault(category, Double.MAX_VALUE);

            double currentExpense = operationService.getOperationsByCategory(category, wallet.getOperations()).stream()
                    .filter(t -> t.getType() == OperationCategory.EXPENSE)
                    .mapToDouble(Operation::getAmount)
                    .sum();

            if (currentExpense + amount > budgetLimit) {
                AlertManager.alertGeneral("превышение бюджета для категории '" + category + "'. Установленный лимит: " + budgetLimit + ", текущие расходы: " + currentExpense);
                return;
            }

            if (wallet.getBalance() < amount) {
                AlertManager.alertGeneral("недостаточно средств для выполнения этой транзакции. Текущий баланс: " + wallet.getBalance());
                return;
            }
        }

        operationService.addOperation(new Operation(category, amount, operationCategory), wallet);

        if (operationCategory == OperationCategory.EXPENSE && wallet.getBalance() < 0) {
            AlertManager.alertLowBalance(wallet.getBalance());
        }
    }

    public String createReport(User user) {
        Wallet wallet = user.getWallets().get(0);
        double totalIncome = operationService.calculateTotalByType(OperationCategory.INCOME, wallet.getOperations());
        double totalExpense = operationService.calculateTotalByType(OperationCategory.EXPENSE, wallet.getOperations());

        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("\nФинансовый отчет\n");
        reportBuilder.append("Баланс: ").append(wallet.getBalance()).append("\n");
        reportBuilder.append("Доходы: ").append(totalIncome).append("\n");
        reportBuilder.append("Расходы: ").append(totalExpense).append("\n");

        reportBuilder.append("\nБюджет и текущие расходы по категориям\n");
        for (Map.Entry<String, Double> entry : categoryBudgets.entrySet()) {
            String category = entry.getKey();
            double budget = entry.getValue();
            double currentExpense = operationService.getOperationsByCategory(category, wallet.getOperations()).stream()
                    .filter(t -> t.getType() == OperationCategory.EXPENSE)
                    .mapToDouble(Operation::getAmount)
                    .sum();
            reportBuilder.append("Категория: ").append(category)
                    .append(", Бюджет: ").append(budget)
                    .append(", Текущие расходы: ").append(currentExpense).append("\n");
        }

        return reportBuilder.toString();
    }

    public void setBudget(User user, String category, double limit) {
        Wallet wallet = user.getWallets().get(0);
        double currentExpense = operationService.getOperationsByCategory(category, wallet.getOperations()).stream()
                .filter(t -> t.getType() == OperationCategory.EXPENSE)
                .mapToDouble(Operation::getAmount)
                .sum();

        if (currentExpense > limit) {
            AlertManager.alertBudgetExceeded(category, limit, currentExpense);
            return;
        }

        categoryBudgets.put(category, limit);
        AlertManager.alertGeneral("бюджет для категории '" + category + "' установлен: " + limit);
    }

    private OperationCategory parseOperationType(String type) {
        if (type.equalsIgnoreCase("доход")) {
            return OperationCategory.INCOME;
        } else if (type.equalsIgnoreCase("расход")) {
            return OperationCategory.EXPENSE;
        } else {
            throw new IllegalArgumentException("Неизвестный тип транзакции: " + type);
        }
    }

    public static void saveUserData(User user) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user_data.dat"))) {
            oos.writeObject(user);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных: " + e.getMessage() + e.getCause());
        }
    }

    public static User loadUser() {
        User user = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user_data.dat"))) {
            user = (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при загрузке данных: " + e.getMessage());
        }
        return user;
    }

}
