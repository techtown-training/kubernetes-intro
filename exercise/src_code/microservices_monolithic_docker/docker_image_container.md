## Basic Docker Image and Container commands

### List of Image actions
Below is a list of few basic and most useful action related to docker images
```
# Search images in docker hub
# Pull image from docker hub
# List all images
# Check Image history
# Inspect an image (check attributes like parent id, cmd, networks)
```

### List of Container actions
Below is a list of few basic and most useful action related to docker images
```
# Create a container
# List all running containers 
# List all running and stopped containers
# Check container logs
# Execute a unix command in command from host
# Attach to a running container 
# Check resource usage by each container
```


### 1 Image Commands

##### 1.1 Search and Pull
In this section, we will practice some image search and pull commands. The goal is to pull few images like (ubuntu, redis, alpine, centos) from docker hub to our local machine. 
```
docker search ubuntu
docker pull ubuntu
docker search alpine
docker pull alpine
docker search redis
docker pull redis
docker search centos
docker pull centos
```

##### 1.2 History and Inspect
*History of the image* - This shows all layers in the image. Docker images works on AuFS (augmented union file system). We will see more of this in the next chapter. Right now, we will only check that an image has multiple layers.
<br> <br>
*Inspect the image* - This shows some details of the image, such as Id, parent Id, Commands to run while starting container, GraphDriver, Networks, Default volumes, etc.
```
docker image history ubuntu
docker image history alpine
docker image history redis
docker image history centos
   --- For each image pulled, check the different layers of the image

docker image inspect ubuntu
docker image inspect alpine
docker image inspect redis
docker image inspect centos
   --- For each image, check parameters like "Parent", "Cmd", "GraphDriver.Data", and others.
```

##### 1.3 See list of images
```
docker image ls
   --- Show the list of images with their size
```


### 2 Container Commands

##### 2.1 Run containers
Container can be started by "docker run" command. Variety of parameters can be passed while running a container.
Few important ones are listed below. <br>

--name : This provides a user-defined name to the container <br> 
-i  : To run container in interactive mode <br>
-t : To return a terminal in the container <br>
-d : run container in background mode <br>
-p : publish ports <br>
-v : mount volumes (we will see this later) <br>
--network : attach networks (we will see this later) <br>
```
docker run --name cent1 centos
   --- container is created but stopped as it doesn't return anything
docker run -it --name cent2 centos
   --- this will give a terminal to work inside the centos container
   --- Press (cntrl P Q) to return to host without exiting the container 
   --- if you exit from the container, it will stop

docker container ls
   --- check all running containers
docker container ls -a
   --- check all running/stopped containers


docker run -d --name red1 redis
   --- run redis container in background
docker run -d --name red2 -P redis
   --- run redis container in background with all ports published randomly
docker run -d --name red3 -p 1234:6379 redis
   --- run redis container in background with 6379 port published as 1234
docker run -d --name red4 -p 1001:6379 redis
   --- run redis container in background with 6379 port published as 1001
   
   
docker container ls
   --- check all running containers
docker container ls -a
   --- check all running/stopped containers

docker container port red1
    --- check all ports published in the container --> it will show nothing
docker container port red2
   --- this will show 6379 published as a random port
docker container port red3
   --- this will show 6379 published as 1234
docker container port red4
   --- this will show 6379 published as 1001
```

##### 2.2 Test Redis Container
To test the redis container, let's install redis-tools first.
```
sudo apt-get install -y redis-tools
```

red3 container is published at port 1234. let's insert values in that container.
```
redis-cli -p 1234
```

This will take you to the redis command line, insert a value in redis using below commands.
```
set foo1 bar1
get foo1
keys *
   --- show all keys in redis
exit
```

red4 container is published at port 1001. let's insert values in that container.
```
redis-cli -p 1001
```

This will take you to the redis command line, insert a value in redis using below commands.
```
set foo2 bar2
get foo2
keys *
   --- this will only show foo2, not foo1 (because foo1 is in a different container)
exit
```

This confirms that there are multiple instances of redis running in different containers (and published as different ports)


##### 2.3 Check logs
```
docker container logs red2
docker container logs red3
   --- Log of the container - useful for monitoring
```

##### 2.4 Do something in the container from the host
Sometimes we want to run commands and take action in a running container. Docker provides two options to do this:
<br>
*Attach* : This attaches the process id of the host with the container, so attaching to a container will take you to the container bash
<br>
*Exec* : This runs unix commands in container from the host
First check if cent2 container is running, if not start a centos container in "-it" mode and exit by (cntrl P Q)
```
docker container attach cent2
docker container exec cent2 mkdir foo
docker container exec cent2 ls
```

##### 2.5 Resources used by containers
To check resource used by running containers
```
docker container stats
   --- cntrl Z to quit
```
