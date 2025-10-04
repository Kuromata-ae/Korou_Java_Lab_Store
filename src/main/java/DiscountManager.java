import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DiscountManager {
    protected static int idSequence = 1;
    protected String id;
    protected Category category;
    protected double discountPercent;
    protected LocalDateTime createdAt;

    protected static String generateId() {
        return "DISC-" + (idSequence++);
    }

    public DiscountManager(Category category, double discountPercent) {
        this.id = generateId();
        this.category = category;
        this.discountPercent = Math.max(0, Math.min(100, discountPercent));
        this.createdAt = LocalDateTime.now();
    }

    public abstract boolean isActive();
    public abstract double calculateDiscount(Product product);

    public String getId() { return id; }
    public Category getCategory() { return category; }
    public double getDiscountPercent() { return discountPercent; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return String.format("%s{id='%s', category='%s', discount=%.1f%%, created=%s}",
                getClass().getSimpleName(), id, category.getName(),
                discountPercent, createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
    }
}
