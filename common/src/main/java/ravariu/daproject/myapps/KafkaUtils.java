package ravariu.daproject.myapps;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class KafkaUtils {
	private static final Logger log = LoggerFactory.getLogger(KafkaUtils.class);

	public static void createTopic(String topicName, int partitions, short replicationFactor) {
		try (AdminClient adminClient = AdminClient.create(Utils.createProperties())) {
			var topics = adminClient.listTopics().names().get();
			if (!topics.contains(topicName)) {
				NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
				var result = adminClient.createTopics(List.of(newTopic));
				result.all().get();
				log.info("Topic {} created", topicName);
			} else {

				log.warn("Topic {} already exists", topicName);
			}


		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		log.info("Creating topic quickstart-events");
	}
}
