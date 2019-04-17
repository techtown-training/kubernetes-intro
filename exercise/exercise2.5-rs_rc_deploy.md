## Replication Controller

Create a Replication Controller from a file called rc.yaml (inside helper files) and add the following text:

rc.yaml file is located in - https://github.com/techtown-training/microservices-bootcamp/blob/master/exercise/src_code/kubernetes_additional_exercise/ex2.5/rc.yaml

Most of this structure should look familiar from our discussion of Deployments; we’ve got the name of the actual 
Replication Controller (soaktestrc) and we’re designating that we should have 3 replicas, each of which are 
defined by the template.  The selector defines how we know which pods belong to this Replication Controller.

Now tell Kubernetes to create the Replication Controller based on that file:

```
# kubectl create -f rc.yaml
replicationcontroller "soaktestrc" created
```

Let’s take a look at what we have using the describe command:

```
# kubectl describe rc soaktestrc
Name:           soaktestrc
Namespace:      default
Image(s):       nickchase/soaktest
Selector:       app=soaktestrc
Labels:         app=soaktestrc
Replicas:       3 current / 3 desired
Pods Status:    3 Running / 0 Waiting / 0 Succeeded / 0 Failed
No volumes.
Events:
  FirstSeen     LastSeen        Count   From                            SubobjectPath   Type   Reason                   Message
  ---------     --------        -----   ----                            -------------   --------------                  -------
  1m            1m              1       {replication-controller }                       Normal SuccessfulCreate Created pod: soaktestrc-g5snq
  1m            1m              1       {replication-controller }                       Normal SuccessfulCreate Created pod: soaktestrc-cws05
  1m            1m              1       {replication-controller }                       Normal SuccessfulCreate Created pod: soaktestrc-ro2bl
```

As we can see, we’ve got the Replication Controller, and there are 3 replicas (pods), of the 3 pods that we wanted. 
All 3 of them are currently running.  You can also see the individual pods listed underneath, along with their names.  We added the --show-labels options and -l option for selecting by labels

```
# kubectl get rc,po --show-labels -l app=soaktestrc
NAME               READY     STATUS    RESTARTS   AGE
soaktestrc-cws05   1/1       Running   0          3m
soaktestrc-g5snq   1/1       Running   0          3m
soaktestrc-ro2bl   1/1       Running   0          3m
```

Delete one of the pods (out of 3)
If you delete one of the pods manually, then the replication controller will create one to match the desired numbers of pods to be 3.

```
# kubectl delete pod/<pod_name>
pod "<pod_name>" deleted

# kubectl get pods -o wide
You will see three pods again
```


Clean up the Replication Controllers

```
# kubectl delete rc soaktestrc
replicationcontroller "soaktestrc" deleted
```

check if the pods got deleted

```
# kubectl get rc,po --show-labels -l app=soaktestrc
As you can see, when you delete the Replication Controller, you also delete all of the pods that it created.
```

## Replica Sets
Next we’ll look at Replica Sets, but first let’s clean up:

Replica Sets are a sort of hybrid, in that they are in some ways more powerful than Replication Controllers,
and in others they are less powerful. Replica Sets are declared in essentially the same way as Replication Controllers, 
except that they have more options for the selector. Use the file rs.yaml for this part

rs.yaml file is located in - https://github.com/techtown-training/microservices-bootcamp/blob/master/exercise/src_code/kubernetes_additional_exercise/ex2.5/rs.yaml

In this case, it’s more or less the same as when we were creating the Replication Controller, 
except we’re using matchLabels instead of label.  But we could just as easily have said (has matchExpressions):

```
spec:
   replicas: 3
   selector:
     matchExpressions:
      - {key: app, operator: In, values: [soaktestrs, soaktestrs, soaktest]}
      - {key: teir, operator: NotIn, values: [production]}
  template:
     metadata:
```

In the above case, we’re looking at two different conditions: The app label must be soaktestrc, soaktestrs, or soaktest
<br> The tier label (if it exists) must not be production
<br>Let’s go ahead and create the Replica Set and get a look at it:

```
# kubectl create -f rs.yaml
replicaset "soaktestrs" created
```

We can describe the Replica Sets

```
# kubectl describe rs soaktestrs
Name:           soaktestrs
Namespace:      default
Image(s):       nickchase/soaktest
Selector:       app in (soaktest,soaktestrs),teir notin (production)
Labels:         app=soaktestrs
Replicas:       3 current / 3 desired
Pods Status:    3 Running / 0 Waiting / 0 Succeeded / 0 Failed
No volumes.
Events:
  FirstSeen     LastSeen        Count   From                            SubobjectPath   Type    Reason                   Message
  ---------     --------        -----   ----                            -------------   --------------                   -------
  1m            1m              1       {replicaset-controller }                        Normal  SuccessfulCreate Created pod: soaktestrs-it2hf
  1m            1m              1       {replicaset-controller }                       Normal  SuccessfulCreate Created pod: soaktestrs-kimmm
  1m            1m              1       {replicaset-controller }                        Normal  SuccessfulCreate Created pod: soaktestrs-8i4ra
```

