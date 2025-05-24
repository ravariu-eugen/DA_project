kubectl create ns kafka

helm install my-kafka -n=kafka  -f ../values/kafka-values.yaml bitnami/kafka

kubectl create ns monitoring

helm install prometheus -n=monitoring -f ../values/prometheus-values.yaml bitnami/prometheus

helm install grafana -n=monitoring -f ../values/grafana-values.yaml bitnami/grafana

helm install kafka-ui -n=monitoring -f ../values/kafka-ui-values.yaml kafka-ui/kafka-ui


::kubectl create ns litmus
::helm install chaos litmuschaos/litmus --namespace=litmus ^
::--set portal.frontend.service.type=NodePort ^
::--set portal.server.graphqlServer.genericEnv.CHAOS_CENTER_UI_ENDPOINT=http://chaos-litmus-frontend-service.litmus.svc.cluster.local:9091


kubectl create ns chaos-mesh
helm install chaos-mesh chaos-mesh/chaos-mesh -n=chaos-mesh   --set dashboard.securityMode=false