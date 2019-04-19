## Install kubernetes on a ubuntu machine using kubeadm

## Setting up the machine

The VM should have at least 2 vCPUs

#### Login to the machine
```bash
ssh <username>@<FQDN>
```

In case you are logging using a pem file

```bash
chmod 400 <path_to_pen_file>
ssh -i <path_to_pen_file> <username>@<FQDN>
```

#### Install docker, kubernetes
```bash
sudo su -
apt-get update && apt-get -y install docker.io apt-transport-https
systemctl enable docker.service
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key add -
echo "deb http://apt.kubernetes.io/ kubernetes-xenial main" | tee /etc/apt/sources.list.d/kubernetes.list
apt-get update -y
apt-get install -y kubelet kubeadm
```

#### Configure Master node (do it only for the master node)
```bash
kubeadm init --pod-network-cidr=10.244.0.0/16
mkdir -p $HOME/.kube
cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
chown $(id -u):$(id -g) $HOME/.kube/config
```

#### overlay network plugin for Master (do this only in the master node)
```bash
export kubever=$(kubectl version | base64 | tr -d '\n')
kubectl apply -f "https://cloud.weave.works/k8s/net?k8s-version=$kubever"
```

#### Verify kubernetes master
```bash
kubectl get nodes -o wide
kubectl get pods --all-namespaces -o wide
kubectl get pods,deployments --all-namespaces -o wide
```

#### check kube config file
```bash
cat .kube/config
kubectl cluster-info
```

So far we have installed and configured the master node. But you won't be able to schedule pods yet. Because the cluster doesn't have any worker node, and in case you need just a single node cluster, your master node is not configured to schedule pods.

Let's test it.

#### pod scheduling won't work. Let's make it work
```bash
kubectl run nginx --image=nginx --replicas=3

# get the <ip-x-x-x-x> by running the command [kubectl get nodes -o wide]
kubectl describe nodes <ip-x-x-x-x>
# check the output of describe -- you will find "Taints: node-role.kubernetes.io/master:NoSchedule"

# delete the deployment we just created
kubectl delete deployment.extensions/nginx

# use master for scheduling
ip=`kubectl get nodes | grep master | awk -F" " '{print $1}'`
kubectl taint nodes $ip node-role.kubernetes.io/master:NoSchedule-

# describe master node again and check the difference. you will find "Taints: <none>"
kubectl describe nodes <ip-x-x-x-x>

# schedule pod again (via deployment)
kubectl run nginx --image=nginx --replicas=3

# check pods and deployment, you will find it now.
kubectl get pods -o wide
kubectl get deployment -o wide
```

#### How to access nginx server? - Need to expose as service
```bash
kubectl expose deployment nginx --port=80 --type=LoadBalancer
kubectl get deployments,svc -o wide
```

#### verify nginx
```bash
curl <public-ip>:<exposed port>
OR
curl <cluster-ip>:80
```
<br><br>
### Advanced 1: How to setup multi-node kube cluster - just discussion, no lab required
> Install docker, kubeadm, kubelet in worker nodes - same as master <br>
> In the worker: DO NOT "kubeadm init" and DO NOT "copy .kube/config" to home folder <br>
> Get token from master by running the command
[kubeadm  token create --print-join-command]
> Run the output of the above command in the worker nodes
> DO NOT taint master for scheduling

<br><br>
### Advanced 2: How to configure access to multiple clusters - just discussion, no lab required
#### Configure local kubectl to connect to another master kube cluster - copy "cluster CA certificate" of the master cluster to a file {remote.cluster.ca.cert}
	- kubectl config set-cluster master-cluster --server=<endpoint> --certificate-authority=remote.cluster.ca.cert
	- kubectl config set-credentials master-cluster-admin --username <username> --password <password>
	- verify by "kubectl config view"
	* kubectl will still pull local kube cluster information if you run "kubectl get" commands
	- create a context: kubectl config set-context frontend --cluster=master --namespace=frontend
	- switch to the new context: kubectl config use-context frontend
	* kubectl will now switch to new context and connect to master cluster. 
	* Check kubectl config help to see different options to configure local kubectl [kubectl config --help]
