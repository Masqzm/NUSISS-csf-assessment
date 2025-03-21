package vttp.batch5.csf.assessment.server.controllers;

import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.csf.assessment.server.services.RestaurantService;
import vttp.batch5.csf.models.Menu;
import vttp.batch5.csf.models.User;

@Controller
@RequestMapping("/api")
public class RestaurantController {
  @Autowired
  private RestaurantService restaurantSvc;

  // TODO: Task 2.2
  // You may change the method's signature
  @GetMapping(path="/menu", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getMenus() {
    JsonArrayBuilder builder = Json.createArrayBuilder();

    restaurantSvc.getMenu().stream()
      .map(Menu::toJson)
      .forEach(builder::add);

    return ResponseEntity.ok(builder.build().toString());
  }

  // TODO: Task 4
  // Do not change the method's signature
  @PostMapping(path="/food_order", 
    consumes=MediaType.APPLICATION_JSON_VALUE,
    produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
    
    //System.out.println(payload);

    JsonReader reader = Json.createReader(new StringReader(payload));
    JsonObject json = reader.readObject();

    User user = new User();
    user.setUsername(json.getString("username"));

    try {
      String unhashedPw = json.getString("password");

      MessageDigest md224 = MessageDigest.getInstance("SHA-224");
      
      byte[] hash = md224.digest(unhashedPw.getBytes());
      StringBuilder hashPw = new StringBuilder();
      
      for (int i = 0; i < hash.length; i++) {
          String hex = Integer.toHexString(0xff & hash[i]);
          if(hex.length() == 1) 
            hashPw.append('0');
          hashPw.append(hex);
      }
      
      user.setPassword(hashPw.toString());
    } catch (NoSuchAlgorithmException e) {
      String response = Json.createObjectBuilder()
                    .add("message", e.getMessage())
                    .build().toString();  

      return ResponseEntity.status(500).body(response);
    }

    // Validate request
    try {
      if(!restaurantSvc.checkCredentials(user)) {
        String response = Json.createObjectBuilder()
                      .add("message", "Invalid username and/or password")
                      .build().toString();  

        return ResponseEntity.status(401).body(response);
      }
    }
    catch(Exception ex){
      String response = Json.createObjectBuilder()
                    .add("message", ex.getMessage())
                    .build().toString();  

      return ResponseEntity.status(500).body(response);
    }

    // Place order
    String orderId = UUID.randomUUID().toString().substring(0, 8);

    String response = Json.createObjectBuilder()
                    .add("orderId", orderId)
                    .build().toString();

    return ResponseEntity.ok(response);
  }

  @PostMapping(path="/payment", 
  consumes=MediaType.APPLICATION_JSON_VALUE,
  produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> postPayment(@RequestBody String payload) {
    //System.out.println("\npost payment: " + payload);

    String response;
    
    try {
      restaurantSvc.postPayment(payload);
    } catch(Exception ex) {
      response = Json.createObjectBuilder()
                .add("message", ex.getMessage())
                .build().toString();  

      return ResponseEntity.status(500).body(response);
    }

    return ResponseEntity.ok("{}");
  }
}
