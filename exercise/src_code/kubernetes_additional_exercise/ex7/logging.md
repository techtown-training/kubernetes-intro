# kubernetes-logging

## Create a loging pod

```
kubectl create -f pod.yaml
```

Grab the last few lines of logs

```
kubectl logs --tail=5 po/logme
```

Grab a stream of the logs
```
kubectl logs ‐f ‐‐since=10s po/logme
```

## Cleanup

```
kubectl delete -f pod.yaml
```

## Launch a container that performs an action and then exits


You can also view logs of pods that have already completed their lifecycle. For this we create a pod called oneshot that counts down from 9 to 1 and then exits. Using the -p option you can print the logs for previous instances of the container in a pod:

```
kubectl create -f oneshotpod.yaml
kubectl logs -p oneshot -c gen
```

## Cleanup
```
kubectl delete -f oneshotpod.yaml
```

