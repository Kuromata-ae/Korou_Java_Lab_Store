public class PercentageOff implements PricePolicy {
    private final double percent; // Например: 15.0 = 15%

    public PercentageOff(double percent) {
        this.percent = Math.max(0, Math.min(90, percent)); // Защита от >90%
    }

    @Override
    public String name() {
        return "Percentage Off -" + percent + "%";
    }

    @Override
    public boolean applicableTo(Product product) {
        return true; // Для всех товаров
    }

    @Override
    public double apply(Product product, int qty) {
        if (qty <= 0) return 0.0;
        return product.getPrice() * (1.0 - percent / 100.0) * qty;
    }
}
