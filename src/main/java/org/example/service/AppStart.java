package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.User;

import java.util.Scanner;

@RequiredArgsConstructor
public class AppStart {

    public void start() {
        Scanner input = new Scanner(System.in);
        AuthenticationHandler authenticationHandler = new AuthenticationHandler();
        FinanceManager financeManager = new FinanceManager(new OperationService());

        User currentUser = null;

        while (true) {
            if (currentUser == null) {
                System.out.println("Введите номер команды:\n" +
                        "1. Регистрация\n" +
                        "2. Вход в систему\n" +
                        "3. Загрузить данные пользователей из файла\n" +
                        "4. Завершить работу программы");
                String command = input.nextLine();
                String login;
                String password;

                switch (command) {
                    case "1":
                        System.out.print("Введите логин: ");
                        login = input.nextLine().trim();
                        System.out.print("Введите пароль: ");
                        password = input.nextLine().trim();

                        try {
                            if (login.length() < 5 || password.length() < 5) {
                                System.out.println("Ошибка: Длина логина и пароля не менее 5 символов");
                                continue;
                            }

                            if (!password.matches("(?=.*[A-Za-z])(?=.*\\d).*")) {
                                System.out.println("Пароль должен содержать как буквы, так и цифры");
                                continue;
                            }

                            currentUser = authenticationHandler.register(login, password);
                            System.out.println("Регистрация завершена. Вы вошли в систему под логином " + login);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Ошибка: " + e.getMessage());
                        }
                        break;

                    case "2":
                        System.out.print("Введите логин: ");
                        login = input.nextLine().trim();
                        System.out.print("Введите пароль: ");
                        password = input.nextLine().trim();

                        try {
                            currentUser = authenticationHandler.login(login, password);
                            System.out.println(currentUser.getUsername() + " вошел в систему.");
                        } catch (IllegalArgumentException e) {
                            System.out.println("Ошибка: " + e.getMessage());
                        }
                        break;

                    case "3":
                        try {
                            FinanceManager.loadUser();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case "4":
                        System.out.println("Работа программы завершена");
                        return;

                    default:
                        System.out.println("Некорректный номер команды");
                }
            } else {
                System.out.println("Введите номер команды:\n" +
                        "1. Добавить доход/расход\n" +
                        "2. Показать отчет\n" +
                        "3. Установить бюджет\n" +
                        "4. Выйти из аккаунта\n" +
                        "5. Сохранить данные пользователя в файл\n" +
                        "6. Завершить работу программы");
                String command = input.nextLine();

                switch (command) {
                    case "1":
                        System.out.print("Введите тип (доход/расход): ");
                        String type = input.nextLine().trim();
                        System.out.print("Введите категорию: ");
                        String category = input.nextLine().trim();
                        System.out.print("Введите сумму: ");
                        double amount;
                        try {
                            amount = Double.parseDouble(input.nextLine().trim());
                            if (amount < 0) {
                                throw new IllegalArgumentException("Введите положительное число");
                            }

                        } catch (IllegalArgumentException e) {
                            System.out.println("Ошибка: " + e.getMessage());
                            continue;
                        }

                        try {
                            financeManager.addOperation(currentUser, type, category, amount);
                            System.out.println("Успешно.");
                        } catch (IllegalArgumentException e) {
                            System.out.println("Ошибка: " + e.getMessage());
                        }
                        break;

                    case "2":
                        System.out.println(financeManager.createReport(currentUser));
                        break;

                    case "3":
                        System.out.print("Введите категорию: ");
                        category = input.nextLine().trim();
                        System.out.print("Введите лимит бюджета: ");
                        double limit;
                        try {
                            limit = Double.parseDouble(input.nextLine().trim());
                            if (limit < 0) {
                                throw new IllegalArgumentException("Лимит бюджета должен быть положительным");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Ошибка: Введите корректное число");
                            continue;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Ошибка: " + e.getMessage());
                            continue;
                        }

                        try {
                            financeManager.setBudget(currentUser, category, limit);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Ошибка: " + e.getMessage());
                        }
                        break;

                    case "4":
                        currentUser = null;
                        System.out.println("Вы вышли из учетной записи");
                        break;

                    case "5":
                        try {
                            FinanceManager.saveUserData(currentUser);
                        } catch (Exception e) {
                            System.out.println("Ошибка при сохранении данных: " + e.getMessage());
                        }
                        System.out.println("Данные пользователя сохранены в файл");
                        break;

                    case "6":
                        System.out.println("Работа программы завершена");
                        return;

                    default:
                        System.out.println("Ошибка: Некорректный номер команды");
                }
            }
        }
    }
}
