package model;

import exceptions.DailyLimitExceededException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;

public class CheckingAccount extends BankAccount {
    private double dailyWithdrawalLimit = 300.0;

    public CheckingAccount(String customerName, String accountNumber, Long userId, String userPassword) {
        super(customerName, accountNumber, userId, userPassword);
    }

    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public void setDailyWithdrawalLimit(double dailyWithdrawalLimit) {
        if (dailyWithdrawalLimit <= 0) {
            throw new InvalidAmountException("Денний ліміт повинен бути більше 0");
        }
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    @Override
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Сума внеску повинна бути більше 0");
        }
        setAccountBalance(getAccountBalance() + amount);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Сума зняття повинна бути більше 0");
        }
        if (amount > dailyWithdrawalLimit) {
            throw new DailyLimitExceededException(
                    "Сума зняття (" + amount + ") перевищує денний ліміт (" + dailyWithdrawalLimit + ")");
        }
        if (amount > getAccountBalance()) {
            throw new InsufficientFundsException(
                    "Недостатньо коштів на рахунку. Поточний баланс: " + getAccountBalance());
        }
        setAccountBalance(getAccountBalance() - amount);
    }
}