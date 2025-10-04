import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a simple category that groups products.  Each category has a
 * name and a list of products.  Products can be added or removed and the
 * list of products can be retrieved.  This class does not enforce any
 * particular relationship between categories and database identifiers; that
 * mapping is managed externally (for example in the Database class).
 */
public class Category {
    /** Name of the category. */
    private final String name;
    /** List of products assigned to this category. */
    private final List<Product> products;

    /** Constructs a category with the given name. */
    public Category(String name) {
        this.name = name;
        this.products = new ArrayList<>();
    }

    /** Returns the name of this category. */
    public String getName() {
        return name;
    }

    /** Returns an unmodifiable view of the products in this category. */
    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    /** Adds a product to this category. Null values are ignored. */
    public void addProduct(Product p) {
        if (p != null) {
            products.add(p);
        }
    }

    /** Removes the product with the given identifier from this category. */
    public void removeProductById(String id) {
        products.removeIf(p -> p.getId().equals(id));
    }

    @Override
    public String toString() {
        return name + products.toString();
    }
}