package utils

object InputValidator {

    fun readMenuOption(prompt: String, validRange: IntRange): Int? {
        print(prompt)
        val input = readlnOrNull()?.trim()?.toIntOrNull() ?: return null
        return if (input in validRange) input else null
    }

    fun readPositiveInt(prompt: String): Int? {
        print(prompt)
        val input = readlnOrNull()?.trim()?.toIntOrNull() ?: return null
        return if (input > 0) input else null
    }

    fun isValidEmail(email: String): Boolean {
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return regex.matches(email)
    }
}