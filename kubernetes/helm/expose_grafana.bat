kubectl port-forward svc/grafana -n monitoring 3000
kubectl port-forward svc/prometheus-server -n monitoring 8080:80


kubectl port-forward svc/kafka-ui -n monitoring 8080:80