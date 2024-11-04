
public class TaxableItem extends Item {
    private double taxRate = 7;
    
    public TaxableItem(Item toTax){
        this.setName(toTax.getName());
        this.setPrice(toTax.getPrice());
        this.setQuantity(toTax.getQuantity());
        this.setDiscountType(toTax.getDiscountType());
        this.setDiscountAmount(toTax.getDiscountAmount());
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
