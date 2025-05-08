package ravariu.daproject;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Random;

public class LogGeneratorApp {

	private static final String[] LEVELS = {"INFO", "WARN", "ERROR"};
	private static final String[] MESSAGES = {
			"Service started",
			"Connection timeout",
			"User logged in",
			"Null pointer exception",
			"Database connection failed",
			"Disk space low"
	};

	public static void main(String[] args) throws InterruptedException {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		KafkaProducer<String, String> producer = new KafkaProducer<>(props);
		Random random = new Random();
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

		while (true) {
			String timestamp = LocalDateTime.now().format(formatter);
			String level = LEVELS[random.nextInt(LEVELS.length)];
			String message = MESSAGES[random.nextInt(MESSAGES.length)];

			String log = String.format("%s|%s|%s", timestamp, level, message);
			producer.send(new ProducerRecord<>("raw-logs", null, log));

			System.out.println("Produced log: " + log);

			Thread.sleep(2000); // every 2 seconds
		}
	}
}
