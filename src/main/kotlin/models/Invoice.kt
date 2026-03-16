package models
import java.time.LocalDateTime

data class Invoice (
    val invoiceNumber: Int,
    val items: List< CartItem>,
    val createdAt: LocalDateTime= LocalDateTime.now(),
    val customerEmail: String
) {
    val subtotal: Double
        get()= items.sumOf { it.subtotal }
    val tax: Double
        get()= subtotal* 0.13 // 13% de IVA (EL SALVADOR)
    val total: Double
        get()= subtotal + tax

}