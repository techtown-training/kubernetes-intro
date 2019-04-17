## Verify and Install necessary components for Kubernetes Master

### Login to the machine

For Unix/Mac user

```
ssh -i Default.pem ubuntu@IP
```

For Windows user, <br>
Use putty and the ppk file to login. The username is `ubuntu`


### Package installation
```bash
sudo apt-get -y update
sudo apt-get -y install default-jdk git maven redis-tools
```

### Verify Docker

```
sudo docker version
```

### Enable sudo(less) Docker

```
sudo groupadd docker
sudo usermod -aG docker $USER
exit
# log back again to the machine
```


### Verify kubectl installation

```
kubectl --help
```

### Install components for kubernetes master

```
sudo su -
sudo sysctl net.bridge.bridge-nf-call-iptables=1

# initialize kubeadm (done only for master)
kubeadm init --pod-network-cidr=10.244.0.0/16

# create kubeconfig so the user can run kubectl commands
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

# Install flannel networking
kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/v0.9.1/Documentation/kube-flannel.yml

Step 6 # Only when you want to use master node to host pods (since we have 1 node cluster)
kubectl taint nodes --all node-role.kubernetes.io/master-
```

### Test

```
kubectl get pods -o wide --all-namespaces
```

Now, you have one node cluster setup, up and running.


### Multi-node (Note that - we are not doing this as exercise !!)

In case you want to setup multi-node K8s cluster, <br>
*** Do not run `kubeadm init` command in the worker node. Instead run `kubeadm  token create --print-join-command` in the master node. This command will output a token, Copy and run the output of this command in the worker node. Repeat the same process for the number of worker nodes you want to join to the cluster.

