FROM apache/kafka:4.0.0


ADD jmx_exporter/prom-jmx-agent-config.yaml /usr/app/prom-jmx-agent-config.yml
ADD ./jmx_exporter/jmx_prometheus_javaagent-1.2.0.jar /usr/app/jmx_prometheus_javaagent.jar

ENV KAFKA_JMX_OPTS="-Dcom.sun.management.jmxremote=true \
                  -Dcom.sun.management.jmxremote.authenticate=false \
                  -Dcom.sun.management.jmxremote.ssl=false \
                  -Djava.rmi.server.hostname=kafka \
                  -Djava.net.preferIPv4Stack=true \
                  -Dcom.sun.management.jmxremote.rmi.port=7070 \
                  -Dcom.sun.management.jmxremote.local.only=false \
                  -javaagent:/usr/app/jmx_prometheus_javaagent-1.2.0.jar=kafka:7071:/usr/app/prom-jmx-agent-config.yaml"


ENV KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR="1"
ENV KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS="0"
ENV KAFKA_TRANSACTION_STATE_LOG_MIN_ISR="1"
ENV KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR="1"
ENV KAFKA_SHARE_COORDINATOR_STATE_TOPIC_REPLICATION_FACTOR="1"
ENV KAFKA_SHARE_COORDINATOR_STATE_TOPIC_MIN_ISR="1"
ENV KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"

ENV ALLOW_PLAINTEXT_LISTENER="yes"
ENV KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR="1"
ENV KAFKA_TRANSACTION_STATE_LOG_MIN_ISR="1"

ENV KAFKA_LISTENER_SECURITY_PROTOCOL_MAP="CONTROLLER:PLAINTEXT,DOCKER:PLAINTEXT,HOST:PLAINTEXT"
ENV KAFKA_PROCESS_ROLES="broker,controller"
ENV KAFKA_CONTROLLER_LISTENER_NAMES="CONTROLLER"
ENV KAFKA_INTER_BROKER_LISTENER_NAME="DOCKER"
