## Labels Usage

### cd to the source code directory - {exercise/src_code/kubernetes_additional_exercise/ex2.7/}


Create a bunch of pods with various labels, so we can operate on these pods 
(https://github.com/techtown-training/microservices-bootcamp/blob/master/exercise/src_code/kubernetes_additional_exercise/ex2.7/sample-infra-with-labels.yaml)

Let’s take a look at what we have created:

```
# kubectl get po
```

You can get more details by showing and displaying the labels:

```
# kubectl get po --show-labels
```

## Create a list of Pods with labels

Use Sample Infrastructure to create a bunch of Pods (even though in real life we don't create pods)

```
kubectl create -f sample-infra-with-labels.yaml
(creates a bunch of pods with labels)
Wait for some time until all pods have been created
```

## Use various Selectors for pods

Now we use various selectors to search in labels to pull out pods, and delete them

```
kubectl get pods --selector env=production --show-labels
or
kubectl get pods -l env=production --show-labels
```

We can use and search multiple labels , this represents and

```
kubectl get pods -l env=production,dev-lead=amy --show-labels
```

We can also have the not equal to operator

```
kubectl get pods -l env=production,dev-lead!=amy --show-labels 
```

Get the pods, which are in a list of versions - we use the in operator

```
kubectl get pods -l 'release-version in (1.0,2.0)' --show-labels
```

Get the pods, which are in a list of versions - we use the notin operator

```
kubectl get pods -l 'release-version notin (1.0,2.0)' --show-labels
```

## add a label on an existing pod and delete the label 

Changing labels of an existing pod

```
Add a new label
# kubectl label po/cart-dev app=helloworldapp
# kubectl get pods/cart-dev --show-labels

Overwrite an existing label
# kubectl label po/cart-dev app=helloworldapp2 --overwrite

Note - if you do not specify "overwrite" option, it will complain that the KEY already has a VALUE
```

delete a label to an existing pod

```
remove the label
# kubectl label po/cart-dev app-
```

# Delete pods, which match some of the given pods [delete, get with a specific label, works for deployment, replication set too]

```
kubectl delete pods -l dev-lead=karthik 
```

# delete all pods which belong to the environment production

```
# kubectl delete pods -l env=production 
```

delete all pods in a namespace (we will see namespace in detail later. But right now, we have to delete all pods for cleanup, so we are using delete by namespace)

```
# kubectl delete --all pods --namespace=default
```


Ref:- https://www.linkedin.com/learning/learning-kubernetes/next-steps


