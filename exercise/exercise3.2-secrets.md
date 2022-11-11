# Tutorial: Secrets

In this tutorial you will learn how to create and consume Kubernetes secrets.

Create the example application configuration file:

```
cat << EOF > config.json
{
  "username": "admin",
  "password": "123456789"
}
EOF
```

Create the `oscon` secret:

```
kubectl create secret generic oscon \
  --from-literal=username=admin \
  --from-literal=password=123456789 \
  --from-file=config.json
```

Describe the `oscon` secret:

```
kubectl describe secrets oscon
```

Run the `secrets` job to fetch the secrets and log the secrets:

```
kubectl create -f https://raw.githubusercontent.com/kelseyhightower/secrets/master/secrets.yaml
```

View the logs of the `secrets` job:

```
kubectl get pods -a
```

```
kubectl logs <print-secrets-pod-name>
```

## Managing Secrets with Vault

In this tutorial you will deploy a Vault server running in dev mode.

### Install the vault client on your client machine:

[https://www.vaultproject.io/downloads.html](https://www.vaultproject.io/downloads.html)

On ubuntu us this command to install 'vault'

```bash
sudo snap install vault
```

### Deploy a Vault Server

```
kubectl create -f https://gist.githubusercontent.com/thejordanclark/a1bedeeeeba0e706a11187fcb1f30812/raw/a8be28a00c9043f3a3720957160ed1466f811861/vault.yaml
```

```
kubectl get pods
```

```
kubectl get svc
```

```
kubectl logs vault-0
```

### Connect to the Vault Server

```
kubectl port-forward vault-0 8200:8200
```

```
export VAULT_ADDR="http://127.0.0.1:8200"
```

```
export VAULT_TOKEN="3e4a5ba1-oscon-422b-d1db-844979cab098"
```

```
vault status
```


### Add Secrets

```
vault write secret/oscon \
  username=admin \
  password=123456789
```
