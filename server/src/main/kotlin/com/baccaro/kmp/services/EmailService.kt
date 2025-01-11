package com.baccaro.kmp.services

import Worker
import kotlinx.html.body
import kotlinx.html.p
import kotlinx.html.stream.createHTML
import org.simplejavamail.api.mailer.config.TransportStrategy
import org.simplejavamail.email.EmailBuilder
import org.simplejavamail.mailer.MailerBuilder


// ... (Otras importaciones)


class EmailService(private val config: EmailConfig) { // Recibe la configuración del email

    data class EmailConfig(
        val host:String, val port:Int, val username:String, val password:String, val from:String
    )


    suspend fun sendWorkerValidationEmail(worker: Worker, validated: Boolean) {
        val subject = if (validated) "Tu cuenta ha sido validada" else "Tu cuenta está en proceso de validación"
        val body = createHTML().body {
            p { +"Hola ${worker.usuario.nombre}," }
            if (validated) {
                p { +"Tu cuenta de worker en KonectAR ha sido validada. Ya puedes comenzar a recibir solicitudes de servicio." }
            } else {
                p { +"Tu cuenta de worker en KonectAR está en proceso de validación. Te notificaremos cuando esté lista." }
            }
            // ... (Más contenido del email)
        }


        val email = EmailBuilder.startingBlank()
            .from(config.from)
            .to(worker.usuario.correoElectronico)
            .withSubject(subject)
            .withHTMLText(body)
            .buildEmail()

        val mailer = MailerBuilder
            .withSMTPServer(config.host, config.port, config.username, config.password)
            .withTransportStrategy(TransportStrategy.SMTP_TLS) // O SMTP si no usas TLS
            .buildMailer()
        mailer.sendMail(email)
    }
}