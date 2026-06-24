package org.example.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    @Value("${app.mail.from}")
     private String fromEmail;
    public void sendPasswordResetEmail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Reset Password Todo App");
        message.setText("""
                Ciao,

                hai richiesto il reset della password.

                Clicca su questo link per impostare una nuova password:

                %s

                Il link scade tra 15 minuti.

                Se non hai richiesto tu il reset, ignora questa email.
                """.formatted(resetLink));

        mailSender.send(message);

    }

}
