package models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Invoice(
    val invoiceNumber: Int,
    val items: List<CartItem>,
    val createdAt: String = LocalDateTime.now().format(
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    ),
    val customerEmail: String
) {
    val subtotal: Double
        get() = items.sumOf { it.subtotal }

    val tax: Double
        get() = subtotal * 0.13 // 13% de IVA (EL SALVADOR)

    val total: Double
        get() = subtotal + tax
}
