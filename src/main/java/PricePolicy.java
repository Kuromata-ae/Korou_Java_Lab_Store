public interface PricePolicy {
    String name();
    boolean applicableTo(Product product);
    double apply(Product product, int qty);
}
