import { Component, inject, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant.service';
import { Receipt } from '../models';

@Component({
  selector: 'app-confirmation',
  standalone: false,
  templateUrl: './confirmation.component.html',
  styleUrl: './confirmation.component.css'
})
export class ConfirmationComponent implements OnInit {
  private restaurantSvc = inject(RestaurantService)
  // TODO: Task 5

  receipt!: Receipt

  ngOnInit(): void {
    this.receipt = this.restaurantSvc.receipt
  }
}
