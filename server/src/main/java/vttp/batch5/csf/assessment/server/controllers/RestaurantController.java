package vttp.batch5.csf.assessment.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import vttp.batch5.csf.assessment.server.services.RestaurantService;
import vttp.batch5.csf.models.Menu;

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
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
    return ResponseEntity.ok("{}");
  }
}
