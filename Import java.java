import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Account {
    public long accountNumber;
    public String holderName;
    public double balance;

    public Account(long accountNumber, String holderName, double balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }
}

interface IBankingSystem {
    void createAccount(Account account);
    void updateAccountName(long accountNumber, String newHolderName);
    void deleteAccount(long accountNumber);
    void deposit(long accountNumber, double amount);
    void withdraw(long accountNumber, double amount);
    void printAllAccountSummariesByHolderName(String holderName);
}

class BankingSystem implements IBankingSystem {
    private List<Account> accountList = new ArrayList<>();

    public void printAllAccountsSummary() {
        for (Account account : accountList) {
            printAccountSummary(account);
        }
    }

    private void printAccountSummary(Account account) {
        String summary = String.format("{accountNumber: %d, holderName: %s, balance: %.2f}", 
                                         account.accountNumber, account.holderName, account.balance);
        printMessage(summary);
    }

    private void printMessage(String message) {  // Correctly defined here
        System.out.println(message);
    }

    public void printAccountSummary(long accountNumber) {
        Account account = findAccount(accountNumber);
        if (account != null) {
            printAccountSummary(account);
        } else {
            printMessage("ACCOUNT_NOT_FOUND");
        }
    }

    private Account findAccount(long accountNumber) {
        for (Account account : accountList) {
            if (account.accountNumber == accountNumber) {
                return account;
            }
        }
        return null;
    }

    @Override
    public void createAccount(Account account) {
        accountList.add(account);
    }

    @Override
    public void updateAccountName(long accountNumber, String newHolderName) {
        Account account = findAccount(accountNumber);
        if (account != null) {
            account.holderName = newHolderName;
        } else {
            printMessage("ACCOUNT_NOT_FOUND");
        }
    }

    @Override
    public void deleteAccount(long accountNumber) {
        Account account = findAccount(accountNumber);
        if (account != null) {
            accountList.remove(account);
            printMessage("ACCOUNT_DELETED");
        } else {
            printMessage("ACCOUNT_NOT_FOUND");
        }
    }

    @Override
    public void deposit(long accountNumber, double amount) {
        Account account = findAccount(accountNumber);
        if (account != null) {
            account.balance += amount;
        } else {
            printMessage("ACCOUNT_NOT_FOUND");
        }
    }

    @Override
    public void withdraw(long accountNumber, double amount) {
        Account account = findAccount(accountNumber);
        if (account != null) {
            if (account.balance >= amount) {
                account.balance -= amount;  // Subtracting amount
            } else {
                printMessage("INSUFFICIENT_FUNDS");
            }
        } else {
            printMessage("ACCOUNT_NOT_FOUND");
        }
    }

    @Override
    public void printAllAccountSummariesByHolderName(String holderName) {
        for (Account account : accountList) {
            if (account.holderName.equals(holderName)) {
                printAccountSummary(account);
            }
        }
    }
}

class Solution {
    public static void main(String[] args) throws IOException {
        BankingSystem bank = new BankingSystem();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int operationCount = Integer.parseInt(bufferedReader.readLine().replaceAll("\\s+$", "").split("=")[1].trim());
        bufferedReader.readLine();
        IntStream.range(0, operationCount).forEach(opCountItr -> {
            try {
                List<String> theInput = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(","))
                        .collect(toList());
                String action = theInput.get(0);
                String arg1 = theInput.size() > 1 ? theInput.get(1).trim() : null;
                String arg2 = theInput.size() > 2 ? theInput.get(2).trim() : null;
                String arg3 = theInput.size() > 3 ? theInput.get(3).trim() : null;
                processInputs(bank, action, arg1, arg2, arg3);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        });
        bufferedReader.close();
    }

    private static void processInputs(BankingSystem bank, String action, String arg1, String arg2, String arg3) {
        long accountNumber;
        String holderName;
        double amount;
        switch (action) {
            case "createAccount":
                accountNumber = Long.parseLong(arg1);
                holderName = arg2;
                amount = Double.parseDouble(arg3);
                Account account = new Account(accountNumber, holderName, amount);
                bank.createAccount(account);
                break;

            case "deleteAccount":
                accountNumber = Long.parseLong(arg1);
                bank.deleteAccount(accountNumber);
                break;

            case "deposit":
                accountNumber = Long.parseLong(arg1);
                amount = Double.parseDouble(arg2);
                bank.deposit(accountNumber, amount);
                break;

            case "printAllAccountsSummary":
                bank.printAllAccountsSummary();
                break;

            case "printAllAccountSummariesByHolderName":
                holderName = arg1;
                bank.printAllAccountSummariesByHolderName(holderName);
                break;

            case "printAccountSummary":
                accountNumber = Long.parseLong(arg1);
                bank.printAccountSummary(accountNumber);
                break;

            case "updateAccountName":
                accountNumber = Long.parseLong(arg1);
                holderName = arg2;
                bank.updateAccountName(accountNumber, holderName);
                break;

            case "withdraw":
                accountNumber = Long.parseLong(arg1);
                amount = Double.parseDouble(arg2);
                bank.withdraw(accountNumber, amount);
                break;

            default:
                throw new IllegalArgumentException("No known action name was provided.");
        }
    }
}
