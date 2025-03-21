import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Menu, PostOrder } from "./models";
import { firstValueFrom } from "rxjs";

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
  postOrder(po: PostOrder) {
    return firstValueFrom(this.http.post<PostOrder>('/api/food_order', po))
  }
}