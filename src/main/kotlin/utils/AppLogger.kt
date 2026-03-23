package utils

import java.io.File
import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

object AppLogger {
    val logger: Logger = Logger.getLogger("CarritoApp").apply {
        useParentHandlers = false

        if (handlers.isEmpty()) {
            File("logs").mkdirs()
            val fileHandler = FileHandler("logs/app.txt", true)
            fileHandler.formatter = SimpleFormatter()
            addHandler(fileHandler)
        }
    }
}

