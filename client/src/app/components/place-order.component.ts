import { Component, inject, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant.service';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Menu } from '../models';
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

  }
}
