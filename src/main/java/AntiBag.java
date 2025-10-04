import java.util.HashSet;
import java.util.Set;

/**
 * Utility class that keeps track of all product identifiers to prevent
 * duplicates across categories.  It can also find and remove products from
 * categories by identifier.  Note that this class operates only on
 * in‑memory collections; persistence is handled separately by the
 * Database class.
 */
public final class AntiBag {
    /**
     * Set of all registered product ids.  Used to ensure ids are unique
     * when adding new products.
     */
    private static final Set<String> PRODUCT_IDS = new HashSet<>();

    private AntiBag() {}

    /**
     * Registers a product id.  Returns true if the id has not been seen
     * before and was successfully registered; false if the id is already
     * registered.  Null ids are ignored.
     */
    public static boolean registerProduct(Product p) {
        if (p == null || p.getId() == null) return false;
        return PRODUCT_IDS.add(p.getId());
    }

    /**
     * Unregisters a product id.  This should be called when a product is
     * removed from all categories so that its id may be reused in future.
     */
    public static void unregisterProduct(Product p) {
        if (p != null && p.getId() != null) {
            PRODUCT_IDS.remove(p.getId());
        }
    }

    /**
     * Finds a product with the given identifier in the supplied categories.
     * Returns the first matching product or null if not found.
     */
    public static Product findProductById(String id, Category... categories) {
        if (id == null) return null;
        for (Category c : categories) {
            for (Product p : c.getProducts()) {
                if (id.equals(p.getId())) {
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * Removes the product with the given identifier from the supplied
     * categories and unregisters its id.  Returns true if a product was
     * removed; false otherwise.
     */
    public static boolean removeProductFromCategories(String id, Category... categories) {
        if (id == null) return false;
        boolean removed = false;
        for (Category c : categories) {
            for (Product p : new java.util.ArrayList<>(c.getProducts())) {
                if (id.equals(p.getId())) {
                    c.removeProductById(id);
                    unregisterProduct(p);
                    removed = true;
                }
            }
        }
        return removed;
    }
}