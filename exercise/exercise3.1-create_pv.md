# Create-Persistent-Volume

### cd to the source code directory - {exercise/src_code/kubernetes_additional_exercise/ex3.1/}


We shall use the 'yaml' files from the current directory

## Create your PersistentVolumes and PersistentVolumeClaims

To deploy the PVC, run:

```
kubectl apply -f mysql-pvc.yaml
kubectl apply -f wordpress-pvc.yaml
```

Check to see if your claims are bound:

```
kubectl get pvc
```

## Setup MySQL

Create a Kubernetes Secret to store the password for the database:

```
kubectl create secret generic mysql --from-literal=password=12345
```

The mysql.yaml manifest describes a Deployment with a single instance MySQL Pod which will have the MYSQL_ROOT_PASSWORD environment variable whose value is set from the secret created. The mysql container will use the PersistentVolumeClaim and mount the persistent disk at /var/lib/mysql inside the container.


Deploy manifest file `mysql.yaml`

```
kubectl create -f mysql.yaml
```

Check to see if the pod is running

```
kubectl get pod -l app=mysql
```

Deploy the manifest `mysql-service.yaml`

```
kubectl create -f mysql-service.yaml
```

Check to see if service was created

```
kubectl get service mysql
```

## Deploy Wordpress

Use the `wordpress.yaml` manifest
This manifest describes a Deployment with a single instance WordPress Pod. This container reads the WORDPRESS_DB_PASSWORD environment variable from the database password Secret you created earlier.

This manifest also configures the WordPress container to communicate MySQL with the host address mysql:3306. This value is set on the WORDPRESS_DB_HOST environment variable. We can refer to the database as mysql, because of Kubernetes DNS allows Pods to communicate a Service by its name.

Deploy the Manifest

```
kubectl create -f wordpress.yaml
```

Check to see if the pod is running

```
kubectl get pod -l app=wordpress
```

### Is there an Error or pending status? Why??
hint:- Do we need to create an object to link it to the PVC (persistent Volume Claim).

### Solution

Delete the PV, PVC using the

```
kubectl delete pv --all
kubectl delete pvc --all
kubectl delete deploy --all
```

Create the 2 directories

```
mkdir -p /root/my_test_vol
mkdir -p /root/my_test_vol_wp
```

Then recreate the PV, and the PVC again

```
kubectl create -f mysql-pv.yaml -f wordpress-pv.yaml
kubectl create -f mysql-pvc.yaml -f wordpress-pvc.yaml
```

On Doing the get on PV, and PVC we should see the PVC binded to the PV's

```
kubectl get pv,pvc -o wide
```

### Expose the wordpress service

In the previous step, you have deployed a WordPress container which is not currently accessible from outside your cluster as it does not have an external IP address. To expose your WordPress application to traffic from the internet using a load balancer (subject to billing), you need a Service with type:LoadBalancer.

Use the `wordpress-service.yaml`

```
kubectl create -f wordpress-service.yaml
```

Check to see if the pod is running

```
kubectl get svc -l app=wordpress
```

In the output above, the EXTERNAL-IP column will show the public IP address created for your blog. Save this IP address for the next step.

## Visit your wordpress blog

Use the AWS Machine IP with the NodePort to check the hosted wordpress service

## Cleaning up

Delete the wordpress service

```
kubectl delete service wordpress
kubectl delete service mysql
kubectl delete deployment.apps/mysql
kubectl delete deployment.apps/wordpress
```

Delete the PersistantVolumes

```
kubectl delete pvc wordpress-volumeclaim
kubectl delete pvc mysql-volumeclaim
```

check pv

```
# kubectl get pv
You will see status:released

# kubectl delete pv mysql-volume
# kubectl delete pv wordpress-volume
Still the directories you created in the master node won't get deleted, you may delete them manually
```
