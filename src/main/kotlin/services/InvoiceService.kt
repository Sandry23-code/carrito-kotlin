package services

import models.Invoice

object InvoiceService {

    private var invoiceCounter = 1

    fun generateInvoice(customerEmail: String): Invoice {
        val items = CartService.getItems()

        return Invoice(
            invoiceNumber = invoiceCounter++,
            items = items.map { it.copy() },
            customerEmail = customerEmail
        )
    }

    fun printInvoice(invoice: Invoice) {
        println("\n==============================================")
        println("                FACTURA DE COMPRA             ")
        println("==============================================")
        println("Factura No.: ${invoice.invoiceNumber}")
        println("Fecha      : ${invoice.createdAt}")
        println("Correo     : ${invoice.customerEmail}")
        println("----------------------------------------------")
        println("%-4s %-25s %8s %10s %12s".format("ID", "Producto", "Cant.", "P.Unit", "Subtotal"))
        println("-".repeat(65))

        invoice.items.forEach { item ->
            println(
                "%-4d %-25s %8d %10.2f %12.2f".format(
                    item.product.id,
                    item.product.name,
                    item.quantity,
                    item.product.price,
                    item.subtotal
                )
            )
        }

        println("-".repeat(65))
        println("%52s %12.2f".format("Subtotal:", invoice.subtotal))
        println("%52s %12.2f".format("IVA 13%:", invoice.tax))
        println("%52s %12.2f".format("Total:", invoice.total))
        println("==============================================")
    }

    fun confirmPurchase(customerEmail: String) {
        val items = CartService.getItems()

        if (items.isEmpty()) {
            println("El carrito está vacío, no hay nada que confirmar.")
            return
        }

        val invoice = generateInvoice(customerEmail)
        printInvoice(invoice)
        CartService.clear()
        println("Compra confirmada con éxito. El carrito ha sido vaciado.")
    }
}