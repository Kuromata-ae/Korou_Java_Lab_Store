import java.time.LocalDateTime;

public class PromoCode extends DiscountManager {
    private String code;
    private boolean used;

    // Конструктор для создания новых промокодов
    public PromoCode(Category category, double discountPercent, String code) {
        super(category, discountPercent);
        this.code = code.toUpperCase();
        this.used = false;
    }

    // Конструктор для загрузки из базы данных
    public PromoCode(Category category,
                     double discountPercent,
                     String code,
                     boolean used,
                     LocalDateTime createdAt) {
        super(category, discountPercent);
        this.code = code.toUpperCase();
        this.used = used;
        this.createdAt = createdAt; // переопределяем время создания
    }

    @Override
    public boolean isActive() {
        return !used;
    }

    @Override
    public double calculateDiscount(Product product) {
        if (used || !product.getCategory().getName().equals(category.getName())) {
            return 0.0;
        }
        return product.getPrice() * (discountPercent / 100.0);
    }

    public boolean usePromoCode(Product product) {
        if (isActive() && product.getCategory().getName().equals(category.getName())) {
            used = true;
            return true;
        }
        return false;
    }

    public String getCode() { return code; }
    public boolean isUsed() { return used; }

    @Override
    public String toString() {
        String status = used ? "USED" : "NEW";
        return super.toString() + String.format(", code='%s', status=%s", code, status);
    }
}

