
helm upgrade my-kafka -n=kafka  -f ../values/kafka-values.yaml bitnami/kafka

helm upgrade prometheus -n=monitoring -f ../values/prometheus-values.yaml bitnami/prometheus

helm upgrade grafana -n=monitoring -f ../values/grafana-values.yaml bitnami/grafana

helm upgrade kafka-ui -n=monitoring -f ../values/kafka-ui-values.yaml kafka-ui/kafka-ui

helm upgrade chaos-mesh chaos-mesh/chaos-mesh -n=chaos-mesh   --set dashboard.securityMode=false