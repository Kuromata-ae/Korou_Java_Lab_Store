import java.time.LocalDateTime;
import java.util.UUID;

public abstract class AbstractDiscount {

    protected final String id;
    protected final Category category;
    protected final double discountPercent;
    protected final LocalDateTime createdAt;

    public AbstractDiscount(Category category, double discountPercent) {
        this.id = "DISC-" + UUID.randomUUID().toString();
        this.category = category;
        this.discountPercent = discountPercent;
        this.createdAt = LocalDateTime.now();
    }

    public AbstractDiscount(String id, Category category, double discountPercent, LocalDateTime createdAt) {
        this.id = id;
        this.category = category;
        this.discountPercent = discountPercent;
        this.createdAt = createdAt;
    }

    public final double applyDiscount(Product product) {
        if (canBeApplied(product)) {
            return calculateDiscount(product);
        }
        return 0.0;
    }

    protected abstract boolean canBeApplied(Product product);

    private double calculateDiscount(Product product) {
        return product.getPrice() * (discountPercent / 100.0);
    }

    public String getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}