docker build .\kafka -t kafka:v1
kubectl apply -f .\namespace.yaml

::kubectl apply -f .\basic\