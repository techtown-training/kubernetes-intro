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
sudo apt-get update && sudo apt-get -y install docker.io apt-transport-https
```

```bash
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add
sudo apt-add-repository "deb http://apt.kubernetes.io/ kubernetes-xenial main"
```

```bash
sudo apt install -y kubelet=1.19.2-00 kubeadm=1.19.2-00 kubectl=1.19.2-00 kubernetes-cni=0.8.7-00
sudo swapoff -a
```

#### Initiate a new Kubernetes cluster on the Master node (do it only for the master node)
```bash
sudo kubeadm init --pod-network-cidr=10.244.0.0/16 --kubernetes-version=1.19.2
```

#### Configure kubectl
```
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```

#### overlay network plugin for Master (do this only in the master node)
```bash
kubectl apply -f https://github.com/weaveworks/weave/releases/download/v2.8.1/weave-daemonset-k8s.yaml
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

---

So far we have installed and configured the master node. But you won't be able to schedule pods yet. Because the cluster doesn't have any worker node, and in case you need just a single node cluster, your master node is not configured to schedule pods.

Let's test it.

#### pod scheduling won't work. Let's make it work
```bash
kubectl create deployment nginx --image=nginx
kubectl scale deployment nginx --replicas=3

# get the <ip-x-x-x-x> by running the command [kubectl get nodes -o wide]
kubectl describe nodes <ip-x-x-x-x>
# check the output of describe -- you will find "Taints: node-role.kubernetes.io/master:NoSchedule"

# Notice the pods are not scheduled but "Pending"
kubectl get pods

# Let's start over, first delete the deployment we just created
kubectl delete deployment/nginx

# use master for scheduling
export K8SMASTERNODE=$(kubectl get nodes | grep master | cut -d " " -f 1)
kubectl taint nodes $K8SMASTERNODE node-role.kubernetes.io/master-

# describe master node again and check the difference. you will find "Taints: <none>"
kubectl describe nodes <ip-x-x-x-x>

# schedule pod again (via deployment)
kubectl create deployment nginx --image=nginx
kubectl scale deployment nginx --replicas=3

# check pods and deployment, you will find it now.
kubectl get pods -o wide
kubectl get deployment -o wide
```

#### How to access nginx server? - Need to expose as service
```bash
kubectl expose deployment nginx --port=80 --type=NodePort
kubectl get deployments,svc -o wide
```

#### verify nginx from your K8s master node
```bash
curl <cluster-ip>:80
```

---

# Add Persistent storage with OpenEBS

Following are the basic steps to setup OpenEBS as a Persistent Volume provider for Kubernetes.  Much more info can be found on the [official documentation](https://docs.openebs.io/).

## iSCSI
OpenEBS provides block volume support through the iSCSI protocol. Therefore, the iSCSI client (initiator) presence on all Kubernetes nodes is required. Choose the platform below to find the steps to verify if the iSCSI client is installed and running or to find the steps to install the iSCSI client.

```bash
sudo apt-get install -y open-iscsi
sudo systemctl enable iscsid
sudo systemctl start iscsid
```

Verify that the initiator name is configured and the iSCSI services is running.
```bash
sudo cat /etc/iscsi/initiatorname.iscsi
systemctl status iscsid
```
## Install OpenEBS

OpenEBS can be installed on an existing Kubernetes cluster by applying the openebs-operator.yaml file.
```bash
kubectl apply -f https://openebs.github.io/charts/openebs-operator.yaml
```

We can verify the status of tho OpenEBS pods by looking at the pods in the openebs namespace.
```bash
kubectl get pods -n openebs -o wide
```

To set 'openebs-hostpath' as the default storageClass run this command.
```bash
kubectl patch storageclass openebs-hostpath -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"true"}}}'
```

---

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
