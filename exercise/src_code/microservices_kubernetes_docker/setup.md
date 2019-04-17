## Set up for Java project - Microservices deployed using Kubernetes

----------

### Step 1: Update apt-get and install necessary packages
Update the apt-get package manager and install necessary packages in the machine
<br>
`sudo apt-get update && apt-get install -y maven git tree default-jdk`
<br>

### Step 2: Clone the git project
Clone the existing git project
<br>
`git clone https://github.com/techtown-training/microservices-bootcamp.git`
<br>

### Step 3: Maven build shopfront service and build docker image
Go to the shopfront project folder, build the maven project to create jar and build the docker image
<br>
`cd exercise/src_code/microservices_kubernetes_docker/shopfront/`
<br>
`mvn clean install -DskipTests`
<br>
`docker build -t shekhar/shopfront:1.0 .`
<br>

### Step 4: Maven build productcatalogue service and build docker image
Go to the productcatalogue project folder, build the maven project to create jar and build the docker image
<br>
`cd ../productcatalogue/`
<br>
`mvn clean install -DskipTests`
<br>
`docker build -t shekhar/productcatalogue:1.0 .`
<br>

### Step 5: Maven build stockmanager service and build docker image
Go to the stockmanager project folder, build the maven project to create jar and build the docker image
<br>
`cd ../stockmanager/`
<br>
`mvn clean install -DskipTests`
<br>
`docker build -t shekhar/stockmanager:1.0 .`
<br>

### Step 6: Check docker images and kubernetes resources
**-- Check all Docker images <br>**
`docker images | grep -i shekhar`
<br>
**-- Check all Kubernetes resources before deployment <br>**
`kubectl get svc,rc,pod,deployment -o wide`
<br>

### Step 7: Deploy kubernetes services
We need to deploy the three services in Kubernetes
<br>
`cd ../kubernetes`
<br>
`kubectl apply -f shopfront-service.yaml`
<br>
`kubectl apply -f productcatalogue-service.yaml`
<br>
`kubectl apply -f stockmanager-service.yaml`
<br>

### Step 8: Check kubernetes resources
Check kubernetes services, pods, deployment and replication controllers. If the pod is still getting created, check it again
<br>
`kubectl get svc -o wide`
<br>
`kubectl get pods -o wide`
<br>
`kubectl get deployment -o wide`
<br>
`kubectl get rc -o wide`
<br>

### Step 9: Test the application

Let's now test our application in the browser

**Check stockmanager service**
<br>
The first option is to use NodePort IP and the port of the service.<br>
<br>IP: `kubectl describe svc/stockmanager | grep -i ip`
<br>Port: `kubectl describe svc/stockmanager | grep -i targetport` 
<br><br>Now use the IP and the port from above to curl the service from within the kubernetes machine
<br><br>`curl <IP>:<Port>/stocks | python3 -m json.tool`
<br> The above Rest API is defined in stockmanager service to get all stocks. This API returns "productId, sku, amountAvailable" as it's defined in the stockmanager service.
<br><br> `curl <IP>:<Port>/stocks/1 | python3 -m json.tool`
<br> `curl <IP>:<Port>/stocks/3 | python3 -m json.tool`
<br> There are Rest APIs to get details of individual stocks.

<br><br> Another option is to use the IP address of the host machine
<br>Browser Testing:
<br> IP: The host IP address of your AWS machine
<br>Port: `kubectl describe svc/stockmanager | grep -i nodeport`
<br>`http://<AWS Machine IP>:<Port>/stocks`
<br>`http://<AWS Machine IP>:<Port>/stocks/3`


**Check productcatalogue service**
Similarly for productcatalogue, get the clusterIP and port by
<br>IP: `kubectl describe svc/productcatalogue | grep -i ip`
<br>Port: `kubectl describe svc/productcatalogue | grep -i targetport` 

<br><br>`curl <IP>:<Port>/products | python3 -m json.tool`
<br> The above Rest API is defined in productcatalogue service to get all products. This API returns "productId, name, description, price" as it's defined in the productcatalogue service.

<br><br> `curl <IP>:<Port>/products/1 | python3 -m json.tool`
<br> `curl <IP>:<Port>/products/3 | python3 -m json.tool`
<br> There are Rest APIs to get details of individual products.

Alternatively, we can test this in browser
<br>Browser Testing:
<br> IP: The host IP address of your AWS machine
<br>Port: `kubectl describe svc/productcatalogue | grep -i nodeport`
<br>`http://<AWS Machine IP>:<Port>/products`
<br>`http://<AWS Machine IP>:<Port>/products/3`

<br>

**Check shopfront service**
<br> 
Now check shopfront service that calls the other two services internally
<br>For shopfront, get the clusterIP and port by
<br>IP: `kubectl describe svc/shopfront | grep -i ip`
<br>Port: `kubectl describe svc/shopfront | grep -i targetport` 

<br><br>`curl <IP>:<Port>/products | python3 -m json.tool`
<br> The above Rest API is defined in shopfront service to get all products.
<br>**This shopfront API calls stockmanager and productcatalogue services internally and returns all attributes defined in these two services.**


Alternatively, we can test this in browser
<br>Browser Testing:
<br> IP: The host IP address of your AWS machine
<br>Port: `kubectl describe svc/shopfront | grep -i nodeport`
<br>`http://<AWS Machine IP>:<Port>/products`
<br>


Now let's clean up

```
kubectl delete --all svc,rc,deploy,pod,hpa --namespace=default
```