## Elasticsearch for Kubernetes

This example uses only pods , and replication controllers to demonstrate the example. This example can be 
changed to use deplpyment and scale deployments instead of pods, and Replica 
Sets will be used instead of dealing with Replication Controllers.

### Clone the examples repo

```
cd /root
git clone https://github.com/kubernetes/examples.git
cd examples/
```

Kubernetes makes it trivial for anyone to easily build and scale [Elasticsearch](http://www.elasticsearch.org/) clusters. Here, you'll find how to do so.
Current Elasticsearch version is `5.6.2`.

[A more robust example that follows Elasticsearch best-practices of separating nodes concern is also available](production_cluster/README.md).

Current pod descriptors use an `emptyDir` for storing data in each data node container. This is meant to be for the sake of simplicity and [should be adapted according to your storage needs](https://kubernetes.io/docs/concepts/storage/persistent-volumes/).

## Docker image

The [pre-built image](https://github.com/pires/docker-elasticsearch-kubernetes) used in this example will not be supported. Feel free to fork to fit your own needs, but keep in mind that you will need to change Kubernetes descriptors accordingly.

## Deploy

Let's kickstart our cluster with 1 instance of Elasticsearch.

```
kubectl create -f staging/elasticsearch/service-account.yaml
kubectl create -f staging/elasticsearch/es-svc.yaml
kubectl create -f staging/elasticsearch/es-rc.yaml
```

The [io.fabric8:elasticsearch-cloud-kubernetes](https://github.com/fabric8io/elasticsearch-cloud-kubernetes) plugin requires limited access to the Kubernetes API in order to fetch the list of Elasticsearch endpoints.
If your cluster has the RBAC authorization mode enabled, create the additional `Role` and `RoleBinding` with:

```
DO NOT RUN THIS, our's is not RBAC enabled
kubectl create -f staging/elasticsearch/rbac.yaml
```

Let's see if it worked (your pod name will be different):

```
$ kubectl get pods
NAME       READY     STATUS    RESTARTS   AGE
es-q8q2v   1/1       Running   0          2m
```

Check the logs of the pod_name (which we got from the previous command)
```
$ kubectl logs es-q8q2v
[2017-10-02T11:39:22,347][INFO ][o.e.n.Node               ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] initializing ...
[2017-10-02T11:39:22,579][INFO ][o.e.e.NodeEnvironment    ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] using [1] data paths, mounts [[/data (/dev/sda1)]], net usable_space [92.5gb], net total_space [94.3gb], spins? [possibly], types [ext4]
[2017-10-02T11:39:22,579][INFO ][o.e.e.NodeEnvironment    ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] heap size [503.6mb], compressed ordinary object pointers [true]
[2017-10-02T11:39:22,581][INFO ][o.e.n.Node               ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] node name [ece3d296-dbd3-46a3-b66c-8b4c282610af], node ID [Rc-odsaESxSAnvOBFg4MNA]
[2017-10-02T11:39:22,582][INFO ][o.e.n.Node               ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] version[5.6.2], pid[9], build[57e20f3/2017-09-23T13:16:45.703Z], OS[Linux/4.4.64+/amd64], JVM[Oracle Corporation/OpenJDK 64-Bit Server VM/1.8.0_131/25.131-b11]
[2017-10-02T11:39:22,583][INFO ][o.e.n.Node               ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] JVM arguments [-XX:+UseConcMarkSweepGC, -XX:CMSInitiatingOccupancyFraction=75, -XX:+UseCMSInitiatingOccupancyOnly, -XX:+DisableExplicitGC, -XX:+AlwaysPreTouch, -Xss1m, -Djava.awt.headless=true, -Dfile.encoding=UTF-8, -Djna.nosys=true, -Djdk.io.permissionsUseCanonicalPath=true, -Dio.netty.noUnsafe=true, -Dio.netty.noKeySetOptimization=true, -Dlog4j.shutdownHookEnabled=false, -Dlog4j2.disable.jmx=true, -Dlog4j.skipJansi=true, -XX:+HeapDumpOnOutOfMemoryError, -Xms512m, -Xmx512m, -Des.path.home=/elasticsearch]
[2017-10-02T11:39:24,386][INFO ][o.e.p.PluginsService     ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] loaded module [aggs-matrix-stats]
[2017-10-02T11:39:24,388][INFO ][o.e.p.PluginsService     ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] loaded module [ingest-common]
[2017-10-02T11:39:24,388][INFO ][o.e.p.PluginsService     ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] loaded module [lang-expression]
[2017-10-02T11:39:24,388][INFO ][o.e.p.PluginsService     ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] loaded module [lang-groovy]
[2017-10-02T11:39:24,388][INFO ][o.e.p.PluginsService     ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] loaded module [lang-mustache]
[2017-10-02T11:39:24,388][INFO ][o.e.p.PluginsService     ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] loaded module [lang-painless]
[2017-10-02T11:39:24,388][INFO ][o.e.p.PluginsService     ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] loaded module [parent-join]
[2017-10-02T11:39:24,388][INFO ][o.e.p.PluginsService     ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] loaded module [percolator]
[2017-10-02T11:39:24,388][INFO ][o.e.p.PluginsService     ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] loaded module [reindex]
[2017-10-02T11:39:24,389][INFO ][o.e.p.PluginsService     ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] loaded module [transport-netty3]
[2017-10-02T11:39:24,389][INFO ][o.e.p.PluginsService     ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] loaded module [transport-netty4]
[2017-10-02T11:39:24,389][INFO ][o.e.p.PluginsService     ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] no plugins loaded
[2017-10-02T11:39:27,395][INFO ][o.e.d.DiscoveryModule    ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] using discovery type [zen]
[2017-10-02T11:39:28,754][INFO ][o.e.n.Node               ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] initialized
[2017-10-02T11:39:28,758][INFO ][o.e.n.Node               ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] starting ...
[2017-10-02T11:39:29,132][INFO ][o.e.t.TransportService   ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] publish_address {10.44.2.5:9300}, bound_addresses {10.44.2.5:9300}
[2017-10-02T11:39:29,154][INFO ][o.e.b.BootstrapChecks    ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] bound or publishing to a non-loopback or non-link-local address, enforcing bootstrap checks
[2017-10-02T11:39:32,264][INFO ][o.e.c.s.ClusterService   ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] new_master {ece3d296-dbd3-46a3-b66c-8b4c282610af}{Rc-odsaESxSAnvOBFg4MNA}{YvzOdsplT12C-9c7X3O8Xw}{10.44.2.5}{10.44.2.5:9300}, reason: zen-disco-elected-as-master ([0] nodes joined)
[2017-10-02T11:39:32,315][INFO ][o.e.h.n.Netty4HttpServerTransport] [ece3d296-dbd3-46a3-b66c-8b4c282610af] publish_address {10.44.2.5:9200}, bound_addresses {10.44.2.5:9200}
[2017-10-02T11:39:32,316][INFO ][o.e.n.Node               ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] started
[2017-10-02T11:39:32,331][INFO ][o.e.g.GatewayService     ] [ece3d296-dbd3-46a3-b66c-8b4c282610af] recovered [0] indices into cluster_state
```

So we have a 1-node Elasticsearch cluster ready to handle some work.

## Scale
If you want you can scale ES.
We prefer not to do that for the lab purpose, because the machines we use is not too large.

## Access the service

*Don't forget* that services in Kubernetes are only acessible from containers in the cluster. For different behavior you should [configure the creation of an external load-balancer](https://kubernetes.io/docs/concepts/services-networking/service/#type-loadbalancer). While it's supported within this example service descriptor, its usage is out of scope of this document, for now.

```
$ kubectl get service elasticsearch
NAME            CLUSTER-IP      EXTERNAL-IP      PORT(S)                         AGE
elasticsearch   10.47.252.248   <pending>   9200:31394/TCP,9300:30907/TCP   6m
```

You can do a describe for the service, and even see the Ips and other details. From any host on your cluster  we
can use the port 9200, and from outside we can use the IP_address of the machine but on 31394 from this example run:

```
$ curl <AWS IP>:<NodePort>
or go to browser http://public_ip:31394
```

You should see something similar to the following:

```json
{
  "name" : "ece3d296-dbd3-46a3-b66c-8b4c282610af",
  "cluster_name" : "myesdb",
  "cluster_uuid" : "lb76DGaGS1msgwC3w8H9Qg",
  "version" : {
    "number" : "5.6.2",
    "build_hash" : "57e20f3",
    "build_date" : "2017-09-23T13:16:45.703Z",
    "build_snapshot" : false,
    "lucene_version" : "6.6.1"
  },
  "tagline" : "You Know, for Search"
}
```

Or if you want to check cluster information:

```
curl 35.189.128.215:9200/_cluster/health?pretty
```

You should see something similar to the following:

```json
{
  "cluster_name" : "myesdb",
  "status" : "green",
  "timed_out" : false,
  "number_of_nodes" : 1,
  "number_of_data_nodes" : 1,
  "active_primary_shards" : 0,
  "active_shards" : 0,
  "relocating_shards" : 0,
  "initializing_shards" : 0,
  "unassigned_shards" : 0,
  "delayed_unassigned_shards" : 0,
  "number_of_pending_tasks" : 0,
  "number_of_in_flight_fetch" : 0,
  "task_max_waiting_in_queue_millis" : 0,
  "active_shards_percent_as_number" : 100.0
}
```
## Get and Post Requests for the Elastic Search cluster
Now Lets play with some Get and Post Requests for the Elastic Search cluster. We'll use the example of a blog engine, 
which has some posts and comments. We shall use the command line to index some data to our elastic Search Cluster, 
and we shall query the results from our browser , to see it all in action

### Create Indexes

Insert name of the user
```
curl -XPUT 'http://<Host_Name>:9200/blog/user/dilbert' -d '{ "name" : "Dilbert Brown" }'
```
Insert post 1
```
curl -XPUT 'http://<Host_Name>:9200/blog/post/1' -d '
{ 
    "user": "dilbert", 
    "postDate": "2011-12-15", 
    "body": "Search is hard. Search should be easy." ,
    "title": "On search"
}'
```
Insert post 2
```
curl -XPUT 'http://<Host_Name>:9200/blog/post/2' -d '
{ 
    "user": "dilbert", 
    "postDate": "2011-12-12", 
    "body": "Distribution is hard. Distribution should be easy." ,
    "title": "On distributed search"
}'
```
Insert post 3
```
curl -XPUT 'http://<Host_Name>:9200/blog/post/3' -d '
{ 
    "user": "dilbert", 
    "postDate": "2011-12-10", 
    "body": "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat" ,
    "title": "Lorem ipsum"
}'
```
Now its time to query our records (Go to the browser, and lets check our query results). These resutls are powered by
our 3 pods of ES instances running in Kubernetes.

Verify all the PUT requestes were successful

```
check URL http://<AWS IP>:<Load_Balancer_Port>/blog/_search
check URL 'http://<Public_IP>:<Load_Balancer_Port>/blog/user/dilbert?pretty=true'
check URL 'http://<Public_IP>:<Load_Balancer_Port>/blog/post/1?pretty=true'
check URL 'http://<Public_IP>:<Load_Balancer_Port>/blog/post/2?pretty=true'
check URL 'http://<Public_IP>:<Load_Balancer_Port>/blog/post/3?pretty=true'
```
### Searching Examples - 

1. All posts which don't contain the term search:
```
go to URL 'http://<Public_IP>:<Load_Balancer_Port>/blog/post/_search?q=-title:search&pretty=true'
```
2. Retrieve the title of all posts which contain search and not distributed:
```
Go to URL 'http://<Public_IP>:<Load_Balancer_Port>/blog/post/_search?q=+title:search%20-title:distributed&pretty=true&fields=title'
```

cleanup
```
kubectl delete --all pods,svc,deploy,rc,rs --namespace=default
```

Reference:- http://www.elasticsearchtutorial.com/elasticsearch-in-5-minutes.html


