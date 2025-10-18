import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountManager {
    private final List<Transaction> transactions = new ArrayList<>();

    public double getBalance(User user) {
        return user.getBalance();
    }

    public double getCredit(User user) {
        return user.getCredit();
    }

    public void deposit(User user, double amount) {
        if (amount <= 0) {
            System.out.println("Сумма пополнения должна быть положительной.");
            return;
        }
        user.setBalance(user.getBalance() + amount);
        transactions.add(new Transaction(Transaction.TransactionType.DEPOSIT, amount, user.getId()));
        System.out.printf("Баланс пополнен на %.2f USDT. Текущий баланс: %.2f USDT%n", amount, user.getBalance());
    }

    public void requestCredit(User user, double amount) {
        if (amount <= 0) {
            System.out.println("Сумма кредита должна быть положительной.");
            return;
        }
        user.setCredit(user.getCredit() + amount);
        user.setBalance(user.getBalance() + amount);
        transactions.add(new Transaction(Transaction.TransactionType.CREDIT_REQUEST, amount, user.getId()));
        System.out.printf("Кредит на %.2f USDT одобрен. Текущий баланс: %.2f USDT, задолженность: %.2f USDT%n", amount, user.getBalance(), user.getCredit());
    }

    public void repayCredit(User user, double amount) {
        if (amount <= 0) {
            System.out.println("Сумма погашения должна быть положительной.");
            return;
        }
        if (user.getBalance() < amount) {
            System.out.println("Недостаточно средств для погашения кредита.");
            return;
        }
        if (amount > user.getCredit()) {
            System.out.println("Сумма погашения превышает задолженность.");
            return;
        }
        user.setBalance(user.getBalance() - amount);
        user.setCredit(user.getCredit() - amount);
        transactions.add(new Transaction(Transaction.TransactionType.CREDIT_REPAYMENT, amount, user.getId()));
        System.out.printf("Кредит погашен на %.2f USDT. Остаток задолженности: %.2f USDT%n", amount, user.getCredit());
    }

    public boolean purchase(User user, double amount, Scanner scanner) {
        if (amount <= 0) {
            System.out.println("Purchase amount must be positive.");
            return false;
        }

        if (user.getBalance() >= amount) {
            user.setBalance(user.getBalance() - amount);
            transactions.add(new Transaction(Transaction.TransactionType.PURCHASE, amount, user.getId()));
            System.out.printf("Debited %.2f USDT. Remaining balance: %.2f USDT%n", amount, user.getBalance());
            return true;
        } else {
            double needed = amount - user.getBalance();
            System.out.printf("Insufficient funds. You need %.2f more USDT. Use credit? (yes/no): ", needed);
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("yes")) {
                double creditToUse = Math.ceil(needed);
                user.setBalance(user.getBalance() + creditToUse);
                user.setCredit(user.getCredit() + creditToUse);
                transactions.add(new Transaction(Transaction.TransactionType.CREDIT_REQUEST, creditToUse, user.getId()));

                user.setBalance(user.getBalance() - amount);
                transactions.add(new Transaction(Transaction.TransactionType.PURCHASE, amount, user.getId()));
                System.out.printf("Credit of %.2f USDT approved. Debited %.2f USDT. Remaining balance: %.2f USDT, Outstanding credit: %.2f USDT%n",
                        creditToUse, amount, user.getBalance(), user.getCredit());
                return true;
            } else {
                System.out.println("Purchase cancelled by user.");
                return false;
            }
        }
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
}