import java.util.List;

public class EmailSender {
    public static void sendOrderConfirmation(String customerEmail, String customerName, List<Item> items, double totalPrice) {
        String message = "Thank you for your order! " + customerName + "!\n\n" +
                "Your order:\n";
        for (Item item : items) {
            message += item.getName() + " - " + item.getPrice() + "\n";
        }
        message += "Total: " + totalPrice;
        sendEmail(customerEmail, "Here is your Order Confirmation!", message);
    }
    public static void sendEmail(String customerEmail, String subject, String message){
        System.out.println("Email to: " + customerEmail);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + message);
    }
}
