import java.util.List;

public class EmailSender {
    public static void sendOrderConfirmation(Order order, double totalPrice) {
        String message = "Thank you for your order! " + order.getCustomerName() + "!\n\n" +
                "Your order:\n";
        for (Item item : order.getItems()) {
            message += item.getName() + " - " + item.getPrice() + "\n";
        }
        message += "Total: " + totalPrice;
        sendEmail(order.getCustomerEmail(), "Here is your Order Confirmation!", message);
    }
    public static void sendEmail(String customerEmail, String subject, String message){
        System.out.println("Email to: " + customerEmail);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + message);
    }
}
