## Lab Setup for kubernetes-intro-bootcamp course

### Create instances

- Select image `ami-09ae5ef93a75b002a` OR `microservices_kubernetes-bootcamp`
- Type of instance required `t2.medium`
- Enable `Auto-assign public IP`
- 16G storage
- Select `all-in-all` security group


### Number of Instances required
1 per student


### Test connection and instance (for ASPE)
Login to one of the instances and run command `kubectl --help` to verify if Docker runs properly

### Test connection and instance (for clients)
Following needs to be sent to the client to test their connection

- IP address of one of the machines
- Default.pem/ppk files
- Word document with testing instruction
```
Test_Instance_and_Connection.docx
```

All required instruction for testing in mentioned in the word document.


<br>
*** <b>Note</b> – no other “kubectl” command will run as of now, because setting `Kubernetes master` is included as a part of `exercise 2.1`, described here -- `https://github.com/techtown-training/kubernetes-intro/blob/master/exercise/exercise2.1-install_kube_master.md`


## Instance connection
Three things are required to connect to AWS instance

- <b>username:</b> ubuntu (all small case)
- <b>Password:</b> There is no password, ASPE will provide the Default.ppk or Default.pem file. To mention here, if you are using unix/mac machines to connect, you need pem file, set the permission to `400`
- <b>IP:</b> ASPE will provide the IP address, for this course we need 1 instance per student

For the microservice course, the instance will have Docker and kubernetes packages installed in it. But it doesnt have kubernetes master setup. That has been included in the mandatory exercise 2.1
