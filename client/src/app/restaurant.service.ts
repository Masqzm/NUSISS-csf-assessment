import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Menu } from "./models";

@Injectable()
export class RestaurantService {
  private http = inject(HttpClient)

  order: Menu[] = []    // TODO: use store if got time
  totalOrderPrice = 0   

  clearOrder() {
    this.order = []
    this.totalOrderPrice = 0
  }

  // TODO: Task 2.2
  // You change the method's signature but not the name
  getMenuItems() {
    return this.http.get<Menu[]>('/api/menu')
  }

  // TODO: Task 3.2

  
}
