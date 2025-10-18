import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main {
    private static final AccountManager accountManager = new AccountManager();
    private static final TaxService taxService = new VatService();
    private static final User currentUser = new User("Customer");
    private static List<CategoryDiscount> categoryDiscounts = new ArrayList<>();
    private static List<PromoCode> promoCodes = new ArrayList<>();

    public static void main(String[] args) {
        // Инициализация базы данных
        Database.initDatabase();
        Database.initDiscountTables();

        Category electronics = new Category("Electronics");
        Category books = new Category("Books");

        Database.loadProducts(electronics, books);
        Database.loadDiscounts(categoryDiscounts, promoCodes, electronics, books);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("=== SHOP MANAGEMENT SYSTEM ===");
            System.out.println("1. Add product to Electronics");
            System.out.println("2. Add product to Books");
            System.out.println("3. Show all products");
            System.out.println("4. Remove product");
            System.out.println("5. Show products by category");
            System.out.println("6. Create category discount");
            System.out.println("7. Create promo code");
            System.out.println("8. Use promo code");
            System.out.println("9. Show all discounts and promo codes");
            System.out.println("10. Buy Product");
            System.out.println("11. Show project information");
            System.out.println("12. View Balance and Credit");
            System.out.println("13. Deposit Funds");
            System.out.println("14. Repay Credit");
            System.out.println("15. Reset Database");
            System.out.println("16. Exit");
            System.out.print("Choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    handleAddProduct(scanner, electronics, 1);
                    break;
                case 2:
                    handleAddProduct(scanner, books, 2);
                    break;
                case 3:
                    showAllProducts(electronics, books);
                    break;
                case 4:
                    handleRemoveProduct(scanner, electronics, books);
                    break;
                case 5:
                    showByCategory(scanner, electronics, books);
                    break;
                case 6:
                    handleCreateCategoryDiscount(scanner, electronics, books);
                    break;
                case 7:
                    handleCreatePromoCode(scanner, electronics, books);
                    break;
                case 8:
                    handleUsePromoCode(scanner, electronics, books);
                    break;
                case 9:
                    showAllDiscounts();
                    break;
                case 10:
                    handleBuyProduct(scanner, electronics, books);
                    break;
                case 11:
                    showProjectInformation(electronics, books);
                    break;
                case 12:
                    showBalanceAndCredit();
                    break;
                case 13:
                    handleDeposit(scanner);
                    break;
                case 14:
                    handleRepayCredit(scanner);
                    break;
                case 15:
                    Database.resetDatabase();
                    Database.initDatabase();
                    Database.initDiscountTables();
                    electronics = new Category("Electronics");
                    books = new Category("Books");
                    categoryDiscounts.clear();
                    promoCodes.clear();
                    System.out.println("Database has been reset.");
                    break;
                case 16:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private static void handleBuyProduct(Scanner scanner, Category electronics, Category books) {
        System.out.print("Enter product ID to buy: ");
        String productId = scanner.nextLine().trim();
        Product product = AntiBag.findProductById(productId, electronics, books);

        if (product == null) {
            System.out.println("Product not found!");
            return;
        }

        if (product.getQuantity() <= 0) {
            System.out.println("Sorry, this product is out of stock.");
            return;
        }

        double price = product.finalPrice(categoryDiscounts);
        double tax = taxService.calculateTax(price);
        double finalPrice = price + tax;

        System.out.printf("Product: %s%n", product.getName());
        System.out.printf("Price: %.2f USDT%n", price);
        System.out.printf("VAT (20%%): %.2f USDT%n", tax);
        System.out.printf("Total: %.2f USDT%n", finalPrice);
        System.out.print("Proceed with purchase? (yes/no): ");
        String confirmation = scanner.nextLine().trim();

        if (confirmation.equalsIgnoreCase("yes")) {
            if (accountManager.purchase(currentUser, finalPrice, scanner)) {
                product.decreaseQuantity(1);
                System.out.println("Purchase successful!");
            } else {
                System.out.println("Purchase failed. Check your balance or credit options.");
            }
        } else {
            System.out.println("Purchase cancelled.");
        }
    }

    private static void showBalanceAndCredit() {
        System.out.println("\n--- ACCOUNT INFORMATION ---");
        System.out.printf("Current Balance: %.2f USDT%n", accountManager.getBalance(currentUser));
        System.out.printf("Outstanding Credit: %.2f USDT%n", accountManager.getCredit(currentUser));
        System.out.println("-------------------------");
    }

    private static void handleDeposit(Scanner scanner) {
        System.out.print("Enter amount to deposit: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            accountManager.deposit(currentUser, amount);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a numeric value.");
        }
    }

    private static void handleRepayCredit(Scanner scanner) {
        System.out.print("Enter amount to repay: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            accountManager.repayCredit(currentUser, amount);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a numeric value.");
        }
    }

    private static void handleCreateCategoryDiscount(Scanner scanner, Category electronics, Category books) {
        System.out.println("\nSelect category for discount:");
        System.out.println("1. Electronics");
        System.out.println("2. Books");
        System.out.print("Choice: ");

        int catChoice;
        try {
            catChoice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice.");
            return;
        }

        Category selectedCategory = (catChoice == 1) ? electronics : (catChoice == 2 ? books : null);
        if (selectedCategory == null) {
            System.out.println("Invalid category choice.");
            return;
        }

        System.out.print("Enter discount percentage (0-100): ");
        double discount;
        try {
            discount = Double.parseDouble(scanner.nextLine().trim());
            if (discount < 0 || discount > 100) {
                System.out.println("Discount must be between 0 and 100.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid discount percentage.");
            return;
        }

        System.out.print("Enter duration in hours: ");
        int hours;
        try {
            hours = Integer.parseInt(scanner.nextLine().trim());
            if (hours <= 0) {
                System.out.println("Duration must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid duration.");
            return;
        }

        CategoryDiscount categoryDiscount = new CategoryDiscount(selectedCategory, discount, hours);
        categoryDiscounts.add(categoryDiscount);
        Database.saveCategoryDiscount(categoryDiscount);

        System.out.println("Category discount created: " + categoryDiscount);
    }

    private static void handleCreatePromoCode(Scanner scanner, Category electronics, Category books) {
        System.out.println("\nSelect category for promo code:");
        System.out.println("1. Electronics");
        System.out.println("2. Books");
        System.out.print("Choice: ");

        int catChoice;
        try {
            catChoice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice.");
            return;
        }

        Category selectedCategory = (catChoice == 1) ? electronics : (catChoice == 2 ? books : null);
        if (selectedCategory == null) {
            System.out.println("Invalid category choice.");
            return;
        }

        System.out.print("Enter promo code: ");
        String code = scanner.nextLine().trim().toUpperCase();
        if (code.isEmpty()) {
            System.out.println("Promo code cannot be empty.");
            return;
        }

        // Проверка уникальности кода
        for (PromoCode existing : promoCodes) {
            if (existing.getCode().equals(code)) {
                System.out.println("Promo code already exists!");
                return;
            }
        }

        System.out.print("Enter discount percentage (0-100): ");
        double discount;
        try {
            discount = Double.parseDouble(scanner.nextLine().trim());
            if (discount < 0 || discount > 100) {
                System.out.println("Discount must be between 0 and 100.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid discount percentage.");
            return;
        }

        PromoCode promoCode = new PromoCode(selectedCategory, discount, code);
        promoCodes.add(promoCode);
        Database.savePromoCode(promoCode);

        System.out.println("Promo code created: " + promoCode);
    }

    private static void handleUsePromoCode(Scanner scanner, Category electronics, Category books) {
        System.out.print("Enter promo code: ");
        String code = scanner.nextLine().trim().toUpperCase();

        PromoCode promoCode = null;
        for (PromoCode pc : promoCodes) {
            if (pc.getCode().equals(code)) {
                promoCode = pc;
                break;
            }
        }

        if (promoCode == null) {
            System.out.println("Promo code not found!");
            return;
        }

        if (promoCode.isUsed()) {
            System.out.println("Promo code has already been used!");
            return;
        }

        System.out.print("Enter product ID to apply discount: ");
        String productId = scanner.nextLine().trim();

        Product product = AntiBag.findProductById(productId, electronics, books);
        if (product == null) {
            System.out.println("Product not found!");
            return;
        }

        double discountAmount = promoCode.applyDiscount(product);

        if (discountAmount > 0) {
            promoCode.use();
            Database.updatePromoCodeUsage(code);
            double newPrice = product.getPrice() - discountAmount;

            System.out.printf("Promo code applied successfully!%n");
            System.out.printf("Product: %s%n", product.getName());
            System.out.printf("Original price: %.2f USDT%n", product.getPrice());
            System.out.printf("Discount: %.2f USDT (%.1f%%)%n", discountAmount, promoCode.getDiscountPercent());
            System.out.printf("Final price: %.2f USDT%n", newPrice);
        } else {
            System.out.println("Cannot apply promo code to this product (wrong category or already used).");
        }

    }

    private static void showAllDiscounts() {
        System.out.println("\n=== ACTIVE CATEGORY DISCOUNTS ===");
        boolean hasActiveDiscounts = false;
        for (CategoryDiscount discount : categoryDiscounts) {
            if (discount.isActive()) {
                System.out.println(discount);
                hasActiveDiscounts = true;
            }
        }
        if (!hasActiveDiscounts) {
            System.out.println("No active category discounts.");
        }

        System.out.println("\n=== EXPIRED CATEGORY DISCOUNTS ===");
        boolean hasExpiredDiscounts = false;
        for (CategoryDiscount discount : categoryDiscounts) {
            if (!discount.isActive()) {
                System.out.println(discount);
                hasExpiredDiscounts = true;
            }
        }
        if (!hasExpiredDiscounts) {
            System.out.println("No expired category discounts.");
        }

        System.out.println("\n=== NEW PROMO CODES ===");
        boolean hasNewPromos = false;
        for (PromoCode promo : promoCodes) {
            if (!promo.isUsed()) {
                System.out.println(promo);
                hasNewPromos = true;
            }
        }
        if (!hasNewPromos) {
            System.out.println("No new promo codes.");
        }

        System.out.println("\n=== USED PROMO CODES ===");
        boolean hasUsedPromos = false;
        for (PromoCode promo : promoCodes) {
            if (promo.isUsed()) {
                System.out.println(promo);
                hasUsedPromos = true;
            }
        }
        if (!hasUsedPromos) {
            System.out.println("No used promo codes.");
        }
    }

    private static void showProjectInformation(Category electronics, Category books) {
        System.out.println("\n========== PROJECT INFORMATION ==========");
        System.out.println("Project: Shop Management System with Discounts");
        System.out.println("Features: Products, Categories, Discounts, Promo Codes");
        System.out.println("Database: H2 (File-based)");
        System.out.println("==========================================");

        System.out.printf("Total products created (lifetime): %d\n", Product.getCreatedCount());
        System.out.printf("Electronics products: %d\n", electronics.getProducts().size());
        System.out.printf("Books products: %d\n", books.getProducts().size());
        System.out.printf("Category discounts: %d\n", categoryDiscounts.size());
        System.out.printf("Promo codes: %d\n", promoCodes.size());

        int activeDiscounts = (int) categoryDiscounts.stream().mapToLong(d -> d.isActive() ? 1 : 0).sum();
        int newPromoCodes = (int) promoCodes.stream().mapToLong(p -> p.isUsed() ? 0 : 1).sum();

        System.out.printf("Active discounts: %d\n", activeDiscounts);
        System.out.printf("Available promo codes: %d\n", newPromoCodes);

        System.out.println("==========================================");
    }

    // Остальные методы остаются без изменений
    private static void handleAddProduct(Scanner scanner, Category category, int categoryId) {
        System.out.print("Enter product id (leave blank for auto): ");
        String idInput = scanner.nextLine().trim();
        String id = idInput.isEmpty() ? null : idInput;

        if (id != null && AntiBag.findProductById(id, category) != null) {
            System.out.println("A product with this id already exists in this category.");
            return;
        }

        String name;
        do {
            System.out.print("Enter product name: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be blank.");
            }
        } while (name.isEmpty());

        System.out.print("Enter product description (optional): ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            description = null;
        }

        double price;
        while (true) {
            System.out.print("Enter product price: ");
            String priceStr = scanner.nextLine().trim();
            try {
                price = Double.parseDouble(priceStr);
                if (price < 0) {
                    System.out.println("Price cannot be negative.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid price. Please enter a numeric value.");
            }
        }

        int qty;
        while (true) {
            System.out.print("Enter quantity (optional, default 0): ");
            String qtyStr = scanner.nextLine().trim();
            if (qtyStr.isEmpty()) {
                qty = 0;
                break;
            }
            try {
                qty = Integer.parseInt(qtyStr);
                if (qty < 0) {
                    System.out.println("Quantity cannot be negative.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Please enter a whole number.");
            }
        }

        Product p = new Product(id, name, description, price, qty, category);

        if (!AntiBag.registerProduct(p)) {
            System.out.println("Product id already exists globally; choose another id.");
            return;
        }

        category.addProduct(p);
        Database.saveProduct(p, categoryId);
        System.out.println("Product added: " + p);
    }

    private static void showAllProducts(Category electronics, Category books) {
        System.out.println("\nElectronics:");
        for (Product p : electronics.getProducts()) {
            System.out.println("  " + p);
        }

        System.out.println("\nBooks:");
        for (Product p : books.getProducts()) {
            System.out.println("  " + p);
        }

        System.out.println("\nTotal products created (lifetime): " + Product.getCreatedCount());
    }

    private static void handleRemoveProduct(Scanner scanner, Category electronics, Category books) {
        System.out.print("Enter product id to remove: ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("No id entered.");
            return;
        }

        boolean removed = AntiBag.removeProductFromCategories(id, electronics, books);
        if (removed) {
            Database.deleteProduct(id);
            System.out.println("Product removed: " + id);
        } else {
            System.out.println("Product not found: " + id);
        }
    }

    private static void showByCategory(Scanner scanner, Category electronics, Category books) {
        System.out.println("\nSelect category:");
        System.out.println("1. Electronics");
        System.out.println("2. Books");
        System.out.print("Choice: ");
        int catChoice;
        try {
            catChoice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice.");
            return;
        }

        Category selected = (catChoice == 1) ? electronics : (catChoice == 2 ? books : null);
        if (selected == null) {
            System.out.println("Unknown category.");
            return;
        }

        System.out.println("\n" + selected.getName() + ":");
        for (Product p : selected.getProducts()) {
            System.out.println("  " + p);
        }
        System.out.println();
    }
}
