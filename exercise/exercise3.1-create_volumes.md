# Creating Volumes

### Create new pod with shared volume

Create a pod with multiple containers with shared volume

```
kubectl create -f pod.yaml
kubectl describe pod sharevol
```

Exec into the containers c1 and save some data

```
kubectl exec sharevol -it -c c1 -- bash
echo 'some data' > /tmp/xchange/data
```

Exit the container and exec into the second container

```
kubectl exec sharevol -it -c c2 -- bash
cat /tmp/data/data
```

Exit the container

### Cleanup
Delete all pods and volumes

```
kubectl delete po,svc --all
```

