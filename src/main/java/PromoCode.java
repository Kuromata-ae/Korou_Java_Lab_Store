import java.time.LocalDateTime;

public class PromoCode extends AbstractDiscount {

    private final String code;
    private boolean used;

    public PromoCode(Category category, double discountPercent, String code) {
        super(category, discountPercent);
        this.code = code.toUpperCase();
        this.used = false;
    }

    public PromoCode(String id, Category category, double discountPercent, String code, boolean used, LocalDateTime createdAt) {
        super(id, category, discountPercent, createdAt);
        this.code = code.toUpperCase();
        this.used = used;
    }

    @Override
    protected boolean canBeApplied(Product product) {
        return !used && product.getCategory() == this.category;
    }

    public void use() {
        if (!used) {
            this.used = true;
        }
    }

    public String getCode() {
        return code;
    }

    public boolean isUsed() {
        return used;
    }

    @Override
    public String toString() {
        String status = used ? "USED" : "NEW";
        return String.format("PromoCode{id='%s', category='%s', discount=%.1f%%, code='%s', status=%s}",
                id, category.getName(), discountPercent, code, status);
    }
}