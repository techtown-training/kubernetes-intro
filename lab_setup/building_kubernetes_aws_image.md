## Building the Kubernetes image for the first time
Below describes the steps to create AWS image for kubernetes-intro-bootcamp course.

### Launch Ubuntu instance
- OS:- Ubuntu Server 16.04
- Machine type:- General Purpose t2.medium
- Auto-assign Public IP:- enable
- Storage: 16G
- Security Group: all-in-all

### Login to the ubuntu machine


### Install Docker and kubeadm, kubelet
```
sudo apt-get -y update && sudo apt-get -y upgrade && sudo apt-get install -y apt-transport-https
sudo apt install -y docker.io
sudo systemctl start docker
sudo systemctl enable docker
sudo su -
sudo curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key add
echo "deb http://apt.kubernetes.io/ kubernetes-xenial main" >> /etc/apt/sources.list.d/kubernetes.list
sudo apt-get -y update && sudo apt-get install -y kubelet kubeadm kubectl kubernetes-cni
```

### Create Image from the above instance

Create Image using AWS UI

## Image details
- Image name: microservices_kubernetes-bootcamp
- Id: ami-09ae5ef93a75b002a


## Instance connection
Three things are required to connect to AWS instance

- <b>username:</b> ubuntu (all small case)
- <b>Password:</b> There is no password, ASPE will provide the Default.ppk or Default.pem file. To mention here, if you are using unix/mac machines to connect, you need pem file, set the permission to `400`
- <b>IP:</b> ASPE will provide the IP address, for this course we need 1 instance per student

For the Kubernetes course, the instance will have Docker and kubernetes packages installed in it. But it doesnt have kubernetes master setup. That has been included in the mandatory exercise 2.1

