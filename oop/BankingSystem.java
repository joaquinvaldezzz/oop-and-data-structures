package oop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a single transaction in the account
 */
class Transaction {
    private String type;
    private double amount;
    private Date date;
    private double balance;

    public Transaction(String type, double amount, double balance) {
        this.type = type;
        this.amount = amount;
        this.date = new Date();
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format("%s: $%.2f on %s (Balance: $%.2f)", type, amount, date, balance);
    }
}

/**
 * ENCAPSULATION: Base class that bundles account data and operations Protects sensitive data like
 * balance and transaction history
 */
class BankAccount {
    // Private fields for security (ENCAPSULATION)
    private double balance;
    private String accountNumber;
    private List<Transaction> transactionHistory;
    protected String accountHolder; // Protected for child class access

    /**
     * Constructor for BankAccount
     *
     * @param accountHolder  Name of the account holder
     * @param accountNumber  Unique account number
     * @param initialDeposit Initial deposit amount
     */
    public BankAccount(String accountHolder, String accountNumber, double initialDeposit) {
        this.accountHolder = accountHolder;
        this.accountNumber = accountNumber;
        this.balance = initialDeposit;
        this.transactionHistory = new ArrayList<>();
        this.recordTransaction("Initial Deposit", initialDeposit);
    }

    /**
     * Get current balance
     *
     * @return Current balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Get masked account number for security
     *
     * @return Masked account number
     */
    public String getAccountNumber() {
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }

    /**
     * Get copy of transaction history
     *
     * @return List of transactions
     */
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory); // Return copy
    }

    /**
     * Deposit money into account
     *
     * @param amount Amount to deposit
     * @return Success status
     */
    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive");
            return false;
        }

        balance += amount;
        recordTransaction("Deposit", amount);
        System.out.printf("Deposited $%.2f. New balance: $%.2f%n", amount, balance);
        return true;
    }

    /**
     * POLYMORPHISM: This method will be customized by child classes
     *
     * @param amount Amount to withdraw
     * @return Success status
     */
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive");
            return false;
        }

        if (amount > balance) {
            System.out.println("Insufficient funds");
            return false;
        }

        balance -= amount;
        recordTransaction("Withdrawal", amount);
        System.out.printf("Withdrew $%.2f. New balance: $%.2f%n", amount, balance);
        return true;
    }

    /**
     * Private method to record transactions (ENCAPSULATION)
     *
     * @param type   Transaction type
     * @param amount Transaction amount
     */
    private void recordTransaction(String type, double amount) {
        transactionHistory.add(new Transaction(type, amount, balance));
    }

    /**
     * POLYMORPHISM: Base implementation for monthly fees
     *
     * @return Monthly fee amount
     */
    public double calculateMonthlyFee() {
        return 5.0; // Base fee
    }

    /**
     * Get account summary
     *
     * @return Account summary string
     */
    public String getAccountSummary() {
        return String.format("Account: %s | Holder: %s | Balance: $%.2f", getAccountNumber(),
                accountHolder, balance);
    }
}

// INHERITANCE: SavingsAccount extends BankAccount
/**
 * Savings account with interest and withdrawal limits
 */
class SavingsAccount extends BankAccount {
    private double interestRate;
    private int withdrawalsThisMonth;
    private int maxWithdrawals;

    /**
     * Constructor for SavingsAccount
     *
     * @param accountHolder  Name of the account holder
     * @param accountNumber  Unique account number
     * @param initialDeposit Initial deposit amount
     * @param interestRate   Annual interest rate (e.g., 2.5 for 2.5%)
     */
    public SavingsAccount(String accountHolder, String accountNumber, double initialDeposit,
            double interestRate) {
        super(accountHolder, accountNumber, initialDeposit);
        this.interestRate = interestRate;
        this.withdrawalsThisMonth = 0;
        this.maxWithdrawals = 6; // Federal regulation limit
    }

    // POLYMORPHISM: Override withdraw to add withdrawal limits
    /**
     * Withdraw with monthly limit enforcement
     *
     * @param amount Amount to withdraw
     * @return Success status
     */
    @Override
    public boolean withdraw(double amount) {
        if (withdrawalsThisMonth >= maxWithdrawals) {
            System.out.printf("Withdrawal limit reached (%d per month)%n", maxWithdrawals);
            return false;
        }

        boolean success = super.withdraw(amount);
        if (success) {
            withdrawalsThisMonth++;
        }
        return success;
    }

    /**
     * Apply monthly interest to the account
     *
     * @return Interest earned
     */
    public double applyInterest() {
        double interest = (getBalance() * interestRate) / 100.0 / 12.0;
        deposit(interest);
        System.out.printf("Interest applied: $%.2f%n", interest);
        return interest;
    }

    // POLYMORPHISM: Override monthly fee calculation
    /**
     * Calculate monthly fee (waived if balance > $500)
     *
     * @return Monthly fee
     */
    @Override
    public double calculateMonthlyFee() {
        return getBalance() > 500 ? 0 : 3.0;
    }

