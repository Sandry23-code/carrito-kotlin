package models

data class CartItem (
    val product: Product,
    var quantity: Int,
) {
    val subtotal: Double
        get()= product.price* quantity

}