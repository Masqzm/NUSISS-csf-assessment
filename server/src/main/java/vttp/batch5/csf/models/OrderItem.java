package vttp.batch5.csf.models;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class OrderItem {
    private String id;
    private double price;
    private int quantity;

    public static OrderItem toOrderItem(String payload) {
        OrderItem oi = new OrderItem();

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject json = reader.readObject();

        oi.setId(json.getString("id"));
        oi.setPrice(json.getJsonNumber("price").doubleValue());
        oi.setQuantity(json.getInt("quantity"));

        return oi;
    }

    @Override
    public String toString() {
        return "OrderItem [id=" + id + ", price=" + price + ", quantity=" + quantity + "]";
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }    
}
