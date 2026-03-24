import java.io.PrintStream
import services.CartService
import services.InvoiceService
import utils.Inventory
import utils.InputValidator
import utils.AppLogger

fun main() {
    System.setOut(PrintStream(System.out, true, "UTF-8"))
    System.setIn(System.`in`.buffered())

    AppLogger.logger.info("Aplicación iniciada")
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

        val option = InputValidator.readMenuOption("Elige una opción: ", 0..7)

        if (option == null) {
            println("Opción no válida, intenta de nuevo.")
            AppLogger.logger.warning("Opción de menú inválida")
            continue
        }

        when (option) {
            1 -> {
                AppLogger.logger.info("Usuario consultó productos")
                Inventory.displayAll()
            }

            2 -> {
                Inventory.displayAll()

                val id = InputValidator.readPositiveInt("\nID del producto: ")
                if (id == null) {
                    println("ID inválido.")
                    AppLogger.logger.warning("ID inválido al agregar producto")
                    continue
                }

                val product = Inventory.findById(id)

                if (product == null) {
                    println("Producto no encontrado.")
                    AppLogger.logger.warning("Producto no encontrado. ID: $id")
                } else if (product.stock == 0) {
                    println("Sin stock disponible.")
                    AppLogger.logger.warning("Producto sin stock. ID: ${product.id}")
                } else {
                    val qty = InputValidator.readPositiveInt("Cantidad (disponible: ${product.stock}): ")

                    if (qty == null) {
                        println("Cantidad inválida.")
                        AppLogger.logger.warning("Cantidad inválida al agregar producto")
                    } else if (qty > product.stock) {
                        println("Stock insuficiente. Máximo: ${product.stock}")
                        AppLogger.logger.warning("Intento de agregar más stock del disponible para ${product.name}")
                    } else {
                        CartService.addProduct(product, qty)
                        println("\"${product.name}\" agregado al carrito.")
                        AppLogger.logger.info("Producto agregado: ${product.name}, cantidad: $qty")
                    }
                }
            }

            3 -> {
                AppLogger.logger.info("Usuario visualizó el carrito")
                CartService.displayCart()
            }

            4 -> {
                CartService.displayCart()

                if (CartService.getItems().isNotEmpty()) {
                    val id = InputValidator.readPositiveInt("\nID del producto a eliminar: ")

                    if (id == null) {
                        println("ID inválido.")
                        AppLogger.logger.warning("ID inválido al eliminar producto")
                        continue
                    }

                    val item = CartService.getItems().find { it.product.id == id }

                    if (item == null) {
                        println("Ese producto no está en el carrito.")
                        AppLogger.logger.warning("Intento de eliminar producto no existente en carrito. ID: $id")
                    } else {
                        println("\"${item.product.name}\" — tienes ${item.quantity} en el carrito.")

                        val qty = InputValidator.readPositiveInt("¿Cuántos quieres eliminar? (1-${item.quantity}): ")

                        if (qty == null) {
                            println("Cantidad inválida.")
                            AppLogger.logger.warning("Cantidad inválida al eliminar producto")
                        } else if (qty > item.quantity) {
                            println("No puedes eliminar más de los que tienes (${item.quantity}).")
                            AppLogger.logger.warning("Intento de eliminar más cantidad de la disponible en carrito")
                        } else {
                            CartService.removeProduct(id, qty)
                            AppLogger.logger.info("Producto eliminado/reducido del carrito. ID: $id, cantidad: $qty")
                        }
                    }
                }
            }

            5 -> {
                CartService.displayCart()

                if (CartService.getItems().isNotEmpty()) {
                    val id = InputValidator.readPositiveInt("\nID del producto a actualizar: ")
                    val qty = InputValidator.readPositiveInt("Nueva cantidad: ")

                    if (id != null && qty != null) {
                        CartService.updateQuantity(id, qty)
                        AppLogger.logger.info("Cantidad actualizada en carrito. ID: $id, nueva cantidad: $qty")
                    } else {
                        println("Datos inválidos.")
                        AppLogger.logger.warning("Datos inválidos al actualizar cantidad")
                    }
                }
            }

            6 -> {
                if (CartService.getItems().isEmpty()) {
                    println("El carrito está vacío, no hay nada que confirmar.")
                    AppLogger.logger.warning("Intento de confirmar compra con carrito vacío")
                } else {
                    var customerEmail: String
                    do {
                        print("Ingrese el correo del cliente: ")
                        customerEmail = readlnOrNull()?.trim().orEmpty()

                        if (!InputValidator.isValidEmail(customerEmail)) {
                            println("Correo inválido. Use el formato: usuario@dominio.com")
                            AppLogger.logger.warning("Correo inválido ingresado: $customerEmail")
                        }
                    } while (!InputValidator.isValidEmail(customerEmail))

                    AppLogger.logger.info("Compra confirmada para correo: $customerEmail")
                    InvoiceService.confirmPurchase(customerEmail)
                }
            }

            7 -> {
                if (CartService.getItems().isEmpty()) {
                    println("El carrito ya se encuentra vacío.")
                    AppLogger.logger.info("Intento de vaciar carrito ya vacío")
                } else {
                    CartService.displayCart()
                    println("\n¿Deseas vaciar todo el carrito?")
                    println("1. Sí")
                    println("2. No")

                    val confirm = InputValidator.readMenuOption("Elige una opción: ", 1..2)

                    when (confirm) {
                        1 -> {
                            CartService.clear()
                            println("Carrito vaciado correctamente.")
                            AppLogger.logger.info("Carrito vaciado correctamente")
                        }

                        2 -> {
                            println("Operación cancelada.")
                            AppLogger.logger.info("Usuario canceló vaciado de carrito")
                        }

                        else -> {
                            println("Opción no válida. Operación cancelada.")
                            AppLogger.logger.warning("Opción inválida en confirmación de vaciado")
                        }
                    }
                }
            }

            0 -> {
                println("¡Hasta luego!")
                AppLogger.logger.info("Aplicación finalizada por el usuario")
                running = false
            }
        }
    }
}