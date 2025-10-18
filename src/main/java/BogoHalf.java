public class BogoHalf implements PricePolicy {
    @Override
    public String name() {
        return "Buy One Get One Half Off";
    }

    @Override
    public boolean applicableTo(Product product) {
        // Только для физических товаров — если будет PhysicalProduct: product instanceof PhysicalProduct
        return true;
    }

    @Override
    public double apply(Product product, int qty) {
        if (qty <= 0) return 0.0;
        int pairs = qty / 2;
        int rest = qty % 2;
        return pairs * product.getPrice() * 1.5 + rest * product.getPrice();
    }
}
