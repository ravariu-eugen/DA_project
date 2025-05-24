kubectl port-forward -n monitoring svc/grafana 3000
kubectl port-forward -n monitoring svc/prometheus-server 8080:80


kubectl port-forward -n monitoring svc/kafka-ui 8080:80