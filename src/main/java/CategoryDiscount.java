import java.time.LocalDateTime;

public class CategoryDiscount extends AbstractDiscount {

    private final LocalDateTime endTime;

    public CategoryDiscount(Category category, double discountPercent, int durationHours) {
        super(category, discountPercent);
        this.endTime = LocalDateTime.now().plusHours(durationHours);
    }

    public CategoryDiscount(String id, Category category, double discountPercent, LocalDateTime createdAt, LocalDateTime endTime) {
        super(id, category, discountPercent, createdAt);
        this.endTime = endTime;
    }

    @Override
    protected boolean canBeApplied(Product product) {
        return LocalDateTime.now().isBefore(endTime) && product.getCategory() == this.category;
    }

    public boolean isActive() {
        return LocalDateTime.now().isBefore(endTime);
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        String status = isActive() ? "ACTIVE" : "EXPIRED";
        return String.format("CategoryDiscount{id='%s', category='%s', discount=%.1f%%, endTime=%s, status=%s}",
                id, category.getName(), discountPercent, endTime.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")), status);
    }
}