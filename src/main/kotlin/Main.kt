import java.io.PrintStream
import services.CartService
import services.InvoiceService
import utils.Inventory


fun isValidEmail(email: String): Boolean {
    val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return regex.matches(email)
}

fun main() {
    System.setOut(PrintStream(System.out, true, "UTF-8"))
    System.setIn(System.`in`.buffered())
    println("=== Bienvenido ===")

    var running = true

    while (running) {
        println("""
            
            ╔══════════════════════════╗
            ║         MENÚ             ║
            ╠══════════════════════════╣
            ║ 1. Ver productos         ║
            ║ 2. Agregar al carrito    ║
            ║ 3. Ver carrito           ║
            ║ 4. Eliminar del carrito  ║
            ║ 5. Actualizar cantidad   ║
            ║ 6. Confirmar compra      ║
            ║ 7. Vaciar carrito        ║
            ║ 0. Salir                 ║
            ╚══════════════════════════╝
        """.trimIndent())

        print("Elige una opción: ")
        val option = readlnOrNull()?.trim()?.toIntOrNull()

        when (option) {
            1 -> Inventory.displayAll()

            2 -> {
                Inventory.displayAll()
                print("\nID del producto: ")
                val id = readlnOrNull()?.trim()?.toIntOrNull()
                val product = if (id != null) Inventory.findById(id) else null

                if (product == null) {
                    println("Producto no encontrado.")
                } else if (product.stock == 0) {
                    println("Sin stock disponible.")
                } else {
                    print("Cantidad (disponible: ${product.stock}): ")
                    val qty = readlnOrNull()?.trim()?.toIntOrNull()
                    if (qty == null || qty <= 0) {
                        println("Cantidad inválida.")
                    } else if (qty > product.stock) {
                        println("Stock insuficiente. Máximo: ${product.stock}")
                    } else {
                        CartService.addProduct(product, qty)
                        println("\"${product.name}\" agregado al carrito.")
                    }
                }
            }

            3 -> CartService.displayCart()

            4 -> {
                CartService.displayCart()
                if (CartService.getItems().isNotEmpty()) {
                    print("\nID del producto a eliminar: ")
                    val id = readlnOrNull()?.trim()?.toIntOrNull()

                    if (id == null) {
                        println("ID inválido.")
                    } else {
                        val item = CartService.getItems().find { it.product.id == id }
                        if (item == null) {
                            println("Ese producto no está en el carrito.")
                        } else {
                            println("\"${item.product.name}\" — tienes ${item.quantity} en el carrito.")
                            print("¿Cuántos quieres eliminar? (1-${item.quantity}): ")
                            val qty = readlnOrNull()?.trim()?.toIntOrNull()

                            if (qty == null || qty <= 0) {
                                println("Cantidad inválida.")
                            } else if (qty > item.quantity) {
                                println("No puedes eliminar más de los que tienes (${item.quantity}).")
                            } else {
                                CartService.removeProduct(id, qty)
                            }
                        }
                    }
                }
            }

            5 -> {
                CartService.displayCart()
                if (CartService.getItems().isNotEmpty()) {
                    print("\nID del producto a actualizar: ")
                    val id = readlnOrNull()?.trim()?.toIntOrNull()
                    print("Nueva cantidad: ")
                    val qty = readlnOrNull()?.trim()?.toIntOrNull()
                    if (id != null && qty != null && qty > 0) {
                        CartService.updateQuantity(id, qty)
                    } else {
                        println("Datos inválidos.")
                    }
                }
            }

            6 -> {
                if (CartService.getItems().isEmpty()) {
                    println("El carrito está vacío, no hay nada que confirmar.")
                } else {
                    var customerEmail: String
                    do {
                        print("Ingrese el correo del cliente: ")
                        customerEmail = readln().trim()
                        if (!isValidEmail(customerEmail)) {
                            println("Correo inválido. Use el formato: usuario@dominio.com")
                        }
                    } while (!isValidEmail(customerEmail))
                    InvoiceService.confirmPurchase(customerEmail)
                }
            }

            7 -> {
                if (CartService.getItems().isEmpty()) {
                    println("El carrito ya se encuentra vacío.")
                } else {
                    CartService.displayCart()
                    println("\n¿Deseas vaciar todo el carrito?")
                    println("1. Sí")
                    println("2. No")
                    print("Elige una opción: ")

                    when (readlnOrNull()?.trim()?.toIntOrNull()) {
                        1 -> {
                            CartService.clear()
                            println("Carrito vaciado correctamente.")
                        }
                        2 -> println("Operación cancelada.")
                        else -> println("Opción no válida. Operación cancelada.")
                    }
                }
            }

            0 -> {
                println("¡Hasta luego!")
                running = false
            }

            else -> println("Opción no válida, intenta de nuevo.")
        }
    }
}