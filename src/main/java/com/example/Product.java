package com.example;

import java.util.*;

import com.example.CollectionsAndMapsUtils.*;

public class Product {
    private String name;
    private double price;
    private String category;
    private int stockQuantity;
    private boolean available;

    public Product() {}

    private Product(Builder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.category = builder.category;
        this.stockQuantity = builder.stockQuantity;
        this.available = builder.available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // toString()
    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", available=" + available +
                '}';
    }

    // hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(name, price, category, stockQuantity, available);
    }

    // equals(Object obj)
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        return Objects.equals(name, other.name)
                && Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price)
                && Objects.equals(category, other.category)
                && stockQuantity == other.stockQuantity
                && available == other.available;
    }


    // Inner Class Builder
    public static class Builder {
        private String name;
        private double price;
        private String category;
        private int stockQuantity;
        private boolean available;

        public Builder(String name) {
            this.name = name;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder stockQuantity(int stockQuantity) {
            this.stockQuantity = stockQuantity;
            return this;
        }

        public Builder available(boolean available) {
            this.available = available;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Product product1 = new Builder("Laptop")
                .price(1299.99)
                .category("Electronics")
                .stockQuantity(50)
                .available(true)
                .build();

        Product product2 = new Builder("Tablet")
                .price(599.99)
                .category("Electronics")
                .stockQuantity(0)
                .available(false)
                .build();

        Product product3 = new Builder("SmartPhone")
                .price(299.99)
                .category("Electronics")
                .stockQuantity(500)
                .available(true)
                .build();

        Product product4 = new Builder("Tablet")
                .price(599.99)
                .category("Electronics")
                .stockQuantity(100)
                .available(false)
                .build();

        List<Product> list1 = Arrays.asList(product1, product2, product3);
        List<Product> list2 = Arrays.asList(product3, product2, product1);
        List<Product> list3 = Arrays.asList(product1, product4, product3);
        List<Product> list4 = Arrays.asList(product1, product2);
        List<Product> list5 = Arrays.<Product>asList();
        List<Product> list6 = Arrays.<Product>asList();
        Set<Product> set1 = Set.of(product1, product2, product3);

        System.out.println("Products");
        System.out.println(product1);
        System.out.println(product2);
        System.out.println(product3);
        System.out.println(product4);
        System.out.println("\nComparing Lists");
        System.out.println(STR."list1 and list2 are equal (with order)? \{CollectionsAndMapsUtils.areCollectionsEqual(list1, list2)}");
        System.out.println(STR."list1 and list2 are equal (without order)? \{CollectionsAndMapsUtils.areCollectionsEqualIgnoringOrder(list1, list2)}");
        System.out.println(STR."list1 and list3 are equal? \{CollectionsAndMapsUtils.areCollectionsEqualIgnoringOrder(list1, list3)}");
        System.out.println(STR."list4 and list2 are equal? \{CollectionsAndMapsUtils.areCollectionsEqualIgnoringOrder(list2, list4)}");
        System.out.println(STR."list2 and emptyList are equal? \{CollectionsAndMapsUtils.areCollectionsEqualIgnoringOrder(list5, list2)}");
        System.out.println(STR."two emptyLists are equal? \{CollectionsAndMapsUtils.areCollectionsEqualIgnoringOrder(list5, list6)}");
        System.out.println("\nShould throw NullPointerException");
        try {
            CollectionsAndMapsUtils.areCollectionsEqual(null, list1);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Thread thread = null;
        thread.sleep(1000);
        System.out.println("\nShould throw UnsupportedOperationException");
        try {
            CollectionsAndMapsUtils.areCollectionsEqualIgnoringOrder(set1, list1);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}

