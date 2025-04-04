package ravariu.daproject;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ravariu.daproject.myapps.Utils;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;


public class Pipe {

	public static void main(String[] args) {
		new Pipe().run();
	}

	private static final Logger log = LoggerFactory.getLogger(Pipe.class);


	public void run() {
		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Utils.getBootstrapServers());
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		final StreamsBuilder builder = new StreamsBuilder();

		var input_topic = "random-strings";
		var output_topic = "character-count";


		builder.<String, String>stream(input_topic).flatMapValues(value -> {
					System.out.println(value);
					return List.of(value.split(""));
				})
				.groupBy((key, value) -> value)
				.count(Materialized.as("counts-store"))
				.toStream()
				.map((String key, Long value) -> {
					log.info("key = {}, value = {}", key, value);
					return new KeyValue<>(key, value);
				})
				.to(output_topic, Produced.with(Serdes.String(), Serdes.Long()));

		final Topology topology = builder.build();

		System.out.println(topology.describe());
		final KafkaStreams streams = new KafkaStreams(topology, props);
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


		System.exit(0);
	}

}
