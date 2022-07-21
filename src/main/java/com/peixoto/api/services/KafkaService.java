package com.peixoto.api.services;

import com.peixoto.api.entities.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaService {

    private final String topic;
    private final KafkaTemplate<String, Book> kafkaTemplate;

    public KafkaService(@Value("${spring.kafka.topic-name}") String topic, KafkaTemplate<String, Book> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Book book){
        kafkaTemplate.send(topic, book).addCallback(
                success -> log.info("Message sent" + success.getProducerRecord().value()),
                failure -> log.info("Message failure" + failure.getMessage())
        );
        kafkaTemplate.flush();
    }
}
