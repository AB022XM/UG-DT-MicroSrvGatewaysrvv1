package ug.co.absa.gatewaysrv.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ug.co.absa.gatewaysrv.broker.KafkaConsumer;

@RestController
@RequestMapping("/api/ugdt-micro-srv-gatewaysrvv-1-kafka")
public class UgdtMicroSrvGatewaysrvv1KafkaResource {

    private static final String PRODUCER_BINDING_NAME = "binding-out-0";

    private final Logger log = LoggerFactory.getLogger(UgdtMicroSrvGatewaysrvv1KafkaResource.class);
    private final KafkaConsumer kafkaConsumer;
    private final StreamBridge streamBridge;

    public UgdtMicroSrvGatewaysrvv1KafkaResource(StreamBridge streamBridge, KafkaConsumer kafkaConsumer) {
        this.streamBridge = streamBridge;
        this.kafkaConsumer = kafkaConsumer;
    }

    @PostMapping("/publish")
    public Mono<ResponseEntity<Void>> publish(@RequestParam String message) {
        log.debug("REST request the message : {} to send to Kafka topic", message);
        streamBridge.send(PRODUCER_BINDING_NAME, message);
        return Mono.just(ResponseEntity.noContent().build());
    }

    @GetMapping("/consume")
    public Flux<String> consume() {
        log.debug("REST request to consume records from Kafka topics");
        return this.kafkaConsumer.getFlux();
    }
}
