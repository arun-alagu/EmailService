package com.example.emailservice.consumers;

import com.example.emailservice.dtos.SendEmailDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

//kafka-topics --create \
//--bootstrap-server localhost:9092 \
//--replication-factor 1 --partitions 1 \
//--topic sendEmail
@Component
public class SendEmailConsumer {
    private final ObjectMapper objectMapper;
    private final JavaMailSender mailSender;

    SendEmailConsumer(ObjectMapper objectMapper, JavaMailSender mailSender) {
        this.objectMapper = objectMapper;
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "sendEmail", groupId = "emailService")
    public void consume(String message) throws JsonProcessingException {
        SendEmailDto sendEmailDto = objectMapper.readValue(message, SendEmailDto.class);

        sendEmail(sendEmailDto.getTo(), sendEmailDto.getFrom(),
                sendEmailDto.getSubject(), sendEmailDto.getBody());
    }

    @Async
    public void sendEmail(String to, String from, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(from);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);

        System.out.println("To: " + to);
        System.out.println("From: " + from);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
    }
}
