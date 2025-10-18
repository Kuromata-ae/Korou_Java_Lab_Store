import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    @Test
    public void testTransactionCreation() {
        Transaction transaction = new Transaction(Transaction.TransactionType.DEPOSIT, 100.0, 1);
        assertNotNull(transaction);
        assertEquals(Transaction.TransactionType.DEPOSIT, transaction.getType());
        assertEquals(100.0, transaction.getAmount());
        assertEquals(1, transaction.getUserId());
        assertNotNull(transaction.getTimestamp());
    }
}