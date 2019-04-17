# Scaling, Auto-Scaling and Rolling out Deployments

delete all prior pods, deployments and services

```
$ kubectl get o,deploy,svc -o wide
$ kubectl delete deploy <deploy_name>
$ kubectl delete pod <pod_name>
```
## Create a deployment
```
$ kubectl create -f https://raw.githubusercontent.com/abhikbanerjee/Kubernetes_Exercise_Files/master/helper_yaml_files/Ex_combine_deploy_service/helloworld-all.yml 
```

Check the deployments and pods
```
$ kubectl get po,deploy,svc -o wide
```

## Scale on an existing deployment 

Scale the deployment created in last step and see the effect on the number of pods, rs, and deployment 
```
$ kubectl scale deploy helloworld-all-deployment --replicas=4
Check the scaling in "describe", at the bottom in the "Events" section, you will see that it's scaled from 1 to 4
$ kubectl describe deploy helloworld-all-deployment
```

## Use Auto-Scaling on an existing deployment 

### Create a nginx deployment
```
$ kubectl run nginx-deployment --image nginx --port 80
```
Scale the deployment to 5 pods
```
$ kubectl scale deploy/nginx-deployment --replicas=5
```
Add autoscale to an existing deployment nginx

```
$ kubectl autoscale deployment nginx-deployment --min=2 --max=10
```
check the HPA
```
$ kubectl get hpa
```

## Functionality of Roll outs and Roll Backs

We will use the yaml files located in this section 
https://github.com/shekhar2010us/kubernetes_teach_git/blob/master/ex4/navbar-black.yaml

Create the deployment and service for the navbar
```
$ kubectl create -f navbar-black.yaml
```

```
$ kubectl get po,deploy,svc --show-labels -o wide 
```
get the port for the LoadBalancer and check the URL for the node with the port

http://<public_ip_address>:<LoadBalancer_exposed_port>

Rollout a new deployment
```
$ kubectl set image deploy/header-deployment navbarworld=karthequian/helloworld:blue
```
Check the current status on pods , Deployment and replicasets (we will see a new replica set starting up, and now we will have 2 instances of the replica set)
```
$ kubectl get po,deploy,rs --show-labels -o wide
```
We can also do a describe on the replica set, and we shall see gradually all the pods moving to the other replica set
```
$ kubectl describe deploy header-deployment
```

Try to hit the front end again, and we should see the header change to blue from black
http://<public_ip_address>:<LoadBalancer_exposed_port>

Try the rollback the deployment now, and keep monitoring the replicasets , pods and deployment
```
$ kubectl rollout undo deployment/header-deployment
```
Check the current status on pods , Deployment and replicasets (we will see the pods going back to the old replica set)
```
$ kubectl get po,deploy,rs --show-labels -o wide
```

hit the url front end again to see the color of the header turn to black again
http://<public_ip_address>:<LoadBalancer_exposed_port>

p.s - We can also see the history of the rollout , this is enabled by the --record, but sometimes may not work . Alternative is to use describe
```
$ kubectl rollout history deployment/header-deployment
```

Let's clean up
```
kubectl delete --all svc,deploy,pod,hpa --namespace=default
```
Ref:- https://www.linkedin.com/learning/learning-kubernetes/next-steps


