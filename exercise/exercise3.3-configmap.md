# Creating ConfigMap

### cd to the source code directory - {exercise/src_code/kubernetes_additional_exercise/ex3.3/}

# Create a Configmap and check log level

We shall be using the yaml files in this folder. 

## Create a deployment which does not refer to config map ( has log level hard coded as error)
```
$ kubectl create -f reader-deployment.yaml
```

Check if you have any Config Map
```
$ kubectl get configmaps -o wide
```

Check for the running pods and describe the pod, and check logs for the running pod
```
$ kubectl get po,deploy -o wide
$ kubectl describe <pod_name>
$ kubectl logs <pod_name>
```

## Create a deployment which uses to config map

Before the deployment define a configmap as a key-value pair

```
$ kubectl create configmap logger --from-literal=log_level=debug
```

Now create the Deployment
```
$ kubectl create -f reader-configmap-deployment.yaml
```

Check if the Config Map was created
```
$ kubectl get configmaps -o wide
```
Check for the running pods and describe the pod, and check logs for the running pod

```
$ kubectl get po,deploy -o wide

$ kubectl describe <pod_name>

$ kubectl logs <pod_name>
```


Ref:- https://www.linkedin.com/learning/learning-kubernetes/next-steps

