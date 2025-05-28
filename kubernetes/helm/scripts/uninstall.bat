helm delete -n kafka my-kafka
kubectl delete -n kafka -f ../values/kafka-metrics-configmap.yaml
helm delete -n monitoring prometheus

helm delete -n monitoring grafana

helm delete -n monitoring kafka-ui

helm uninstall chaos-mesh -n chaos-mesh