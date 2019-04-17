## 1.1 Docker Monitoring

#### google/cadvisor

This is used for monitoring images and containers running in the host machine.

**Steps to run:**
* Pull the google/cadvisor image

```
docker pull google/cadvisor
```

* Run a container
```
docker run \
  --volume=/:/rootfs:ro \
  --volume=/var/run:/var/run:rw \
  --volume=/sys:/sys:ro \
  --volume=/var/lib/docker/:/var/lib/docker:ro \
  --volume=/dev/disk/:/dev/disk:ro \
  --publish=8080:8080 \
  --detach=true \
  --name=cadvisor \
  google/cadvisor:latest
```
<br>

* Test in browser
```
http://YourIPAddress:8080
```

<br>

## 1.2 Private Docker Registry

Private registries are used to store images privately and henceforth used to share the images across teams and environments.

* Pull the image and run a container
```
docker pull registry:2
docker run -d -p 5000:5000 --restart always --name registry registry:2
```

* Test the registry
```
# pull ubuntu image from public repository
docker pull ubuntu

# Tag the ubuntu image as localhost:5000/....
docker tag ubuntu localhost:5000/myubuntu:1.0


# Push the tagged image to private registry
docker push localhost:5000/myubuntu:1.0

# Remove locally available tagged ubuntu image and the ubuntu image itself 
docker rmi -f localhost:5000/myubuntu:1.0
docker rmi –f ubuntu

# List the image and confirm that no ubuntu image should be available locally
docker images

# Pull from the private registry into local
docker pull localhost:5000/myubuntu:1.0

# Run a container
docker run -it localhost:5000/myubuntu:1.0
```

* Check the registry using curl OR browser
```
# list the catalog
curl -XGET http://localhost:5000/v2/_catalog

# list all tags for a specific image
curl -XGET http://localhost:5000/v2/myubuntu/tags/list
```

It is a common practice to persist the image files in the registry. So even when the container is killed, the image files are persisted.

<br>


 