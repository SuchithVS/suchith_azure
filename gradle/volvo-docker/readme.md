

### to build Image
docker build -t volvo-demo-app .    
### To run container
docker run volvo-demo-app      
### to show image
docker images volvo-demo-app  


### Optional Docker Hub
docker tag volvo-demo-app:latest suchithvs/volvo-demo-app:latest 
docker push suchithvs/volvo-demo-app:latest


brew install kubectl


### Create a Kubernetes Deployment YAML file:  android-app-deployment.yaml 

### Create a Kubernetes Service YAML file:  android-app-service.yaml 


kubectl apply -f volvo-deployment.yaml
kubectl apply -f volvo-service.yaml


kubectl get deployments
kubectl get pods
kubectl get services

kubectl logs $(kubectl get pods -l app=volvo-demo-app -o jsonpath="{.items[0].metadata.name}")

