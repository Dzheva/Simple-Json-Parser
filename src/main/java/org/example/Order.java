package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {
    private String productType;

    private String brandName;

    private int price;

    private String clientName;

    @JsonCreator
    public Order(@JsonProperty("productType") String productType, @JsonProperty("brandName") String brandName, @JsonProperty("price") int price, @JsonProperty("clientName") String clientName) {
        this.productType = productType;
        this.brandName = brandName;
        this.price = price;
        this.clientName = clientName;
    }

    public String getProductType() {
        return productType;
    }

    public String getBrandName() {
        return brandName;
    }

    public int getPrice() {
        return price;
    }

    public String getClientName() {
        return clientName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;

        Order order = (Order) o;

        if (price != order.price) return false;
        if (productType != null ? !productType.equals(order.productType) : order.productType != null) return false;
        if (brandName != null ? !brandName.equals(order.brandName) : order.brandName != null) return false;
        return clientName != null ? clientName.equals(order.clientName) : order.clientName == null;
    }

    @Override
    public int hashCode() {
        int result = productType != null ? productType.hashCode() : 0;
        result = 31 * result + (brandName != null ? brandName.hashCode() : 0);
        result = 31 * result + price;
        result = 31 * result + (clientName != null ? clientName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "productType='" + productType + '\'' +
                ", brandName='" + brandName + '\'' +
                ", price=" + price +
                ", clientName='" + clientName + '\'' +
                '}';
    }
}
