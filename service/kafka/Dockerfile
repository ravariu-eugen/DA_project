FROM apache/kafka:4.0.0


ADD prom-jmx-agent-config.yaml /usr/app/prom-jmx-agent-config.yml
ADD ./jmx_exporter/jmx_prometheus_javaagent-1.2.0.jar /usr/app/jmx_prometheus_javaagent.jar

ENV JMX_PORT=7071
ENV KAFKA_JMX_OPTS="-Dcom.sun.management.jmxremote=true \
-Dcom.sun.management.jmxremote.authenticate=false \
-Dcom.sun.management.jmxremote.ssl=false \
-Djava.rmi.server.hostname=kafka \
-Djava.net.preferIPv4Stack=true \
-Dcom.sun.management.jmxremote.port=7071 \
-Dcom.sun.management.jmxremote.local.only=false \
-javaagent:/usr/app/jmx_prometheus_javaagent.jar=kafka:7071:/usr/app/prom-jmx-agent-config.yml"