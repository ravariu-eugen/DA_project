package ravariu.daproject;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import ravariu.daproject.myapps.KafkaUtils;
import ravariu.daproject.myapps.Utils;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;


public class ConsumerDemo {

	public static void main(String[] args) {

		new ConsumerDemo().run();
	}


	private Properties createProperties() {
		Properties properties = new Properties();
		properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Utils.getBootstrapServers());
		properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
		properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		return properties;
	}

	public void run() {


		StreamsBuilder builder = new StreamsBuilder();
		var props = createProperties();

		KafkaUtils.createTopic("WordsWithCountsTopic", 1, (short) 1);

		KStream<String, String> textLines = builder.stream("quickstart-events");

		KTable<String, Long> wordCounts = textLines
				.flatMapValues(textLine -> {
					System.out.println(textLine);

					return Arrays.asList(textLine.toLowerCase().split("\\W+"));
				})
				.groupBy((key, word) -> word)
				.count(Materialized.as("counts-store"));
		wordCounts.toStream().to("WordsWithCountsTopic");

		try (KafkaStreams streams = new KafkaStreams(builder.build(), props)) {
			streams.start();
			System.out.println("Kafka Streams started");

			final CountDownLatch latch = new CountDownLatch(1);
			// attach shutdown handler to catch control-c
			Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
				@Override
				public void run() {
					streams.close();
					latch.countDown();
				}
			});

			try {
				streams.start();
				latch.await();
			} catch (InterruptedException e) {
				System.exit(1);
			}
		}


		System.exit(0);
		System.out.println("end");
	}
}
