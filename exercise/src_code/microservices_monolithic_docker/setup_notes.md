## Run Monolithic Dockerized Application in Java in Ubuntu machine

## * Setting up the machine

```bash
ssh ubuntu@FQDN
```

#### apt-get update and install java, git, maven
```bash
sudo apt-get -y update
sudo apt-get -y install default-jdk git maven redis-tools
```

#### It is assumed that your machine has Docker installed in it. If not, please install Docker.

### Note that docker-compose installation is needed only if your machine doesn't have docker-compose
#### install docker-compose

```bash
sudo curl -L https://github.com/docker/compose/releases/download/1.21.0/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

##### check docker and docker-compose
```bash
sudo docker version
sudo docker-compose version
```

## * Clone the git project

```bash
git clone https://github.com/techtown-training/microservices-bootcamp.git
cd microservices-bootcamp/exercise/src_code/microservices_monolithic_docker/
```

#### build the maven project locally

```bash
cd restful-test/
mvn clean install -U
cd ..
```

#### build the Docker image for the maven project and redis data upload

```bash
sudo docker build -f Dockerfile.dataloader -t java_mvn_redis_loader:1.0 .
```

#### run the docker-compose
```bash
sudo docker-compose up -d
```

#### check running docker containers
```bash
sudo docker ps
```

Wait until "redis_loader" container gets killed. <br>
This container is responsible for loading static data to redis datastore.

<br>

## * Test the application

#### Run below http either in browser or using curl (note: change the IP of your machine)
* http://IP:8080/sample-monolithic-1.0/rest/retailDesign/getallstocks
* http://IP:8080/sample-monolithic-1.0/rest/retailDesign/getallproducts
* http://IP:8080/sample-monolithic-1.0/rest/retailDesign/getstock?productId=1
* http://IP:8080/sample-monolithic-1.0/rest/retailDesign/getstock?productId=3
* http://IP:8080/sample-monolithic-1.0/rest/retailDesign/getproduct?id=2
* http://IP:8080/sample-monolithic-1.0/rest/retailDesign/getproduct?id=5

* http://IP:8080/sample-monolithic-1.0/rest/retailDesign/combinedproduct?productId=1&id=1
* http://IP:8080/sample-monolithic-1.0/rest/retailDesign/combinedproduct?productId=1&id=2

<br>
## * Source code

<b>Docker file</b> for data loading to Redis

```
# docker build -f Dockerfile.dataloader -t java_mvn_redis_loader:1.0 .
FROM maven

RUN apt-get update
RUN mkdir /code
ADD . /code
WORKDIR /code/restful-test

CMD ["mvn", "clean", "install", "-U"]
```

<b> docker compose file </b> to start the app with 3 containers

```
version: "3"
services:
  retail_app:
    image: tomcat
    depends_on:
      - "redis_loader"
    volumes:
     - ./restful-test/target/sample-monolithic-1.0.war:/usr/local/tomcat/webapps/sample-monolithic-1.0.war
    ports:
     - "8080:8080"

  redis_loader:
    image: java_mvn_redis_loader:1.0
    container_name: redis_loader
    command: mvn exec:java -Dexec.mainClass="com.app.startup.DataLoader"
    depends_on:
      - "redis"

  redis:
    image: "redis:alpine"
    container_name: redis
    ports:
     - "6379:6379"
#docker run -it -p 8080:8080 -v ~/restful-test/target/sample-monolithic-1.0.war:/usr/local/tomcat/webapps/sample-monolithic-1.0.war tomcat
```