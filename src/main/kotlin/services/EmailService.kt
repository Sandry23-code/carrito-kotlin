package services

import models.Invoice
import java.io.File
import java.io.FileInputStream
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object EmailService {

    private val config: Properties by lazy { loadConfig() }

    private fun loadConfig(): Properties {
        val props = Properties()
        val configFile = File("email.properties")
        if (configFile.exists()) {
            FileInputStream(configFile).use { props.load(it) }
        } else {
            println("[AVISO] No se encontró 'email.properties' en la raíz del proyecto.")
            println("        Crea el archivo con los datos SMTP para activar el envío de correos.")
        }
        return props
    }

    private val smtpHost    get() = config.getProperty("smtp.host",     "smtp.gmail.com")
    private val smtpPort    get() = config.getProperty("smtp.port",     "587")
    private val senderEmail get() = config.getProperty("smtp.user",     "")
    private val senderPass  get() = config.getProperty("smtp.password", "")

    fun sendInvoice(invoice: Invoice) {
        if (senderEmail.isBlank() || senderPass.isBlank()) {
            println("\n[AVISO] Credenciales SMTP no configuradas en 'email.properties'.")
            println("        El correo NO fue enviado.")
            return
        }

        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", smtpHost)
            put("mail.smtp.port", smtpPort)
            put("mail.smtp.ssl.trust", smtpHost)
        }

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication() =
                PasswordAuthentication(senderEmail, senderPass)
        })

        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(senderEmail))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(invoice.customerEmail))
                subject = "Factura de Compra #${invoice.invoiceNumber}"
                setText(buildEmailBody(invoice), "UTF-8")
            }
            Transport.send(message)
            println("\n[OK] Factura enviada correctamente a: ${invoice.customerEmail}")
        } catch (e: MessagingException) {
            println("\n[ERROR] No se pudo enviar el correo: ${e.message}")
        }
    }

    private fun buildEmailBody(invoice: Invoice): String {
        val sb = StringBuilder()
        sb.appendLine("==============================================")
        sb.appendLine("           FACTURA DE COMPRA                 ")
        sb.appendLine("==============================================")
        sb.appendLine("Factura No. : ${invoice.invoiceNumber}")
        sb.appendLine("Fecha       : ${invoice.createdAt}")
        sb.appendLine("Correo      : ${invoice.customerEmail}")
        sb.appendLine("----------------------------------------------")
        sb.appendLine("%-4s %-25s %8s %10s %12s".format("ID", "Producto", "Cant.", "P.Unit", "Subtotal"))
        sb.appendLine("-".repeat(65))
        invoice.items.forEach { item ->
            sb.appendLine(
                "%-4d %-25s %8d %10.2f %12.2f".format(
                    item.product.id,
                    item.product.name,
                    item.quantity,
                    item.product.price,
                    item.subtotal
                )
            )
        }
        sb.appendLine("-".repeat(65))
        sb.appendLine("%52s %12.2f".format("Subtotal:", invoice.subtotal))
        sb.appendLine("%52s %12.2f".format("IVA 13%:", invoice.tax))
        sb.appendLine("%52s %12.2f".format("TOTAL:", invoice.total))
        sb.appendLine("==============================================")
        return sb.toString()
    }
}
