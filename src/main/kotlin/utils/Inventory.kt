package utils

import models.Product

object Inventory {
    val products: MutableList<Product> = mutableListOf(
        Product(1,  "Arroz (1 lb)",          0.85,  50),
        Product(2,  "Frijoles (1 lb)",        1.10,  40),
        Product(3,  "Aceite vegetal (1 L)",   2.50,  30),
        Product(4,  "Azúcar (1 lb)",          0.75,  60),
        Product(5,  "Sal (500 g)",            0.50,  45),
        Product(6,  "Leche (1 L)",            1.20,  25),
        Product(7,  "Huevos (cartón x12)",    2.80,  20),
        Product(8,  "Pan francés (unidad)",   0.15, 100),
        Product(9,  "Café molido (200 g)",    3.50,  15),
        Product(10, "Jabón de baño (unidad)", 0.90,  35)
    )

    fun findById(id: Int): Product? = products.find { it.id == id }

    fun displayAll() {
        println("\n%-4s %-25s %8s %8s".format("ID", "Producto", "Precio", "Stock"))
        println("-".repeat(50))
        products.forEach { p ->
            println("%-4d %-25s $%7.2f %7d".format(p.id, p.name, p.price, p.stock))
        }
    }
}
