package org.example.util;

public class AlertManager {

    public static void alertGeneral(String message) {
        System.out.println("Уведомление: " + message);
    }

    public static void alertBudgetExceeded(String category, double limit, double currentAmount) {
        System.out.printf("Расходы по категории '%s' превышают лимит %.2f. Текущие расходы составляют %.2f.", category, limit, currentAmount);
    }

    public static void alertLowBalance(double balance) {
        System.out.println("Внимание: низкий баланс. Текущий баланс: " + balance);
    }
}