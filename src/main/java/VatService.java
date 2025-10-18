public class VatService implements TaxService {
    private static final double VAT_RATE = 0.20; // 20%

    @Override
    public double calculateTax(double amount) {
        return amount * VAT_RATE;
    }
}