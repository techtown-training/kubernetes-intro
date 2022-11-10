# Kubernetes 101

Kubernetes 101 workshop - introduction to Kubernetes and basic concepts


## Part 1 Running nginx

Everyone says that kubernetes is hard, however this proves otherwise!
Let's create nginx service.

```
kubectl create deploy my-nginx --image=nginx:latest --replicas=2 --port=80 --save-config
kubectl expose deployment my-nginx --type=LoadBalancer --port=80
```

Let's go step by step and explore what just happened:

* [Pods](http://kubernetes.io/docs/user-guide/pods/) are a building block
of the infrastructure. In essence this is a group of containers sharing the same networking and host
linux namespaces. They are used to group related processes together. Our `run` command resulted in several running pods:

```
kubectl get pods

NAME                        READY     STATUS    RESTARTS   AGE
my-nginx-3800858182-auusv   1/1       Running   0          32m
my-nginx-3800858182-jzoxe   1/1       Running   0          32m
```

You can explore individual pods or group of pods using handy `kubectl describe`

```
kubectl describe pods

Name:		my-nginx-3800858182-auusv
Namespace:	default
Node:		172.28.128.5/172.28.128.5
Start Time:	Sun, 15 May 2016 19:37:01 +0000
Labels:		pod-template-hash=3800858182,run=my-nginx
Status:		Running
IP:		10.244.33.109
Controllers:	ReplicaSet/my-nginx-3800858182
Containers:
  my-nginx:
    Container ID:	docker://f322f42081024e8374d23765652d3abc4cb1f28d3cfd4ed37a7dd0c990c12c5f
    Image:		nginx
    Image ID:		docker://44d8b6f34ba13fdbf1da947d4bc6467eadae1cc84c2090011803f7b0862ea124
    Port:		80/TCP
    QoS Tier:
      cpu:		BestEffort
      memory:		BestEffort
    State:		Running
      Started:		Sun, 15 May 2016 19:37:36 +0000
    Ready:		True
    Restart Count:	0
    Environment Variables:
Conditions:
  Type		Status
  Ready 	True
Volumes:
  default-token-8n3l2:
    Type:	Secret (a volume populated by a Secret)
    SecretName:	default-token-8n3l2
Events:
  FirstSeen	LastSeen	Count	From			SubobjectPath			Type		Reason		Message
  ---------	--------	-----	----			-------------			--------	------		-------
  33m		33m		1	{default-scheduler }					Normal		Scheduled	Successfully assigned my-nginx-3800858182-auusv to 172.28.128.5
  33m		33m		1	{kubelet 172.28.128.5}	spec.containers{my-nginx}	Normal		Pulling		pulling image "nginx"
  32m		32m		1	{kubelet 172.28.128.5}	spec.containers{my-nginx}	Normal		Pulled		Successfully pulled image "nginx"
  32m		32m		1	{kubelet 172.28.128.5}	spec.containers{my-nginx}	Normal		Created		Created container with docker id f322f4208102
  32m		32m		1	{kubelet 172.28.128.5}	spec.containers{my-nginx}	Normal		Started		Started container with docker id f322f4208102
```

Let's see what's inside the pod.

**Pod IPs**

You can spot IP in the overlay network assigned to pod. In my case it's `10.244.33.109`. Can we access it directly?

Let's try and see!

```
kubectl run -it --rm jumpbox --image=project42/jumpbox -- bash
curl http://10.244.33.109
<!DOCTYPE html>
<html>
<head>
<title>Welcome to nginx!</title>
<style>
...
```

Whoa! That worked! Our `sandbox` machine is joined to the cluster's overlay network, so you can access it directly, however in practice
that's rarely necessary.

### Pod Containers

In our Nginx pod there's only one running container `my-nginx`, however as we've mentioned before we can have multiple
containers running in single Pod.

Our container exposes Port 80. Thanks to overlay network every container can expose the same port on the same machine, and they won't collide.

We can enter pod container using handy `kubectl exec` command:

```
kubectl exec -ti my-nginx-3800858182-auusv -c my-nginx -- /bin/bash
```

Our `kubectl exec` command specified pod id and container name within the pod. `-ti` stands for attach PTY and connect input to the container respectively.

If there's just one container, we can omit the container name within the pod:

```
kubectl exec -ti my-nginx-3800858182-auusv /bin/bash
```

Let's explore our nginx container a bit:

```
$ ls -l /proc/1/exe
lrwxrwxrwx. 1 root root 0 Mar 14 22:57 /proc/1/exe -> /usr/sbin/nginx
```

as you see, our container has it's own separate PID namespace - nginx process is actually `PID 1`.

```
ls -l /var/run/secrets/kubernetes.io/serviceaccount/
```

K8s also mounted special volume in our container `serviceaccount` with access credentials to talk to K8s API process.
K8s uses this techinque a lot to mount configuration and secrets into a running container. We will explore this in more detail
a bit later.

We don't need to always run interactive sessions within container, e.g. we can execute commmand without attaching PTY:

```
kubectl exec my-nginx-3800858182-auusv -- /bin/ls -l
total 0
drwxr-xr-x.   1 root root 1190 May  3 18:53 bin
drwxr-xr-x.   1 root root    0 Mar 13 23:46 boot
drwxr-xr-x.   5 root root  380 May 15 19:37 dev
drwxr-xr-x.   1 root root 1646 May 15 19:47 etc
drwxr-xr-x.   1 root root    0 Mar 13 23:46 home
drwxr-xr-x.   1 root root  100 May  4 02:38 lib
drwxr-xr-x.   1 root root   40 May  3 18:52 lib64
drwxr-xr-x.   1 root root    0 May  3 18:52 media
drwxr-xr-x.   1 root root    0 May  3 18:52 mnt
drwxr-xr-x.   1 root root    0 May  3 18:52 opt
dr-xr-xr-x. 151 root root    0 May 15 19:37 proc
drwx------.   1 root root   56 May 15 19:46 root
drwxr-xr-x.   1 root root   48 May 15 19:37 run
drwxr-xr-x.   1 root root 1344 May  3 18:53 sbin
drwxr-xr-x.   1 root root    0 May  3 18:52 srv
dr-xr-xr-x.  13 root root    0 May 15 17:56 sys
drwxrwxrwt.   1 root root    0 May 15 19:47 tmp
drwxr-xr-x.   1 root root   70 May  4 02:38 usr
drwxr-xr-x.   1 root root   90 May  4 02:38 var
```

**Note:** when calling exec, don't forget `--`. You don't need to escape or join command arguments passed to exec really, `kubectl` will simply
send everything after `--` as is.

### Part 2 Deployments and Replicasets

So k8s created 2 Pods for us and that's it? Not really, it's a bit more advanced system and it really thought through the deployment lifecycle.
K8s created a deployment with replicaset of 2 pods:

```
kubectl get deployments
NAME       DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
my-nginx   2         2         2            2           1h
```

```
kubectl get replicasets
NAME                  DESIRED   CURRENT   AGE
my-nginx-3800858182   2         2         1h
```

Whoa! Lot's of stuff! Let's go through it one by one:

[Deployment](http://kubernetes.io/docs/user-guide/deployments) is a special declarative state of your [Pods](http://kubernetes.io/docs/user-guide/pods) and [ReplicaSets](http://kubernetes.io/docs/user-guide/replicasets).
You simply declare the desire state of your deployment and K8s converges the current state to it.

Every time you update the deployment, it kicks off the update procedure using whatever update strategy you've selected for it.

Let's dig a little deeper into this deployment:

Here we see that it manages 2 replicas of our Pod and using RollingUpdate strategy:

```
kubectl describe deployments/my-nginx
Name:			my-nginx
Namespace:		default
CreationTimestamp:	Sun, 15 May 2016 12:37:01 -0700
Labels:			run=my-nginx
Selector:		run=my-nginx
Replicas:		2 updated | 2 total | 2 available | 0 unavailable
StrategyType:		RollingUpdate
MinReadySeconds:	0
RollingUpdateStrategy:	1 max unavailable, 1 max surge
OldReplicaSets:		<none>
NewReplicaSet:		my-nginx-3800858182 (2/2 replicas created)
Events:
  FirstSeen	LastSeen	Count	From				SubobjectPath	Type		Reason			Message
  ---------	--------	-----	----				-------------	--------	------			-------
  1h		1h		1	{deployment-controller }			Normal		ScalingReplicaSet	Scaled up replica set my-nginx-3800858182 to 2

```


Events tell us what happened to this deployment in the past. We'll dig a little bit deeper into this deployment later and now let's move on to services!

### Services

Pods, Replicasets and Deployments and all done with one command! But that's not all. We need a scalable way to access our services, so k8s team came up with
[Services](http://kubernetes.io/docs/user-guide/services)

Services provide special Virtual IPs load balancing traffic to the set of pods in a replica sets.

```
kubectl get services
kubernetes   10.100.0.1     <none>        443/TCP   2h
my-nginx     10.100.68.75   <none>        80/TCP    1h
```

As you see there are two services - one is a system service `kubernetes` that points to k8s API. Another one is `my-nginx` service, pointing to our Pods in a replica sets.

Let's dig a little deeper into services:

```
kubectl describe services/my-nginx
Name:			my-nginx
Namespace:		default
Labels:			<none>
Selector:		run=my-nginx
Type:			ClusterIP
IP:			10.100.68.75
Port:			<unset>	80/TCP
Endpoints:		10.244.33.109:80,10.244.40.109:80
Session Affinity:	None
No events.
```

`ClusterIP` type of the service means that it's an internal IP managed by k8s and not reachable outside. You can create outher types of services that play nicely with AWS/GCE and Azure `LoadBalancer`, we'll
dig into that some other time though. Meanwhile, let's notice that there are 2 endpoints:

```
Endpoints:		10.244.33.109:80,10.244.40.109:80
```

Every one of them points to appropriate Pod in the ReplicaSet. As long as pods come and go, this section will be updated, so applications don't
worry about individual Pod locations.

And finally, there's service IP

```
IP:			10.100.68.75
```

This is our VIP that never changes and provides a static piece of configuration making it easier for our components in the system to talk to each other.


```
kubectl run -it --rm jumpbox --image=project42/jumpbox -- bash
curl http://10.100.68.75
<!DOCTYPE html>
<html>
<head>
<title>Welcome to nginx!</title>
<style>
    body {
        width: 35em;
        margin: 0 auto;
        font-family: Tahoma, Verdana, Arial, sans-serif;
    }
</style>
</head>
<body>
<h1>Welcome to nginx!</h1>
<p>If you see this page, the nginx web server is successfully installed and
working. Further configuration is required.</p>
```

It works! Wait, so you need to hardcode this VIP in your configuration? What if it changes from environment to environment?
Thankfully, k8s team thought about this as well, and we can simply do:

```
kubectl run -it --rm jumpbox --image=project42/jumpbox -- bash
curl http://my-nginx
<!DOCTYPE html>
...
```

K8s is integrated with [SkyDNS](https://github.com/kubernetes/kubernetes/tree/master/cluster/addons/dns) service
that watches the services and pods and sets up appropriate `A` records. Our `sandbox` local DNS server is simply configured to
point to the DNS service provided by k8s.

That's very similar how K8s manages discovery in containers as well. Let's login into one of the nginx boxes and
discover `/etc/resolv.conf` there:


```
kubectl exec -ti my-nginx-3800858182-auusv -- /bin/bash
root@my-nginx-3800858182-auusv:/# cat /etc/resolv.conf

nameserver 10.100.0.4
search default.svc.cluster.local svc.cluster.local cluster.local hsd1.ca.comcast.net
options ndots:5
```

As you see, `resolv.conf` is set up to point to the DNS resolution service managed by K8s.

### Back to Deployments

The power of Deployments comes from ability to do smart upgrades and rollbacks in case if something goes wrong.

Let's update our deployment of nginx to the newer version.

```
cat my-nginx-new.yaml
```

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    run: my-nginx
  name: my-nginx
  namespace: default
spec:
  replicas: 2
  selector:
    matchLabels:
      run: my-nginx
  template:
    metadata:
      labels:
        run: my-nginx
    spec:
      containers:
      - image: nginx:1.11.5
        name: my-nginx
        ports:
        - containerPort: 80
          protocol: TCP
```

Let's apply our deployment:

```
kubectl apply -f my-nginx-new.yaml
```


We can see that a new replicaset has been created

```
kubectl get rs

NAME                  DESIRED   CURRENT   AGE
my-nginx-1413250935   2         2         50s
my-nginx-3800858182   0         0         2h
```

If we look at the events section of the deployment we will see how it performed rolling update
scaling up new replicaset and scaling down old replicaset:


```
kubectl describe deployments/my-nginx
Name:			my-nginx
Namespace:		default
CreationTimestamp:	Sun, 15 May 2016 19:37:01 +0000
Labels:			run=my-nginx
Selector:		run=my-nginx
Replicas:		2 updated | 2 total | 2 available | 0 unavailable
StrategyType:		RollingUpdate
MinReadySeconds:	0
RollingUpdateStrategy:	1 max unavailable, 1 max surge
OldReplicaSets:		<none>
NewReplicaSet:		my-nginx-1413250935 (2/2 replicas created)
Events:
  FirstSeen	LastSeen	Count	From				SubobjectPath	Type		Reason			Message
  ---------	--------	-----	----				-------------	--------	------			-------
  2h		2h		1	{deployment-controller }			Normal		ScalingReplicaSet	Scaled up replica set my-nginx-3800858182 to 2
  1m		1m		1	{deployment-controller }			Normal		ScalingReplicaSet	Scaled up replica set my-nginx-1413250935 to 1
  1m		1m		1	{deployment-controller }			Normal		ScalingReplicaSet	Scaled down replica set my-nginx-3800858182 to 1
  1m		1m		1	{deployment-controller }			Normal		ScalingReplicaSet	Scaled up replica set my-nginx-1413250935 to 2
  1m		1m		1	{deployment-controller }			Normal		ScalingReplicaSet	Scaled down replica set my-nginx-3800858182 to 0
```

And now it's `1.11.5`, let's check out in the headers:

```
kubectl run -it --rm jumpbox --image=project42/jumpbox -- bash
curl -v http://my-nginx

* About to connect() to my-nginx port 80 (#0)
*   Trying 10.100.68.75...
* Connected to my-nginx (10.100.68.75) port 80 (#0)
> GET / HTTP/1.1
> User-Agent: curl/7.29.0
> Host: my-nginx
> Accept: */*
>
< HTTP/1.1 200 OK
< Server: nginx/1.9.1
```

Let's simulate a situation when a deployment fails and we need to rollback. Our deployment has a typo

```
cat my-nginx-typo.yaml
```

```
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    run: my-nginx
  name: my-nginx
  namespace: default
spec:
  replicas: 2
  selector:
    matchLabels:
      run: my-nginx
  template:
    metadata:
      labels:
        run: my-nginx
    spec:
      containers:
      - image: nginx:1.91
        name: my-nginx
        ports:
        - containerPort: 80
          protocol: TCP
```

Let's apply a bad configuration:

```shell
kubectl apply -f my-nginx-typo.yaml
deployment "my-nginx" configured
```

Our new pods have crashed:

```
kubectl get pods
NAME                        READY     STATUS             RESTARTS   AGE
my-nginx-1413250935-rqstg   1/1       Running            0          10m
my-nginx-2896527177-8wmk7   0/1       ImagePullBackOff   0          55s
my-nginx-2896527177-cv3fd   0/1       ImagePullBackOff   0          55s
```

Our deployment shows 2 unavailable replicas:

```
kubectl describe deployments/my-nginx
Name:			my-nginx
Namespace:		default
CreationTimestamp:	Sun, 15 May 2016 19:37:01 +0000
Labels:			run=my-nginx
Selector:		run=my-nginx
Replicas:		2 updated | 2 total | 1 available | 2 unavailable
StrategyType:		RollingUpdate
MinReadySeconds:	0
RollingUpdateStrategy:	1 max unavailable, 1 max surge
OldReplicaSets:		my-nginx-1413250935 (1/1 replicas created)
NewReplicaSet:		my-nginx-2896527177 (2/2 replicas created)
Events:
  FirstSeen	LastSeen	Count	From				SubobjectPath	Type		Reason			Message
  ---------	--------	-----	----				-------------	--------	------			-------
  2h		2h		1	{deployment-controller }			Normal		ScalingReplicaSet	Scaled up replica set my-nginx-3800858182 to 2
  11m		11m		1	{deployment-controller }			Normal		ScalingReplicaSet	Scaled up replica set my-nginx-1413250935 to 1
  11m		11m		1	{deployment-controller }			Normal		ScalingReplicaSet	Scaled down replica set my-nginx-3800858182 to 1
  11m		11m		1	{deployment-controller }			Normal		ScalingReplicaSet	Scaled up replica set my-nginx-1413250935 to 2
  10m		10m		1	{deployment-controller }			Normal		ScalingReplicaSet	Scaled down replica set my-nginx-3800858182 to 0
  1m		1m		1	{deployment-controller }			Normal		ScalingReplicaSet	Scaled up replica set my-nginx-2896527177 to 1
  1m		1m		1	{deployment-controller }			Normal		ScalingReplicaSet	Scaled down replica set my-nginx-1413250935 to 1
  1m		1m		1	{deployment-controller }			Normal		ScalingReplicaSet	Scaled up replica set my-nginx-2896527177 to 2
```

Our rollout has stopped. Let's view the history:

```
kubectl rollout history deployments/my-nginx
deployments "my-nginx":
REVISION	CHANGE-CAUSE
1		kubectl run my-nginx --image=nginx --replicas=2 --port=80 --expose --record
2		kubectl apply -f my-nginx-new.yaml
3		kubectl apply -f my-nginx-typo.yaml
```

**Note:** We used `--record` flag and now all commands are recorded!

Let's roll back the last deployment:

```
kubectl rollout undo deployment/my-nginx
```

We've created a new revision by doing `undo`:

```
kubectl rollout history deployment/my-nginx
deployments "my-nginx":
REVISION	CHANGE-CAUSE
1		kubectl run my-nginx --image=nginx --replicas=2 --port=80 --expose --record
3		kubectl apply -f my-nginx-typo.yaml
4		kubectl apply -f my-nginx-new.yaml
```

[Deployments](http://kubernetes.io/docs/user-guide/deployments/) are a very powerful tool, and we've barely scratched the surface of what they can do. Check out [docs](http://kubernetes.io/docs/user-guide/deployments/) for more detail.


### Part 3 Configuration management basics

Well, our `nginx`es are up and running, let's make sure they actually do something useful by configuring them to say `hello, kubernetes!`

[ConfigMap](http://kubernetes.io/docs/user-guide/configmap/) is a special K8s resource that maps to configuration files or environment variables inside a Pod.

Lets create configmap from a directory. Our `conf.d` contains a `default.conf` file:

```
cat conf.d/default.conf
server {
    listen       80;
    server_name  localhost;

    location / {
        return 200 'hello, Kubernetes!';
    }
}
```

We can convert the whole directory into configmap:

```
kubectl create configmap my-nginx-v1 --from-file=conf.d
configmap "my-nginx-v1" created
```

```
kubectl describe configmaps/my-nginx-v1
Name:		my-nginx-v1
Namespace:	default
Labels:		<none>
Annotations:	<none>

Data
====
default.conf:	125 bytes

```

Every file is now it's own property, e.g. `default.conf`. Now, the trick is to mount this config map in the `/etc/nginx/conf.d/`
of our nginxes. We will use new deployment for this purpose:


```
cat my-nginx-configmap.yaml
```

```
kind: Deployment
metadata:
  labels:
    run: my-nginx
  name: my-nginx
  namespace: default
spec:
  replicas: 2
  selector:
    matchLabels:
      run: my-nginx
  template:
    metadata:
      labels:
        run: my-nginx
    spec:
      containers:
      - image: nginx:1.9.1
        name: my-nginx
        ports:
        - containerPort: 80
          protocol: TCP
        volumeMounts:
        - name: config-volume
          mountPath: /etc/nginx/conf.d
      volumes:
       - name: config-volume
         configMap:
           name: my-nginx-v1
```

Notice that we've introduced `volumes` section that tells k8s to attach volumes to the pods. One special volume type we support is `configMap` that
is created on the fly from the configmap resource `my-nginx-v1` that we've just created.

Another part of our config is `volumeMounts` that are specified for each container and tell it where to mount the volume.

Let's apply our config map:

```
kubectl apply -f my-nginx-configmap.yaml
```

Just as usual, new pods have been created:

```
kubectl get pods
NAME                        READY     STATUS    RESTARTS   AGE
my-nginx-3885498220-0c6h0   1/1       Running   0          39s
my-nginx-3885498220-9q61s   1/1       Running   0          38s
```

Out of curiosity, let's login into one of them and see ourselves the mounted configmap:

```
kubectl exec -ti my-nginx-3885498220-0c6h0 /bin/bash
cat /etc/nginx/conf.d/default.conf
server {
    listen       80;
    server_name  localhost;

    location / {
        return 200 'hello, Kubernetes!';
    }
}
```

and finally, let's see it all in action:

```
kubectl run -it --rm jumpbox --image=project42/jumpbox -- bash
curl http://my-nginx
hello, Kubernetes!
```

### Part 4 Connecting services

Let's deploy a bit more complicated stack. In this excercise we will deploy [Mattermost](http://www.mattermost.org) - an alternative to Slack that can run
on your infrastructure. We will build our own containers and configuration, push it to the registry and
Mattermost stack is composed of a worker process that connects to a running postgres instance.

**Build container**

Let's build container image for our worker and push it to our local private registry:

```
cd mattermost/worker
docker build -t mattermost-worker:2.1.0 .
```

**Note:** Notice the `$(minikube ip):5000` prefix. This is a private registry we've set up on our master server.

**Create configmap**

Mattermost's worker expects configuration to be mounted at:

`/var/mattermost/config/config.json`

```
cat mattermost/worker-config/config.json
```

If we examine config closely, we will notice that mattermost expects a connector
string to postgres:

```
   "DataSource": "postgres://postgres:mattermost@postgres:5432/postgres?sslmode=disable"
   "DataSourceReplicas": ["postgres://postgres:mattermost@postgres:5432/postgres?sslmode=disable"]
```

Here's where k8s power comes into play. We don't need to provide hardcoded IPs, we can simply make sure that
there's a `postres` service pointing to our Postgres DB running somewhere in the cluster.


Let us create config map based on this file:

```
kubectl create configmap mattermost-v1 --from-file=mattermost/worker-config
kubectl describe configmaps/mattermost-v1
Name:		mattermost-v1
Namespace:	default
Labels:		<none>
Annotations:	<none>

Data
====
config.json:	2951 bytes
```

**Starting Up Postgres**

Let's create a single Pod running posgres and point our service to it:

```
kubectl create -f mattermost/postgres.yaml
kubectl get pods
NAME                        READY     STATUS    RESTARTS   AGE
mattermost-database         1/1       Running   0          12m
```

Let's check out the logs of our postgres:

```
kubectl logs mattermost-database
The files belonging to this database system will be owned by user "postgres".
This user must also own the server process.

The database cluster will be initialized with locale "en_US.utf8".
The default database encoding has accordingly been set to "UTF8".
The default text search configuration will be set to "english".

Data page checksums are disabled.

fixing permissions on existing directory /var/lib/postgresql/data ... ok
creating subdirectories ... ok
selecting default max_connections ... 100
selecting default shared_buffers ... 128MB
```

**Note** Our `mattermost-database` is a special snowflake, in real production systems we must create a proper replicaset
for the stateful service, what is slightly more complicated than this sample.


**Creating Postgres Service**

Let's create postrges service:

```
kubectl create -f mattermost/postgres-service.yaml
```

Let's check out that everything is allright:

```
kubectl describe svc/postgres
Name:			postgres
Namespace:		default
Labels:			app=mattermost,role=mattermost-database
Selector:		role=mattermost-database
Type:			NodePort
IP:			    10.100.41.153
Port:			<unset>	5432/TCP
NodePort:		<unset>	31397/TCP
Endpoints:		10.244.40.229:5432
Session Affinity:	None
```

Seems like IP has been allocated and endpoints have been found.

**Creating Mattermost worker deployment**


```
cat mattermost/worker.yaml
```

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: mattermost
    role: mattermost-worker
  name: mattermost-worker
  namespace: default
spec:
  replicas: 1
  selector:
    matchLabels:
      role: mattermost-worker
  template:
    metadata:
      labels:
        app: mattermost
        role: mattermost-worker
    spec:
      containers:
      - image: mattermost-worker:2.1.0
        name: mattermost-worker
        ports:
        - containerPort: 80
          protocol: TCP
        volumeMounts:
        - name: config-volume
          mountPath: /var/mattermost/config
      volumes:
       - name: config-volume
         configMap:
           name: mattermost-v1
```

```
kubectl create -f mattermost/worker.yaml --record
```

Let's check out the status of the deployment to see if everything is allright:

```
kubectl describe deployments/mattermost-worker
Name:			mattermost-worker
Namespace:		default
CreationTimestamp:	Sun, 15 May 2016 23:56:57 +0000
Labels:			app=mattermost,role=mattermost-worker
Selector:		role=mattermost-worker
Replicas:		1 updated | 1 total | 1 available | 0 unavailable
StrategyType:		RollingUpdate
MinReadySeconds:	0
RollingUpdateStrategy:	1 max unavailable, 1 max surge
OldReplicaSets:		<none>
NewReplicaSet:		mattermost-worker-1848122701 (1/1 replicas created)
Events:
  FirstSeen	LastSeen	Count	From				SubobjectPath	Type		Reason			Message
  ---------	--------	-----	----				-------------	--------	------			-------
  3m		3m		1	{deployment-controller }			Normal		ScalingReplicaSet	Scaled up replica set mattermost-worker-1932270926 to 1
  1m		1m		1	{deployment-controller }			Normal		ScalingReplicaSet	Scaled up replica set mattermost-worker-1848122701 to 1
  1m		1m		1	{deployment-controller }			Normal		ScalingReplicaSet	Scaled down replica set mattermost-worker-1932270926 to 0
```

**Creating mattermost service**

Our last touch is to create mattermost service and check how it all works together:

```
kubectl create -f mattermost/worker-service.yaml
You have exposed your service on an external port on all nodes in your
cluster.  If you want to expose this service to the external internet, you may
need to set up firewall rules for the service port(s) (tcp:32321) to serve traffic.

See http://releases.k8s.io/release-1.2/docs/user-guide/services-firewalls.md for more details.
service "mattermost" created
```

Let's inspect the service spec:

```
cat mattermost/worker-service.yaml
```

Here's what we got. Notice `NodePort` service type.

```yaml
# service for web worker
apiVersion: v1
kind: Service
metadata:
  name: mattermost
  labels:
    app: mattermost
    role: mattermost-worker
spec:
  type: NodePort
  ports:
  - port: 80
    name: http
  selector:
    role: mattermost-worker
```

`NodePort` service type exposes a static port on every node in the cluster. In this case this port
is `32321`. This is handy sometimes when you are working on-prem or locally.

**Accessing the installation**

```
kubectl run -it --rm jumpbox --image=project42/jumpbox -- bash
curl http://mattermost

<!DOCTYPE html>
<html>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="robots" content="noindex, nofollow">
    <meta name="referrer" content="no-referrer">

    <title>Mattermost - Signup</title>
```

Okay, okay, we need to actually access the website now. Well, that' when `NodePort` comes in handy.
Let's view it a bit closer:

```
kubectl describe svc/mattermost
Name:			mattermost
Namespace:		default
Labels:			app=mattermost,role=mattermost-worker
Selector:		role=mattermost-worker
Type:			NodePort
IP:			10.100.226.155
Port:			http	80/TCP
NodePort:		http	32321/TCP
Endpoints:		10.244.40.23:80
Session Affinity:	None
```

Notice this:

```
NodePort:		http	32321/TCP
```

Here we see that on my Vagrant every node in the system should have `IP:32321` resolve to the mattermost web app.
On your Vagrant the port most likely will be different!

So on my computer I can now open mattermost app using one of the nodes IP:


![mattermost](images/mattermost.png)



### Recap

We've learned several quite important concepts like Services, Pods, Replicasets and
Configmaps. But that's just a small part of what Kubernetes can do. Read more on [Kubernetes portal](http://kubernetes.io)
