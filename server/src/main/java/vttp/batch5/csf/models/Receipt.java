package vttp.batch5.csf.models;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Receipt {
    private String payment_id;
    private String order_id;
    private Long timestamp;     // in ms

    public static Receipt toReceipt(String payload) {
        Receipt receipt = new Receipt();

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject json = reader.readObject();

        receipt.setPayment_id(json.getString("payment_id"));
        receipt.setOrder_id(json.getString("order_id"));
        receipt.setTimestamp(json.getJsonNumber("timestamp").longValue());

        return receipt;
    }

    @Override
    public String toString() {
        return "Receipt [payment_id=" + payment_id + ", order_id=" + order_id + ", timestamp=" + timestamp + "]";
    }

    public String getPayment_id() {
        return payment_id;
    }
    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }
    public String getOrder_id() {
        return order_id;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
    public Long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    
}
