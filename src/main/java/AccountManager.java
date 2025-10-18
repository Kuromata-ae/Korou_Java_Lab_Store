import java.util.ArrayList;
import java.util.List;

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

    public boolean purchase(User user, double amount) {
        if (amount <= 0) {
            System.out.println("Сумма покупки должна быть положительной.");
            return false;
        }
        if (user.getBalance() < amount) {
            System.out.println("Недостаточно средств. Пожалуйста, пополните баланс.");
            return false;
        }
        user.setBalance(user.getBalance() - amount);
        transactions.add(new Transaction(Transaction.TransactionType.PURCHASE, amount, user.getId()));
        System.out.printf("Списано %.2f USDT. Остаток баланса: %.2f USDT%n", amount, user.getBalance());
        return true;
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
}