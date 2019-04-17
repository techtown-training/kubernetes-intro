
## Practise creating Pods, Deployments and Services

# Create a pod, deployment and service using the Imperative method

### cd to the source code directory - {exercise/src_code/kubernetes_additional_exercise/ex2.6/}

Create a deployment nginx

```
kubectl run nginx-deployment --image nginx --port 80
```

Check the currently running pods, deployments, services and replica sets

```
kubectl get po,deploy,svc,rs -o wide
```

Expose the Deployment as a Service of Loadbalancer 

```
kubectl expose deploy/nginx-deployment --type=LoadBalancer --name nginx-service 
```

Check the currently running pods, deployments, services and replica sets

```
kubectl get po,deploy,svc,rs -o wide
```

### Check for the nginx application from url , using the IP 
Do a describe on the nginx service , using the IP try doing a curl for that port

```
kubectl describe svc nginx-service
```

do a 

```
curl <ip_address>:<port_number>
```

# Create a deployment with the name blue  (Imperative management using config files)

```
kubectl create -f blue.yaml
```

Check the currently running pods, deployments, services and replica sets

```
 kubectl get po,deploy,svc,rs -o wide
```

Edit the Deployment and change the replicas from 1 to 2

```
# kubectl edit deploy blue
```

Check the currently running pods, deployments, services and replica sets ( we should now see 2 pods running)
<br>
*** Note - editing a running resource will change in the resource, but that will not change your Config file. It's a good practise to make changes in the Config file for maintainability. EDIT options are mostly used for quick debugging.


```
 kubectl get po,deploy,svc,rs -o wide
```

# Use the pod.yaml file and deploy.yaml files (Examples of Imperative commands)

Create a pod using the blue.yaml file

```
kubectl create -f sample_pod.yaml
```

Check for the running pods, deploy, svc, rs

```
kubectl get po,deploy,svc,rs -o wide
```

Let's try to create the blue Deployment again using the blue.yaml file

```
kubectl create -f blue.yaml
```

This will give an error, something like <br>
Error from server (AlreadyExists): error when creating "blue.yaml": deployments.apps "blue" already exists

Check for the running pods, deploy, svc, rs

```
kubectl get po,deploy,svc,rs -o wide
```

Now delete the blue, deployment and the pod that was created

```
kubectl delete pod <blue_pod_name>
 Deleting the pod will re-create it again.

kubectl delete deploy blue
This will delete all pods, rs and deploy associated with blue.
```

Check for the running pods, deploy, svc, rs

```
kubectl get po,deploy,svc,rs -o wide
```

# One example of Declarative command (use of kubectl apply)

Create a Deployment using the red.yaml file, but now using apply
```
kubectl apply -f red.yaml
```
Now go and edit the file and change the replicas from 1 to 3, using vi
```
vi red.yaml

# change the replicas to 3, save and come out
```
Check for the running pods, deploy, svc, rs
```
kubectl get po,deploy,svc,rs -o wide
```
Now apply these changes using the apply command
```
kubectl apply -f red.yaml
Replica 3 will be applied...
```

Let's describe the deploy red now
```
kubectl describe deploy red
At the bottom, see the changes in the "Events" section. It should show something like  below
Events:
  Type    Reason             Age   From                   Message
  ----    ------             ----  ----                   -------
  Normal  ScalingReplicaSet  1m    deployment-controller  Scaled up replica set red-755d594cd4 to 1
  Normal  ScalingReplicaSet  24s   deployment-controller  Scaled up replica set red-755d594cd4 to 3
```

Check for the running pods, deploy, svc, rs
```
kubectl get po,deploy,svc,rs -o wide
```

Now delete the red,blue deployment and see the pods, and deployment going down
```
kubectl delete deploy blue

kubectl delete deploy red
```
Check for the running pods, deploy, svc, rs
```
kubectl get pod -o wide -w
# -w is like watch option to see see the changes happening
```

If you have run all commands, you will have few more resources running, let's clean that up

```
kubectl delete pod/nginx-apparmor
kubectl delete deployment.apps/nginx-deployment
kubectl delete service/nginx-service
```

Ref:- https://kubernetes.io/docs/concepts/overview/object-management-kubectl/overview/,
<br>
Ref:- https://www.linkedin.com/learning/learning-kubernetes/next-steps


