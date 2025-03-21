// You may use this file to create any models
export interface Menu {
    id: string
    name: string
    description: string
    price: number
    quantity: number        // for Order
}

export interface OrderItem {
    id: string
    price: number
    quantity: number
}

export interface PostOrder {
    username: string
    password: string
    items: OrderItem[]
}