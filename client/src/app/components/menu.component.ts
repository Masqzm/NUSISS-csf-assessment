import { Component, inject, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant.service';
import { Menu } from '../models';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit {
  // Task 2
  private restaurantSvc = inject(RestaurantService)

  menus$!: Observable<Menu[]>

  order: Menu[] = []
  totalPrice: number = 0
  totalQty: number = 0

  ngOnInit(): void {
    this.menus$ = this.restaurantSvc.getMenuItems()
  }

  addItem(selectedItem: Menu) {
    // Check if order alrd have the selected item 
    const index = this.order.findIndex(item => item.name === selectedItem.name)
    
    if(index > -1)  // item alrd exist in order
      this.order[index].quantity += 1
    else
    {
      selectedItem.quantity = 1     // init to 1 (first of this menu item added)
      this.order.push(selectedItem)
    }
    
    // Update displays
    this.totalPrice += selectedItem.price
    this.totalQty += 1
  }

  removeItem(selectedItem: Menu) {
    // Check if order alrd have the selected item 
    const index = this.order.findIndex(item => item.name === selectedItem.name)
      
    if(index > -1)  // item not yet in order
    {
      this.order[index].quantity -= 1

      // remove item from order if qty is <= 0
      if(this.order[index].quantity <= 0)
        this.order.splice(index, 1)

      this.totalPrice -= selectedItem.price
      this.totalQty -= 1
    }
  }
}
