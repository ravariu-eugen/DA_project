package ravariu.daproject;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ravariu.daproject.myapps.Utils;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ProducerDemo {
	private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class);


	private double getRate() {
		var messageRate = System.getenv().getOrDefault("MESSAGE_RATE", "1.0");
		try {
			return Double.parseDouble(messageRate);
		} catch (NumberFormatException e) {
			log.error("Invalid MESSAGE_RATE value: {}", messageRate);
			return 1.0;
		}
	}

	public void run() {
		log.info("I am a Kafka Producer");

		var topic = "random-strings";

		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Utils.getBootstrapServers());
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

//		KafkaUtils.createTopic(topic, 6, (short) 3);
		// create the producer

		long sleepTime = (long) (1000 / getRate());
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		KafkaProducer<String, String> producer = new KafkaProducer<>(properties);


		// send data - asynchronous

		executorService.scheduleAtFixedRate(() -> {
			try {
				String randomString = ThreadLocalRandom.current().ints(97, 122)
						.limit(20)
						.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
						.toString();
				ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, randomString);
				log.info("Key: {}, Value: {}", producerRecord.key(), producerRecord.value());
				producer.send(producerRecord);
				// flush data - synchronous
				producer.flush();
			} catch (Exception e) {
				log.error("Error in producer task", e);
			}
		}, 0, sleepTime, TimeUnit.MILLISECONDS);


	}


	public static void main(String[] args) {
		new ProducerDemo().run();
	}
}