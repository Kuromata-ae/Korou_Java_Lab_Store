import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccountManagerTest {

    private AccountManager accountManager;
    private User user;

    @BeforeEach
    public void setUp() {
        accountManager = new AccountManager();
        user = new User("Test User");
    }

    @Test
    public void testDeposit() {
        accountManager.deposit(user, 100.0);
        assertEquals(100.0, accountManager.getBalance(user));
        assertEquals(1, accountManager.getTransactions().size());
        assertEquals(Transaction.TransactionType.DEPOSIT, accountManager.getTransactions().get(0).getType());
    }

    @Test
    public void testDepositInvalidAmount() {
        accountManager.deposit(user, -50.0);
        assertEquals(0.0, accountManager.getBalance(user));
        assertTrue(accountManager.getTransactions().isEmpty());
    }

    @Test
    public void testPurchaseSuccess() {
        accountManager.deposit(user, 100.0);
        boolean success = accountManager.purchase(user, 50.0);
        assertTrue(success);
        assertEquals(50.0, accountManager.getBalance(user));
        assertEquals(2, accountManager.getTransactions().size());
        assertEquals(Transaction.TransactionType.PURCHASE, accountManager.getTransactions().get(1).getType());
    }

    @Test
    public void testPurchaseInsufficientFunds() {
        accountManager.deposit(user, 20.0);
        boolean success = accountManager.purchase(user, 50.0);
        assertFalse(success);
        assertEquals(20.0, accountManager.getBalance(user));
        assertEquals(1, accountManager.getTransactions().size());
    }

    @Test
    public void testRequestCredit() {
        accountManager.requestCredit(user, 200.0);
        assertEquals(200.0, accountManager.getBalance(user));
        assertEquals(200.0, accountManager.getCredit(user));
        assertEquals(1, accountManager.getTransactions().size());
        assertEquals(Transaction.TransactionType.CREDIT_REQUEST, accountManager.getTransactions().get(0).getType());
    }

    @Test
    public void testRepayCreditSuccess() {
        accountManager.requestCredit(user, 200.0);
        accountManager.repayCredit(user, 100.0);
        assertEquals(100.0, accountManager.getBalance(user));
        assertEquals(100.0, accountManager.getCredit(user));
        assertEquals(2, accountManager.getTransactions().size());
        assertEquals(Transaction.TransactionType.CREDIT_REPAYMENT, accountManager.getTransactions().get(1).getType());
    }

    @Test
    public void testRepayCreditInsufficientFunds() {
        accountManager.requestCredit(user, 200.0);
        user.setBalance(50);
        accountManager.repayCredit(user, 100.0);
        assertEquals(50.0, accountManager.getBalance(user));
        assertEquals(200.0, accountManager.getCredit(user));
        assertEquals(1, accountManager.getTransactions().size());
    }

    @Test
    public void testRepayCreditExceedsDebt() {
        accountManager.requestCredit(user, 100.0);
        accountManager.repayCredit(user, 150.0);
        assertEquals(100.0, accountManager.getBalance(user));
        assertEquals(100.0, accountManager.getCredit(user));
        assertEquals(1, accountManager.getTransactions().size());
    }
}