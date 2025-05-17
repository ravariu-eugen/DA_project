helm install my-kafka  -f kafka-values.yaml bitnami/kafka

helm install prometheus -f prometheus-values.yaml bitnami/prometheus

helm install grafana -f grafana-values.yaml bitnami/grafana

helm install kafka-ui -f kafka-ui-values.yaml kafka-ui/kafka-ui