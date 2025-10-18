import java.util.List;

public class PhysicalProduct extends Product {
    private double shippingCost = 100.0;

    // Конструктор 1: как Product(String id, String name, double price)
    public PhysicalProduct(String id, String name, double price) {
        super(id, name, price);
    }

    // Конструктор 2: как Product(String id, String name, String description, double price, int quantity, Category category)
    public PhysicalProduct(String id, String name, String description, double price, int quantity, Category category) {
        super(id, name, description, price, quantity, category);
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(double shippingCost) {
        this.shippingCost = Math.max(0, shippingCost);
    }

    // Переопределённые методы finalPrice для полиморфизма
    public double finalPrice() {
        return getPrice() + shippingCost;
    }

    public double finalPrice(int qty) {
        if (qty <= 0) return 0.0;
        return getPrice() * qty + shippingCost;
    }

    public double finalPrice(int qty, PricePolicy policy) {
        if (qty <= 0) return 0.0;
        double discountedPrice = policy.apply(this, qty);
        return discountedPrice + shippingCost; // Доставка добавляется после скидки
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
