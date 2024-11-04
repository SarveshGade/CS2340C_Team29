
public class TaxableItem extends Item {
    private double taxRate = 7;
    
    public TaxableItem(Item toTax) {
        super(toTax.getName(), toTax.getPrice(), toTax.getQuantity(), toTax.getDiscountType(), toTax.getDiscountAmount());
    }

    public double getTaxRate(){
        return taxRate;
    }
    public void setTaxRate(double rate) {
        if(rate>=0){
            taxRate = rate;
        }
    }
}
