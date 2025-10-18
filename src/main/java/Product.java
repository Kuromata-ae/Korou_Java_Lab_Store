import java.util.Objects;
import java.util.List;

public class Product {

    public static final String DEFAULT_CURRENCY = "USDT";

    private static int SEQ = 1;
    private static int createdCount = 0;

    private String id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private Category category;

    private static String nextSeq() {
        return String.valueOf(SEQ++);
    }

    public static int getCreatedCount() {
        return createdCount;
    }

    // ---------------- Guard methods ----------------

    private boolean trySetId(String id) {
        if (id != null && !id.isBlank()) {
            this.id = id;
            return true;
        }
        return false;
    }

    private boolean trySetName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
            return true;
        }
        return false;
    }

    private boolean trySetDescription(String description) {
        if (description == null || description.isBlank()) {
            this.description = null;
        } else {
            this.description = description;
        }
        return true;
    }

    private boolean trySetPrice(double price) {
        if (price >= 0.0) {
            this.price = price;
            return true;
        }
        return false;
    }

    private boolean trySetQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
            return true;
        }
        return false;
    }

    private boolean trySetCategory(Category category) {
        this.category = category;
        return true;
    }

    // ---------------- Constructors ----------------

    public Product() {
        this.id = "AUTO-" + nextSeq();
        this.name = "Unnamed";
        this.description = null;
        this.price = 0.0;
        this.quantity = 0;
        this.category = null;
        createdCount++;
    }

    public Product(String id, String name, double price) {
        this(); // chain to no-args constructor
        trySetId(id);
        trySetName(name);
        trySetPrice(price);
    }

    public Product(String id, String name, String description,
                   double price, int quantity, Category category) {
        this(); // initialise default values
        trySetId(id);
        trySetName(name);
        trySetDescription(description);
        trySetPrice(price);
        trySetQuantity(quantity);
        trySetCategory(category);
    }

    // ---------------- Static factory methods ----------------

    public static Product of(String id, String name, double price) {
        return new Product(id, name, price);
    }

    public static Product freeSample(String name) {
        Product p = new Product();
        p.trySetName(name);
        p.trySetPrice(0.0);
        p.trySetQuantity(1);
        return p;
    }

    // ---------------- Getters ----------------

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Category getCategory() {
        return category;
    }

    // ---------------- Final Price Methods (POLYMORPHISM) ----------------

    public double finalPrice() {
        return price;
    }

    public double finalPrice(int qty) {
        if (qty <= 0) return 0.0;
        return price * qty;
    }

    public double finalPrice(int qty, PricePolicy policy) {
        if (qty <= 0) return 0.0;
        return policy.apply(this, qty);
    }

    public double finalPrice(int qty, List<PricePolicy> policies) {
        if (qty <= 0) return 0.0;
        double bestPrice = Double.MAX_VALUE;
        for (PricePolicy policy : policies) {
            if (policy.applicableTo(this)) {
                double price = policy.apply(this, qty);
                bestPrice = Math.min(bestPrice, price);
            }
        }
        return bestPrice == Double.MAX_VALUE ? finalPrice(qty) : bestPrice;
    }

    // ---------------- String representation ----------------

    @Override
    public String toString() {
        String catName = (category == null ? "null" : category.getName());
        return String.format("Product{id='%s', name='%s', description='%s', price=%.2f %s, quantity=%d, category=%s}",
                id, name, description, price, DEFAULT_CURRENCY, quantity, catName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
