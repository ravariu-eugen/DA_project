kubectl port-forward svc/chaos-litmus-frontend-service -n litmus 9091:9091


kubectl port-forward svc/chaos-dashboard  -n chaos-mesh 2333:2333