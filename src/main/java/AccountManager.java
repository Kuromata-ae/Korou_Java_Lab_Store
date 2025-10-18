public class AccountManager {
    private double balance = 0.0;

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.printf("Баланс пополнен на %.2f USDT. Текущий баланс: %.2f USDT%n", amount, balance);
        } else {
            System.out.println("Сумма пополнения должна быть положительной.");
        }
    }

    public boolean charge(double amount) {
        if (amount <= balance) {
            balance -= amount;
            System.out.printf("Списано %.2f USDT. Остаток баланса: %.2f USDT%n", amount, balance);
            return true;
        } else {
            System.out.println("Недостаточно средств. Пожалуйста, пополните баланс.");
            return false;
        }
    }
}