    /**
     * Reset monthly withdrawal counter
     */
    public void resetMonthlyLimit() {
        withdrawalsThisMonth = 0;
    }
}

// INHERITANCE: CheckingAccount extends BankAccount
/**
 * Checking account with overdraft protection
 */
class CheckingAccount extends BankAccount {
    private double overdraftLimit;
    private boolean hasDebitCard;

    /**
     * Constructor for CheckingAccount
     *
     * @param accountHolder  Name of the account holder
     * @param accountNumber  Unique account number
     * @param initialDeposit Initial deposit amount
     * @param overdraftLimit Maximum overdraft allowed
     */
    public CheckingAccount(String accountHolder, String accountNumber, double initialDeposit,
            double overdraftLimit) {
        super(accountHolder, accountNumber, initialDeposit);
        this.overdraftLimit = overdraftLimit;
        this.hasDebitCard = true;
    }

    // POLYMORPHISM: Override withdraw to allow overdraft
    /**
     * Withdraw with overdraft protection
     *
     * @param amount Amount to withdraw
     * @return Success status
     */
    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive");
            return false;
        }

        double availableFunds = getBalance() + overdraftLimit;

        if (amount > availableFunds) {
            System.out.println("Amount exceeds available funds including overdraft");
            return false;
        }

        if (amount > getBalance()) {
            double overdraftUsed = amount - getBalance();
            System.out.printf("Using $%.2f overdraft protection%n", overdraftUsed);
        }

        return super.withdraw(amount);
    }

    /**
     * Issue a check
     *
     * @param payee  Check recipient
     * @param amount Check amount
     * @return Success status
     */
    public boolean writeCheck(String payee, double amount) {
        System.out.printf("Writing check to %s for $%.2f%n", payee, amount);
        return withdraw(amount);
    }

    // POLYMORPHISM: Different fee structure for checking
    /**
     * Calculate monthly fee (waived if balance > $1000)
     *
     * @return Monthly fee
     */
    @Override
    public double calculateMonthlyFee() {
        return getBalance() > 1000 ? 0 : 10.0;
    }
}

// ABSTRACTION: High-level banking operations without exposing complexity
/**
 * Bank manager that works with any account type
 */
class BankManager {
    /**
     * Process monthly maintenance for any account
     *
     * @param account Any bank account
     */
    public static void processMonthlyMaintenance(BankAccount account) {
        System.out.printf("%n--- Processing monthly maintenance for %s ---%n",
                account.accountHolder);

        double fee = account.calculateMonthlyFee();
        if (fee > 0) {
            account.withdraw(fee);
            System.out.printf("Monthly fee charged: $%.2f%n", fee);
        } else {
            System.out.println("Monthly fee waived");
        }

        // Apply interest if it's a savings account
        if (account instanceof SavingsAccount) {
            SavingsAccount savings = (SavingsAccount) account;
            savings.applyInterest();
            savings.resetMonthlyLimit();
        }

        System.out.println(account.getAccountSummary());
    }

    /**
     * Transfer money between accounts
     *
     * @param fromAccount Source account
     * @param toAccount   Destination account
     * @param amount      Amount to transfer
     * @return Success status
     */
    public static boolean transfer(BankAccount fromAccount, BankAccount toAccount, double amount) {
        System.out.printf("%nTransferring $%.2f from %s to %s%n", amount, fromAccount.accountHolder,
                toAccount.accountHolder);

        if (fromAccount.withdraw(amount)) {
            toAccount.deposit(amount);
            return true;
        }
        return false;
    }
}

// ============= MAIN CLASS WITH USAGE EXAMPLE =============
public class BankingSystem {
    public static void main(String[] args) {
        System.out.println("===== BANKING SYSTEM DEMO =====\n");

        // Create different account types
        SavingsAccount savings = new SavingsAccount("Alice Johnson", "1234567890", 1000, 2.5);
        CheckingAccount checking = new CheckingAccount("Bob Smith", "0987654321", 500, 200);

        // Perform various operations
        System.out.println("--- Initial Setup ---");
        System.out.println(savings.getAccountSummary());
        System.out.println(checking.getAccountSummary());

        System.out.println("\n--- Transactions ---");
        savings.deposit(500);
        checking.writeCheck("Electric Company", 150);

        System.out.println("\n--- Transfer Between Accounts ---");
        BankManager.transfer(savings, checking, 300);

        System.out.println("\n--- Withdrawal Limit Test (Savings) ---");
        for (int i = 1; i <= 7; i++) {
            System.out.printf("%nWithdrawal attempt %d:%n", i);
            savings.withdraw(50);
        }

        System.out.println("\n--- Overdraft Test (Checking) ---");
        checking.withdraw(600); // Should use overdraft

        // Monthly maintenance (POLYMORPHISM in action)
        BankManager.processMonthlyMaintenance(savings);
        BankManager.processMonthlyMaintenance(checking);

        System.out.println("\n--- Transaction History ---");
        System.out.printf("%s's transactions: %d transactions%n", savings.accountHolder,
                savings.getTransactionHistory().size());
    }
}
