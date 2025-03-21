package vttp.batch5.csf.assessment.server.services;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;
import vttp.batch5.csf.models.Menu;
import vttp.batch5.csf.models.OrderItem;
import vttp.batch5.csf.models.Receipt;
import vttp.batch5.csf.models.User;

@Service
public class RestaurantService {
  @Autowired
  private OrdersRepository orderRepo;
  @Autowired
  private RestaurantRepository restaurantRepo;

  @Value("${payment.url}")
  private String paymentUrl;
  @Value("${payment.nricName}")
  private String paymentNRICName;

  // Task 2.2
  // You may change the method's signature
  public List<Menu> getMenu() {
    return orderRepo.getMenu();
  }
  
  // Task 4
  public boolean checkCredentials(User user) throws SQLException {
    Optional<User> userOpt = restaurantRepo.getUserByUsername(user.getUsername());

    if(userOpt.isEmpty())   // user not found
      return false;
    else 
      // Compare passwords
      return userOpt.get().getPassword().equals(user.getPassword());
  }

  public String postPayment(String incomingPayload) {
    JsonReader reader = Json.createReader(new StringReader(incomingPayload));
    JsonObject jsonIn = reader.readObject();
    
    // Extract OrderItems
    List<OrderItem> orderItems = new ArrayList<>();
    JsonArray jArr = jsonIn.getJsonArray("items");
    for (int i = 0; i < jArr.size(); i++)
    {
      JsonObject jOI = jArr.get(i).asJsonObject();
      orderItems.add(OrderItem.toOrderItem(jOI.toString()));
    }

    System.out.println(orderItems.toString());

    // Create outgoing Json payload
    JsonObject jsonOut = Json.createObjectBuilder()
        .add("order_id", jsonIn.getString("order_id"))
        .add("payer", jsonIn.getString("payer"))
        .add("payee", paymentNRICName)
        .add("payment", jsonIn.getJsonNumber("payment"))
        .build();

    RequestEntity<String> req = RequestEntity
        .post(paymentUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .header("X-Authenticate", jsonIn.getString("payer"))
        .body(jsonOut.toString(), String.class);

    //System.out.println("REQ BODY: " + req.getBody());
    //System.out.println("REQ HEADER: " + req.getHeaders());

    // Create REST template
    RestTemplate template = new RestTemplate();
    ResponseEntity<String> resp;

    try {
      // Make call
      resp = template.exchange(req, String.class);
      // Extract payload
      String payload = resp.getBody();

      //System.out.println("/nReceipt: " + Receipt.toReceipt(payload));
      Receipt receipt = Receipt.toReceipt(payload);

      // Save payment to MySQL and mongoDB
      try {
        savePayment(receipt, jsonIn.getString("payer"), 
                  jsonIn.getJsonNumber("payment").doubleValue(), orderItems);
      } catch(Exception ex) {
        // Problem saving to db
        throw ex;
      }

      // All successful, return payment receipt to client
      JsonObject respToClient = Json.createObjectBuilder()
        .add("orderId", receipt.getOrder_id())
        .add("paymentId", receipt.getPayment_id())
        .add("total", jsonIn.getJsonNumber("payment"))
        .add("timestamp", receipt.getTimestamp())
        .build();

      return respToClient.toString();
    } catch (Exception ex) {
      throw ex;
    }
  }

  @Transactional
  public void savePayment(Receipt receipt, String username, double total, List<OrderItem> orderItems) {
    restaurantRepo.insertOrder(receipt, username, total);
    orderRepo.insertOrder(receipt, username, total, orderItems);
  }
}