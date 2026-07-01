package com.example.barberbooking.service;

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

    public void sendPasswordResetEmail(String toEmail, String resetLink ) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(fromEmail);
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Reset Password - BarberBooking");

        mailMessage.setText("""
                            Hai richiesto il reset della password.
                            
                            Clicca sul link seguente: 
                            
                            %s
                            
                            Se non hai richiesto il reset della password puoi ignorare questa mail.
                
                """.formatted(resetLink));

        mailSender.send(mailMessage);

    }




}
