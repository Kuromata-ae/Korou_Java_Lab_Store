import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class DiscountTest {

    private Category electronics;
    private Product laptop;
    private TaxService taxService;

    @BeforeEach
    public void setUp() {
        electronics = new Category("Electronics");
        laptop = new Product("L001", "Laptop", "A powerful laptop", 1000.0, 10, electronics);
        taxService = new VatService();
    }

    @Test
    public void testCategoryDiscount() {
        CategoryDiscount discount = new CategoryDiscount(electronics, 10.0, 1);
        double discountAmount = discount.applyDiscount(laptop);
        assertEquals(100.0, discountAmount, 0.001);
    }

    @Test
    public void testExpiredCategoryDiscount() {
        CategoryDiscount discount = new CategoryDiscount(electronics, 10.0, -1);
        double discountAmount = discount.applyDiscount(laptop);
        assertEquals(0.0, discountAmount, 0.001);
    }

    @Test
    public void testPromoCode() {
        PromoCode promoCode = new PromoCode(electronics, 20.0, "SALE20");
        double discountAmount = promoCode.applyDiscount(laptop);
        assertEquals(200.0, discountAmount, 0.001);
    }

    @Test
    public void testUsedPromoCode() {
        PromoCode promoCode = new PromoCode(electronics, 20.0, "SALE20");
        promoCode.use();
        double discountAmount = promoCode.applyDiscount(laptop);
        assertEquals(0.0, discountAmount, 0.001);
    }

    @Test
    public void testProductFinalPriceWithCategoryDiscount() {
        List<CategoryDiscount> discounts = new ArrayList<>();
        discounts.add(new CategoryDiscount(electronics, 10.0, 1));
        double finalPrice = laptop.finalPrice(discounts);
        assertEquals(900.0, finalPrice, 0.001);
    }

    @Test
    public void testVatCalculation() {
        double price = 100.0;
        double expectedTax = 20.0;
        double actualTax = taxService.calculateTax(price);
        assertEquals(expectedTax, actualTax, 0.001);
    }
}