import java.time.LocalDateTime;

public class CategoryDiscount extends DiscountManager {
    private LocalDateTime endTime;

    // Основной конструктор для создания новых скидок
    public CategoryDiscount(Category category, double discountPercent, int durationHours) {
        super(category, discountPercent);
        this.endTime = LocalDateTime.now().plusHours(durationHours);
    }

    // Конструктор для загрузки из базы данных
    public CategoryDiscount(Category category, double discountPercent, LocalDateTime createdAt, LocalDateTime endTime) {
        super(category, discountPercent);
        this.createdAt = createdAt; // переопределяем время создания
        this.endTime = endTime;
    }

    @Override
    public boolean isActive() {
        return LocalDateTime.now().isBefore(endTime);
    }

    @Override
    public double calculateDiscount(Product product) {
        if (!isActive() || !product.getCategory().getName().equals(category.getName())) {
            return 0.0;
        }
        return product.getPrice() * (discountPercent / 100.0);
    }

    public LocalDateTime getEndTime() { return endTime; }

    @Override
    public String toString() {
        String status = isActive() ? "ACTIVE" : "EXPIRED";
        return super.toString() + String.format(", endTime=%s, status=%s",
                endTime.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")), status);
    }
}
