public class FixedOff implements PricePolicy {
    private final double amount; // Например, 100.0

    public FixedOff(double amount) {
        this.amount = Math.max(0, amount);
    }

    @Override
    public String name() {
        return "Fixed Off -" + amount + " currency";
    }

    @Override
    public boolean applicableTo(Product product) {
        return true;
    }

    @Override
    public double apply(Product product, int qty) {
        if (qty <= 0) return 0.0;
        double p = Math.max(0, product.getPrice() - amount);
        return p * qty;
    }
}
