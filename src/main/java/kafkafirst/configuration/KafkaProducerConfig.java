package kafkafirst.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

	@Value("${spring.kafka.producer.bootstrap-servers}")
	public String BOOTSTRAP_SERVER;

	@Value("${spring.kafka.producer.client-id}")
	public String CLIENT_ID;

	@Value("${spring.kafka.producer.request-timeout}")
	public String REQUEST_TIMEOUT;

	@Value("${spring.kafka.producer.deliver-timeout}")
	public String DELIVER_TIMEOUT;

	@Value("${spring.kafka.producer.acks}")
	public String ACKS;

	@Value("${spring.kafka.template.default-topic}")
	public String TOPIC_NAME;

	private Map<String, Object> senderProps() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
		props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, REQUEST_TIMEOUT);
		props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, DELIVER_TIMEOUT);
		props.put(ProducerConfig.ACKS_CONFIG, ACKS);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		return props;
	}

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		return new DefaultKafkaProducerFactory<>(senderProps());
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		KafkaTemplate<String, String> template = new KafkaTemplate<>(producerFactory());
		template.setDefaultTopic(TOPIC_NAME);
		return template;
	}

}
