package vttp.batch5.csf.models;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Menu {
    private String id;
    private String name;
    private String description;
    private double price;

    public static Menu toMenu(Document doc) {
        Menu menu = new Menu();

        menu.setId(doc.getString("id"));
        menu.setName(doc.getString("name"));
        menu.setDescription(doc.getString("description"));
        menu.setPrice(doc.getDouble("price"));

        return menu;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("id", id)
            .add("name", name)
            .add("description", description)
            .add("price", price)
            .build();
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}