We can get the pods

```
# kubectl get pods
NAME               READY     STATUS    RESTARTS   AGE
soaktestrs-8i4ra   1/1       Running   0          1m
soaktestrs-it2hf   1/1       Running   0          1m
soaktestrs-kimmm   1/1       Running   0          1m
```

As we can see, the output is pretty much the same as for a Replication Controller (except for the selector), 
and for most intents and purposes, they are similar.  

Exploration :- To see the errors when the labels and selectors does not match , feel free to try out the other 2 replica sets
in the same location :-

```
kubectl create -f rs_selector2.yaml
kubectl get po,rs --show-labels
```

and this gives an error 

```
kubectl create -f rs_selector2_bad.yaml 
Error :- The ReplicaSet "soaktestrsbad" is invalid: spec.template.metadata.labels: Invalid value: map[string]string{"app":"soaktestrspq", "tier":"prod"}: `selector` does not match template `labels`
```


The major difference is that the rolling-update command (we will see an example for this) works with Replication Controllers, 
but won’t work with a Replica Set.  This is because Replica Sets are meant to be used as the backend for Deployments.

Let’s clean up before we move on.

```
# kubectl delete rs soaktestrs
replicaset "soaktestrs" deleted
# kubectl delete rs soaktestrs1
replicaset "soaktestrs1" deleted
```

Again, the pods that were created are deleted when we delete the Replica Set.

```
# kubectl get pods,rc,rs
--- there shouldn't be any pod, rc, rs resources available. If you have any then delete it.
```

## Deployments

Deployments are intended to replace Replication Controllers.  They provide the same replication functions
(through Replica Sets) and also the ability to rollout changes and roll them back if necessary.

You first have to create a simple Deployment (you can find it in the git repo)  
<br>
deploy_backed_by_rs.yaml is located in - https://github.com/techtown-training/microservices-bootcamp/blob/master/exercise/src_code/kubernetes_additional_exercise/ex2.5/deploy_backed_by_rs.yaml

Now go ahead and create the Deployment:

```
# kubectl create -f deploy_backed_by_rs.yaml
deployment "soaktest" created
```

Now let’s go ahead and describe the Deployment:

```
# kubectl describe deployment soaktest
Name:                   soaktest
Namespace:              default
CreationTimestamp:      Sun, 05 Mar 2017 16:21:19 +0000
Labels:                 app=soaktest
Selector:               app=soaktest
Replicas:               5 updated | 5 total | 5 available | 0 unavailable
StrategyType:           RollingUpdate
MinReadySeconds:        0
RollingUpdateStrategy:  1 max unavailable, 1 max surge
OldReplicaSets:         <none>
NewReplicaSet:          soaktest-3914185155 (5/5 replicas created)
Events:
  FirstSeen     LastSeen        Count   From                            SubobjectPath   Type    Reason                   Message
  ---------     --------        -----   ----                            -------------   --------------                   -------
  38s           38s             1       {deployment-controller }                        Normal  ScalingReplicaSet        Scaled up replica set soaktest-3914185155 to 3
  36s           36s             1       {deployment-controller }                        Normal  ScalingReplicaSet        Scaled up replica set soaktest-3914185155 to 5
```
  
As you can see, rather than listing the individual pods, Kubernetes shows us the Replica Set.  
Notice that the name of the Replica Set is the Deployment name and a hash value.

We can check the pods for the deployment (and also behind the scenes the dpeloyment creates a replica sets)

```
Check all deployments, pods, RCs, RSs
# kubectl get deploy,po,rs,rc
NAME                        READY     STATUS    RESTARTS   AGE
soaktest-3914185155-7gyja   1/1       Running   0          2m
soaktest-3914185155-lrm20   1/1       Running   0          2m
soaktest-3914185155-o28px   1/1       Running   0          2m
soaktest-3914185155-ojzn8   1/1       Running   0          2m
soaktest-3914185155-r2pt7   1/1       Running   0          2m

You can see that there is 1 deployment, 5 pods (becaue of 5 replication), 1 replica set BUT no replication controller.
This is because deployment created replica set but not replication controller
```

Ref:- https://www.mirantis.com/blog/kubernetes-replication-controller-replica-set-and-deployments-understanding-replication-options/

