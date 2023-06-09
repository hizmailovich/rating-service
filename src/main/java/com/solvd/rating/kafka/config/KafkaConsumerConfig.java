package com.solvd.rating.kafka.config;

import com.solvd.rating.kafka.parser.XmlParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ReceiverOptions<String, Long> receiverOptions() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, XmlParser.getValue("groupId"));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, XmlParser.getValue("keyDeserializer"));
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, XmlParser.getValue("valueDeserializer"));
        ReceiverOptions<String, Long> receiverOptions = ReceiverOptions.create(props);
        return receiverOptions
                .subscription(Collections.singleton(XmlParser.getValue("topic")))
                .addAssignListener(receiverPartitions -> log.info("Assigned: " + receiverPartitions))
                .addRevokeListener(receiverPartitions -> log.info("Revoked: " + receiverPartitions));
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public KafkaReceiver<String, Long> kafkaReceiver() {
        return KafkaReceiver.create(receiverOptions());
    }

}
