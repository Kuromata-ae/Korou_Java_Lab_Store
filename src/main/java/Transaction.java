import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class Transaction {
    private static final AtomicLong idGenerator = new AtomicLong(0);

    private final long id;
    private final TransactionType type;
    private final double amount;
    private final int userId;
    private final LocalDateTime timestamp;

    public enum TransactionType {
        DEPOSIT,
        PURCHASE,
        CREDIT_REQUEST,
        CREDIT_REPAYMENT
    }

    public Transaction(TransactionType type, double amount, int userId) {
        this.id = idGenerator.incrementAndGet();
        this.type = type;
        this.amount = amount;
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", userId=" + userId +
                ", timestamp=" + timestamp +
                '}';
    }
}