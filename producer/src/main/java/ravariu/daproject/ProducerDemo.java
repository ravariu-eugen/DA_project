package ravariu.daproject;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ravariu.daproject.myapps.Utils;

import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class ProducerDemo {
	private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class);


	public void run() {
		log.info("I am a Kafka Producer");

		var topic = "random-strings";

		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Utils.getBootstrapServers());
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		//KafkaUtils.createTopic(topic, 1, (short) 1);
		// create the producer
		try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {


			// send data - asynchronous
			while (true) {
				Thread.sleep(1000);

				String randomString = ThreadLocalRandom.current().ints(97, 122)
						.limit(20)
						.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
						.toString();
				ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, randomString);
				log.info("Key: {}, Value: {}", producerRecord.key(), producerRecord.value());
				producer.send(producerRecord);
				// flush data - synchronous
				producer.flush();
			}

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}


	public static void main(String[] args) {
		new ProducerDemo().run();
	}
}