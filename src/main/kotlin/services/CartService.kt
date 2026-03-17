package services

import models.CartItem
import models.Product

object CartService {

    private val items: MutableList<CartItem> = mutableListOf()

    // Devuelve la lista en modo solo lectura para los demás módulos
    fun getItems(): List<CartItem> = items

    fun addProduct(product: Product, quantity: Int) {
        // Si ya está en el carrito, solo aumenta la cantidad
        val existing = items.find { it.product.id == product.id }
        if (existing != null) {
            existing.quantity += quantity
        } else {
            items.add(CartItem(product, quantity))
        }
        // Descuenta del inventario
        product.stock -= quantity
    }

    fun removeProduct(productId: Int, quantityToRemove: Int) {
        val item = items.find { it.product.id == productId }
        if (item == null) {
            println("Ese producto no está en el carrito.")
            return
        }

        when {
            quantityToRemove >= item.quantity -> {
                // Elimina el producto completo
                item.product.stock += item.quantity
                items.remove(item)
                println("\"${item.product.name}\" eliminado del carrito.")
            }
            else -> {
                // Solo reduce la cantidad
                item.quantity -= quantityToRemove
                item.product.stock += quantityToRemove
                println("Se quitaron $quantityToRemove unidad(es) de \"${item.product.name}\".")
            }
        }
    }

    fun updateQuantity(productId: Int, newQuantity: Int) {
        val item = items.find { it.product.id == productId }
        if (item != null) {
            val diff = newQuantity - item.quantity
            // Verifica que haya suficiente stock
            if (diff > item.product.stock) {
                println("Stock insuficiente. Disponible: ${item.product.stock}")
                return
            }
            item.product.stock -= diff
            item.quantity = newQuantity
            println("Cantidad actualizada.")
        } else {
            println("Ese producto no está en el carrito.")
        }
    }

    fun getTotal(): Double = items.sumOf { it.subtotal }

    // Imprime el carrito en consola
    fun displayCart() {
        if (items.isEmpty()) {
            println("\nEl carrito está vacío.")
            return
        }
        println("\n%-4s %-25s %8s %10s %12s".format("ID", "Producto", "Cant.", "P. Unit.", "Subtotal"))
        println("-".repeat(63))
        items.forEach { item ->
            println(
                "%-4d %-25s %8d %9.2f %11.2f".format(
                    item.product.id,
                    item.product.name,
                    item.quantity,
                    item.product.price,
                    item.subtotal
                )
            )
        }
        println("-".repeat(63))
        println("%49s $%10.2f".format("TOTAL:", getTotal()))
    }

    fun clear() = items.clear()
}