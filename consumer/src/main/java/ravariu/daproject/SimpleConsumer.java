package ravariu.daproject;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ravariu.daproject.myapps.Utils;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SimpleConsumer {

	private static final Logger log = LoggerFactory.getLogger(SimpleConsumer.class);

	public void run() {
		String topic = "character-count";
		String groupId = "your-consumer-group";

		// Set consumer properties
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Utils.getBootstrapServers());
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		// Create Kafka consumer

		// Subscribe to topic
		//KafkaUtils.createTopic(topic, 1, (short) 1);

		try (KafkaConsumer<String, Long> consumer = new KafkaConsumer<>(properties, new StringDeserializer(), new LongDeserializer())) {
			consumer.subscribe(List.of(topic));
			while (true) {
				var records = consumer.poll(Duration.ofMillis(100));
				if (records.count() != 0) {
					log.info("Received {} records", records.count());
				}

				var recordsList = records.records(topic);

				var rl = StreamSupport.stream(recordsList.spliterator(), false)
						.sorted(Comparator.comparing(ConsumerRecord::key))
						.collect(Collectors.toList());

				for (var record : rl) {
					var key = record.key();
					var value = record.value();
					log.info("Received message: key = {}, value = {}, partition = {}, offset = {} %n",
							key, value, record.partition(), record.offset());
				}
			}
		}
	}
}
