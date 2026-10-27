package ru.edu.pr04;

import java.util.Objects;

public class Product {

    private final String id;
    private final String name;
    private final double price;

    public Product(String id, String name, double price) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID товара не может быть пустым");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Наименование товара не может быть пустым");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Цена товара должна быть положительной: " + price);
        }
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        return id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
