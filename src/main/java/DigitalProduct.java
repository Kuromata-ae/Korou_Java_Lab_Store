import java.util.List;

public class DigitalProduct extends Product {

    // Конструктор 1: как Product(String id, String name, double price)
    public DigitalProduct(String id, String name, double price) {
        super(id, name, price);
    }

    // Конструктор 2: как Product(String id, String name, String description, double price, int quantity, Category category)
    public DigitalProduct(String id, String name, String description, double price, int quantity, Category category) {
        super(id, name, description, price, quantity, category);
    }

    // Переопределённые методы finalPrice для полиморфизма
    public double finalPrice() {
        return getPrice();
    }

    public double finalPrice(int qty) {
        if (qty <= 0) return 0.0;
        return getPrice() * qty;
    }

    public double finalPrice(int qty, PricePolicy policy) {
        if (qty <= 0) return 0.0;
        // BogoHalf неприменим к цифровым товарам - игнорируем
        if (policy instanceof BogoHalf) {
            return getPrice() * qty;
        }
        return policy.apply(this, qty);
    }

    public double finalPrice(int qty, List<PricePolicy> policies) {
        if (qty <= 0) return 0.0;
        double bestPrice = Double.MAX_VALUE;
        for (PricePolicy policy : policies) {
            if (policy.applicableTo(this)) {
                double price = finalPrice(qty, policy);
                bestPrice = Math.min(bestPrice, price);
            }
        }
        return bestPrice == Double.MAX_VALUE ? finalPrice(qty) : bestPrice;
    }
}

