variable "aspe_courseID" {
  description = "ASPE Course ID"
}

variable "aspe_sessionID" {
  description = "ASPE Session ID"
}

variable "aws_accessKey" {
  description = "AWS Access Key"
}

variable "aws_amis" {
  type        = "map"
  description = "Region to AMI map"
  default = {
    "us-east-1" = "ami-0ac019f4fcb7cb7e6"
    "us-east-2" = "ami-0f65671a86f061fcd"
    "us-west-1" = "ami-063aa838bd7631e0b"
    "us-west-2" = "ami-0bbe6b35405ecebdb"
  }
}

variable "aws_availabilityZone" {
  description = "AWS Avalibility Zone"
}

variable "aws_egressCIDRblock" {
  description = "AWS Security Group egress CIDR block"
  type        = "list"
  default     = [ "0.0.0.0/0" ]
}

variable "aws_ingressCIDRblock" {
  description = "AWS Security Group ingress CIDR block"
  type        = "list"
  default     = [ "0.0.0.0/0" ]
}

variable "aws_instance_type" {
  description = "AWS instance type"
}

variable "aws_volume_type" {
  description = "AWS root volume type"
}

variable "aws_volume_size" {
  description = "AWS root block device size"
}

variable "aws_keyPairName" {
  description = "AWS Key Pair name"
}

variable "aws_region" {
  description = "AWS region"
}

variable "aws_secretKey" {
  description = "AWS Secrect Access Key"
}

variable "aws_subnetCIDRblock" {
  description = "AWS Subnet CIDR block"
}

variable "aws_subnetMapPublicIP" {
  description = "AWS Subnet map public IP"
  default = true
}

variable "aws_vpcCIDRblock" {
  description = "AWS VPC CIDR block"
}

variable "aws_vpcDnsHostNames" {
  description = "AWS VPC DNS Host Names"
  default     = true
}

variable "aws_vpcDnsSupport" {
  description = "AWS VPC DNS Support"
  default     = true
}

variable "aws_vpcTenancy" {
  description = "AWS VPC Tenancy"
  default     = "default"
}

variable "dockerVersion" {
  description = "Docker Version to install"
  default     = "5:18.09.6~3-0~ubuntu-bionic"
}

variable "instanceCount" {
  description = "Number of Lab Instances to create"
}
