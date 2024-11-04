import java.util.List;

public class Order {
    private List<Item> items;
    private String customerName;
    private String customerEmail;
    private boolean hasGiftCard;

    public Order(List<Item> items, String customerName, String customerEmail) {
        this.items = items;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.hasGiftCard = checkForGiftCard();
    }

    public double applyDiscounts(Item item) {
        double price = item.getPrice();
        switch (item.getDiscountType()) {
            case PERCENTAGE:
                price -= item.getDiscountAmount() * price;
                break;
            case AMOUNT:
                price -= item.getDiscountAmount();
                break;
            default:
                // no discount
                break;
        }
        return price;
    }
    public double calculateTax(Item item) {
        if (item instanceof TaxableItem) {
            TaxableItem taxableItem = (TaxableItem) item;
            double tax = taxableItem.getTaxRate() / 100.0 * item.getPrice();
            return tax;
        }
        return 0.0;
    }

    public double calculateTotalPrice() {
    	double total = 0.0;
    	for (Item item : items) {
            total += calculateItemTotal(item);
        }

        total = applyGiftCardDiscount(total);
        total = applyLargeOrderDiscount(total);
    	return total;
    }

    private double calculateItemTotal(Item item) {
        double priceAfterDiscount = applyDiscounts(item);
        double totalTax = calculateTax(item);
        return (priceAfterDiscount + totalTax) * item.getQuantity();
    }

    private double applyGiftCardDiscount(double total) {
        if (this.hasGiftCard) {
            total -= 10.0; // subtract $10 for gift card
        }
        return total;
    }

    private double applyLargeOrderDiscount(double total) {
        if (total > 100.0) {
            total *= 0.9; // apply 10% discount for orders over $100
        }
        return total;
    }

    public void sendConfirmationEmail() {
        double totalPrice = calculateTotalPrice();
        EmailSender.sendOrderConfirmation(this, totalPrice);
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    private boolean checkForGiftCard() {
        for (Item item : items) {
            if (item.getName().equals("Gift Card")) {
                return true;
            }
        }
        return false;
    }

    public void addItem(Item item) {
        items.add(item);
        if (item.getName().equals("Gift Card")) {
            hasGiftCard = true;
        }
    }

    public void removeItem(Item item) {
        items.remove(item);
        if (item.getName().equals("Gift Card")) {
            hasGiftCard = checkForGiftCard();
        }
    }

    public boolean hasGiftCard() {
        return hasGiftCard;
    }

   public void printOrder() {
        System.out.println("Order Details:");
        for (Item item : items) {
            System.out.println(item.getName() + " - " + item.getPrice());
        }
   }

   public void addItemsFromAnotherOrder(Order otherOrder) {
        for (Item item : otherOrder.getItems()) {
            items.add(item);
        }
   }

}

