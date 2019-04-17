## Dockerfile

#### 1.1 Docker build Image

* Create a folder '**first**' in your virtual machine. <br>
* Create a text file '**temp.file**' to copy to the image. <br>
* Create **Dockerfile**
 
```
mkdir first
cd first
echo "Hello there" >> temp.file
vi Dockerfile
```

<br>

Below content goes in the Dockerfile. These are the instructions to create an image.
* Copy the below content and paste to the Dockerfile

```
# use ubuntu base image 
FROM ubuntu:latest

# update apt-get package manager
RUN apt-get update

# install wget
RUN apt-get install -y wget

# install tree and redis-tools together
RUN apt-get install -y tree redis-tools

# create two directories foo and bar in the image
RUN mkdir foo
RUN mkdir bar

# copy local file to foo directory 
ADD temp.file foo/

# return bash while starting the container
CMD ["/bin/sh"]
```


* Save and exit the Dockerfile.
* Build your image
```
docker build -t myubuntu:latest .
   --- There are 8 steps. Check each..
```

* Check list of images
```
docker images
```

<br>

#### 1.2 Docker inspect Image

* Inspect the new image
```
docker image inspect myubuntu
```

<br>

Check individual keys from the inspect json.


* Get the id the parent image
```
docker inspect -f {{.Parent}} myubuntu
   --- this should provide a sha key, like "sha256:41025a43c32a889feda9bab2......."
```

* Get the Cmd that has been run in this image
```
docker inspect -f {{.ContainerConfig.Cmd}} myubuntu
   --- this should output { [/bin/sh -c #(nop)  CMD ["/bin/sh"]] }
```

<br>

##### 1.2.1 Verify the layers in the image

The goal is to verify layers of the  newly created image. <br>
Inspect the image, check the Cmd, get the parent id and then inspect the parent image.
```
docker inspect -f {{.Parent}} <parent of myubuntu>
docker inspect -f {{.ContainerConfig.Cmd}} <parent of myubuntu>
    --- this should output something like:- [/bin/sh -c #(nop) ADD file:664c2525e162e... in foo/ ]
```

* Continue doing until you get no parent id.
* Check the ContainerConfig.Cmd in each step

This will verify that all individual instruction created a separate image layer

<br>

#### 1.3 Run a container from the newly created image

* Run a container in interactive mode
```
docker run -it --name myubuntu1 myubuntu
```

In the container, check things that we instructed in the Dockerfile.

>> check the foo and bar folders <br>
>> temp.file inside foo folder <br> 
>> use "tree" package (just type 'tree' and enter)

* exit the container by (cntrl P Q).. We want the container to be running.

<br>

#### 1.4 exec and attach to the container

Use exec commands to run commands inside the container from the host
 
```
docker exec myubuntu1 ls foo
   --- to see temp.file inside foo directory
docker exec myubuntu1 mkdir foo2
   --- create a new folder foo2
docker exec myubuntu1 ls
   --- ls all files
```

<br>

#### 1.5 kill and re-start the container

```
docker stop myubuntu1
   --- this will stop the running container
docker exec myubuntu1 ls foo
   --- this will give error:- the container is not running
docker ps
docker ps -a
docker start myubuntu1
   --- re-start the container
docker exec myubuntu1 ls foo
   --- works this time
   ```
 <br>
 