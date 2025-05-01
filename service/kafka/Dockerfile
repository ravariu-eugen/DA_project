FROM apache/kafka:4.0.0



ADD ./jmx_exporter/kafka-jmx.yml /usr/app/prom-jmx-agent-config.yml
ADD https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.20.0/jmx_prometheus_javaagent-0.20.0.jar /usr/app/jmx_prometheus_javaagent.jar

ENV KAFKA_HEAP_OPTS="-javaagent:/usr/app/jmx_prometheus_javaagent.jar=7071:/usr/app/prom-jmx-agent-config.yml"