import java.sql.*;

public class Database {

    private static final String DB_URL =
            "jdbc:h2:file:./korou_db;MODE=PostgreSQL;DATABASE_TO_UPPER=false;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private Database() {}

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    public static void initDatabase() {
        try (Connection c = getConnection(); Statement st = c.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS categories (" +
                    "id IDENTITY PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL UNIQUE" +
                    ")");
            st.execute("CREATE TABLE IF NOT EXISTS products (" +
                    "id VARCHAR(50) PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "price DOUBLE NOT NULL," +
                    "category_id BIGINT NOT NULL," +
                    "FOREIGN KEY (category_id) REFERENCES categories(id)" +
                    ")");
            st.execute("MERGE INTO categories (id, name) KEY(id) VALUES (1, 'Electronics')");
            st.execute("MERGE INTO categories (id, name) KEY(id) VALUES (2, 'Books')");
            System.out.println("DB created or verified");
        } catch (SQLException e) {
            System.err.println("DB init error: " + e.getMessage());
        }
    }

    public static void loadProducts(Category electronics, Category books) {
        String sql = "SELECT id, name, price, category_id FROM products";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int catId = rs.getInt("category_id");
                Category target = (catId == 1) ? electronics : books;
                Product p = new Product(id, name, price);
                target.addProduct(p);
            }
        } catch (SQLException e) {
            System.err.println("Load products error: " + e.getMessage());
        }
    }

    public static void saveProduct(Product p, int categoryId) {
        String sql = "MERGE INTO products (id, name, price, category_id) KEY(id) VALUES (?, ?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getId());
            ps.setString(2, p.getName());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, categoryId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Save product error: " + e.getMessage());
        }
    }

    public static void deleteProduct(String id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Delete product error: " + e.getMessage());
        }
    }

    public static void resetDatabase() {
        try (Connection c = getConnection(); Statement st = c.createStatement()) {
            st.execute("DROP ALL OBJECTS");
            System.out.println("Database cleared!");
        } catch (SQLException e) {
            System.err.println("Reset error: " + e.getMessage());
        }
    }

    // =============== DISCOUNT METHODS ===============

    public static void initDiscountTables() {
        try (Connection c = getConnection(); Statement st = c.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS category_discounts (" +
                    "id VARCHAR(50) PRIMARY KEY," +
                    "category_name VARCHAR(255) NOT NULL," +
                    "discount_percent DOUBLE NOT NULL," +
                    "created_at TIMESTAMP NOT NULL," +
                    "end_time TIMESTAMP NOT NULL" +
                    ")");
            st.execute("CREATE TABLE IF NOT EXISTS promo_codes (" +
                    "id VARCHAR(50) PRIMARY KEY," +
                    "code VARCHAR(100) NOT NULL UNIQUE," +
                    "category_name VARCHAR(255) NOT NULL," +
                    "discount_percent DOUBLE NOT NULL," +
                    "created_at TIMESTAMP NOT NULL," +
                    "used BOOLEAN DEFAULT FALSE" +
                    ")");
            System.out.println("Discount tables created or verified");
        } catch (SQLException e) {
            System.err.println("Discount tables init error: " + e.getMessage());
        }
    }

    public static void saveCategoryDiscount(CategoryDiscount discount) {
        String sql = "MERGE INTO category_discounts " +
                "(id, category_name, discount_percent, created_at, end_time) KEY(id) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, discount.getId());
            ps.setString(2, discount.getCategory().getName());
            ps.setDouble(3, discount.getDiscountPercent());
            ps.setTimestamp(4, Timestamp.valueOf(discount.getCreatedAt()));
            ps.setTimestamp(5, Timestamp.valueOf(discount.getEndTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Save category discount error: " + e.getMessage());
        }
    }

    public static void savePromoCode(PromoCode promo) {
        String sql = "MERGE INTO promo_codes " +
                "(id, code, category_name, discount_percent, created_at, used) KEY(id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, promo.getId());
            ps.setString(2, promo.getCode());
            ps.setString(3, promo.getCategory().getName());
            ps.setDouble(4, promo.getDiscountPercent());
            ps.setTimestamp(5, Timestamp.valueOf(promo.getCreatedAt()));
            ps.setBoolean(6, promo.isUsed());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Save promo code error: " + e.getMessage());
        }
    }

    public static void updatePromoCodeUsage(String code) {
        String sql = "UPDATE promo_codes SET used = TRUE WHERE code = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Update promo code error: " + e.getMessage());
        }
    }

    public static void loadDiscounts(
            java.util.List<CategoryDiscount> discounts,
            java.util.List<PromoCode> promoCodes,
            Category electronics,
            Category books) {

        String discountSql = "SELECT id, category_name, discount_percent, created_at, end_time FROM category_discounts";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(discountSql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Category cat = rs.getString("category_name").equals("Electronics")
                        ? electronics : books;
                CategoryDiscount discount = new CategoryDiscount(
                        rs.getString("id"),
                        cat,
                        rs.getDouble("discount_percent"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime()
                );
                discounts.add(discount);
            }
        } catch (SQLException e) {
            System.err.println("Load discounts error: " + e.getMessage());
        }

        String promoSql = "SELECT id, code, category_name, discount_percent, created_at, used FROM promo_codes";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(promoSql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Category cat = rs.getString("category_name").equals("Electronics")
                        ? electronics : books;
                PromoCode promo = new PromoCode(
                        rs.getString("id"),
                        cat,
                        rs.getDouble("discount_percent"),
                        rs.getString("code"),
                        rs.getBoolean("used"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                promoCodes.add(promo);
            }
        } catch (SQLException e) {
            System.err.println("Load promo codes error: " + e.getMessage());
        }
    }
}
