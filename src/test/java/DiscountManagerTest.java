import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountManagerTest {

    @Test
    public void testCategoryDiscountWithSameNameCategories() {
        // Create two different category instances with the same name
        Category category1 = new Category("Electronics");
        Category category2 = new Category("Electronics");

        // Create products for each category
        Product product1 = new PhysicalProduct("P001", "Laptop", "A powerful laptop", 1000.0, 1, category1);
        Product product2 = new PhysicalProduct("P002", "Headphones", "Noise-cancelling headphones", 100.0, 1, category2);

        // Create a discount for the first category only
        CategoryDiscount discount = new CategoryDiscount(category1, 10.0, 24);

        // Calculate discounts
        double discount1 = discount.calculateDiscount(product1);
        double discount2 = discount.calculateDiscount(product2);

        // Verify that the discount is applied only to the product in the correct category
        assertEquals(100.0, discount1, 0.001);
        assertEquals(0.0, discount2, 0.001);
    }

    @Test
    public void testPromoCodeWithSameNameCategories() {
        // Create two different category instances with the same name
        Category category1 = new Category("Books");
        Category category2 = new Category("Books");

        // Create products for each category
        Product product1 = new PhysicalProduct("P003", "Java Programming", "A book on Java", 50.0, 1, category1);
        Product product2 = new PhysicalProduct("P004", "Python Programming", "A book on Python", 60.0, 1, category2);

        // Create a promo code for the first category only
        PromoCode promoCode = new PromoCode(category1, 20.0, "SAVE20");

        // Calculate discounts
        double discount1 = promoCode.calculateDiscount(product1);
        double discount2 = promoCode.calculateDiscount(product2);

        // Verify that the discount is applied only to the product in the correct category
        assertEquals(10.0, discount1, 0.001);
        assertEquals(0.0, discount2, 0.001);

        // Test the usePromoCode method
        boolean used1 = promoCode.usePromoCode(product1);
        boolean used2 = promoCode.usePromoCode(product2);

        // Verify that the promo code can only be used for the correct category
        assertEquals(true, used1);
        assertEquals(false, used2);
    }
}