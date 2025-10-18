import java.util.Objects;

public class User {
    private static int userCount = 0;
    private final int id;
    private final String name;
    private double balance;
    private double credit;

    public User(String name) {
        this.id = ++userCount;
        this.name = name;
        this.balance = 0.0;
        this.credit = 0.0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}