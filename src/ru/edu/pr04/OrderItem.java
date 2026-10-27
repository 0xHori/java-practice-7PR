package ru.edu.pr04;

public class OrderItem {

    public static final int MIN_QUANTITY = 1;

    public static final int MAX_QUANTITY = 110;

    private final Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Товар не может быть null");
        }
        validateQuantity(quantity);
        this.product = product;
        this.quantity = quantity;
    }

    private static void validateQuantity(int quantity) {
        if (quantity < MIN_QUANTITY || quantity > MAX_QUANTITY) {
            throw new IllegalArgumentException(
                    "Количество должно быть в диапазоне " + MIN_QUANTITY + ".." + MAX_QUANTITY
                            + ": " + quantity);
        }
    }

    public void increase(int delta) {
        if (delta <= 0) {
            throw new IllegalArgumentException("Увеличение должно быть положительным: " + delta);
        }
        int newQuantity = quantity + delta;
        validateQuantity(newQuantity);
        this.quantity = newQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getLineTotal() {
        return product.getPrice() * quantity;
    }
}
