import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant.service';
import { Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { Menu, PostOrder } from '../models';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent implements OnInit {
  // Task 3
  private fb = inject(FormBuilder)
  private restaurantSvc = inject(RestaurantService)
  private router = inject(Router)

  form!: FormGroup
  order: Menu[] = []
  totalPrice: number = 0

  orderSub$!: Subscription

  ngOnInit(): void {
    this.form = this.createForm()

    this.order = this.restaurantSvc.order
    this.totalPrice = this.restaurantSvc.totalOrderPrice
  }
  createForm() {
    return this.fb.group({
      username: this.fb.control<string>('', [ Validators.required ]),
      password: this.fb.control<string>('', [ Validators.required ])
    })
  }

  startOver() {
    // Reset 
    this.restaurantSvc.clearOrder()
    // Switch view
    this.router.navigate(['/'])
  }

  placeOrder() {
    let po: PostOrder = {
      username: this.form.value['username'],
      password: this.form.value['password'],
      items: []
    } 

    // Loop thru this.order and copy over its items to po.items
    this.order.forEach(item => {
      const orderItem = {
        id: item.id,
        price: item.price,
        quantity: item.quantity
      }
      po.items.push(orderItem)
    });

    this.restaurantSvc.postOrder(po).then(
      response => {
        // Make payment if postOrder is successful
        this.restaurantSvc.postPayment(response.orderId, this.form.value['username'])
      }
    )
    .catch(
      error => {
        console.info(JSON.stringify(error.error))
      }
    )
  }
}
