package com.example.emailservice.producers;

import com.example.emailservice.dtos.SendEmailDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SendEmailProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    SendEmailProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public String sendEmail(SendEmailDto sendEmailDto) throws JsonProcessingException {
        kafkaTemplate.send("sendEmail", objectMapper.writeValueAsString(sendEmailDto));
        return "Email sent";
    }
}
