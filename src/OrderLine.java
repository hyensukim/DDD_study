public class OrderLine {
    private Product Product;
    private int price;
    private int quantity;
    private int amounts;

    public OrderLine(Product product, int price, int quantity){
        this.Product = product;
        this.price = price;
        this.quantity = quantity;
        this.amounts = calculateAmounts();
    }

    private int calculateAmounts(){
        return price * quantity;
    }
}
