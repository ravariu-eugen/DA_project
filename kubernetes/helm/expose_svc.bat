kubectl port-forward -n monitoring svc/grafana 3000

kubectl port-forward -n monitoring svc/prometheus-server 8081:80

kubectl port-forward -n monitoring svc/kafka-ui 8080:80

kubectl port-forward -n chaos-mesh svc/chaos-dashboard 2333:2333