import exceptions.*;
import model.BankAccount;
import model.CheckingAccount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientInterface {

    private final List<BankAccount> accounts = new ArrayList<>();

    private final Scanner scanner = new Scanner(System.in);

    private final List<String> options = List.of(
            "1 - Create account",
            "2 - Delete account",
            "3 - Make account deposit",
            "4 - Make account withdrawal",
            "5 - Print account info",
            "6 - Exit"
    );

    public void start() throws IOException {
        while (true) {
            printMainMenu();
            String option = scanner.next();

            try {
                int choice = Integer.parseInt(option);
                switch (choice) {
                    case 1:
                        addNewAccount();
                        break;
                    case 2:
                        deleteAccount();
                        break;
                    case 3:
                        deposit();
                        break;
                    case 4:
                        withdraw();
                        break;
                    case 5:
                        printInfo();
                        break;
                    case 6:
                        return;
                    default:
                        System.err.println("Error: Please enter a valid choice (1 thru 6)\n");
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: Invalid numeric input.\n");
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage() + "\n");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n****************");
        System.out.println("Banking system: ");
        System.out.println("****************");
        options.forEach(System.out::println);
        System.out.print("\n>>> Choose your option: ");
    }

    private void addNewAccount() {
        System.out.println("\n****************");
        System.out.println("Creating new account: ");
        System.out.println("****************");

        System.out.print(">>> Enter customer name: ");
        String name = scanner.next();
        System.out.print(">>> Enter user id: ");
        Long userId = parseUserId(scanner.next());
        System.out.print(">>> Enter user password: ");
        String password = scanner.next();
        System.out.print(">>> Confirm password: ");
        String confirmPassword = scanner.next();

        for (BankAccount acc : accounts) {
            if (acc.getUserId().equals(userId)) {
                throw new DuplicateUserIdException("Акаунт з userId " + userId + " вже існує");
            }
        }

        validatePassword(password);

        if (!password.equals(confirmPassword)) {
            throw new InvalidPasswordException("Пароль та підтвердження паролю не збігаються");
        }

        String accountNumber = "ACC-" + userId;
        CheckingAccount newAccount = new CheckingAccount(name, accountNumber, userId, password);
        accounts.add(newAccount);

        System.out.println("\nAccount created successfully!");
    }

    private void deleteAccount() {
        System.out.println("\n****************");
        System.out.println("Deleting account: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        Long userId = parseUserId(scanner.next());
        System.out.print(">>> Enter user password: ");
        String password = scanner.next();

        BankAccount account = authenticateAccount(userId, password);

        if (account.getAccountBalance() != 0) {
            throw new NonZeroBalanceException(
                    "Неможливо видалити рахунок з ненульовим балансом: " + account.getAccountBalance());
        }

        accounts.remove(account);
        System.out.println("\nAccount deleted successfully!");
    }

    private void deposit() {
        System.out.println("\n****************");
        System.out.println("Making a deposit: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        Long userId = parseUserId(scanner.next());
        System.out.print(">>> Enter user password: ");
        String password = scanner.next();
        System.out.print(">>> Enter amount: ");
        double amount = parseAmount(scanner.next());

        BankAccount account = authenticateAccount(userId, password);
        account.deposit(amount);

        System.out.println("\nDeposit completed successfully! Current balance: " + account.getAccountBalance());
    }

    private void withdraw() {
        System.out.println("\n****************");
        System.out.println("Making a withdrawal: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        Long userId = parseUserId(scanner.next());
        System.out.print(">>> Enter user password: ");
        String password = scanner.next();
        System.out.print(">>> Enter amount: ");
        double amount = parseAmount(scanner.next());

        BankAccount account = authenticateAccount(userId, password);
        account.withdraw(amount);

        System.out.println("\nWithdrawal completed successfully! Current balance: " + account.getAccountBalance());
    }

    private void printInfo() {
        System.out.println("\n****************");
        System.out.println("Printing account info: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        Long userId = parseUserId(scanner.next());
        System.out.print(">>> Enter user password: ");
        String password = scanner.next();

        BankAccount account = authenticateAccount(userId, password);

        System.out.println("\nAccount info: ");
        System.out.println(" - customer name: " + account.getCustomerName());
        System.out.println(" - account balance: " + account.getAccountBalance());
        System.out.println(" - account type: Checking");
        if (account instanceof CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) account;
            System.out.println(" - account daily withdrawal limit: " + checkingAccount.getDailyWithdrawalLimit());
        }

        System.out.print("\nPress Enter to return to main menu...");
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Long parseUserId(String input) {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new AccountException("userId повинен бути цілим числом");
        }
    }

    private double parseAmount(String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("Сума повинна бути коректним числом");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new InvalidPasswordException("Пароль повинен містити щонайменше 8 символів");
        }
        if (!password.matches(".*\\d.*")) {
            throw new InvalidPasswordException("Пароль повинен містити щонайменше одну цифру");
        }
    }

    private BankAccount authenticateAccount(Long userId, String password) {
        for (BankAccount account : accounts) {
            if (account.getUserId().equals(userId)) {
                if (!account.getUserPassword().equals(password)) {
                    throw new InvalidPasswordException("Невірний пароль для userId: " + userId);
                }
                return account;
            }
        }
        throw new AccountNotFoundException("Рахунок з userId " + userId + " не знайдено");
    }
}