import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserCreation() {
        User user = new User("John Doe");
        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        assertEquals(0.0, user.getBalance());
        assertEquals(0.0, user.getCredit());
    }

    @Test
    public void testSetBalance() {
        User user = new User("Jane Doe");
        user.setBalance(100.0);
        assertEquals(100.0, user.getBalance());
    }

    @Test
    public void testSetCredit() {
        User user = new User("Jane Doe");
        user.setCredit(50.0);
        assertEquals(50.0, user.getCredit());
    }
}