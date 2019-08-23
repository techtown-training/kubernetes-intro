provider "aws" {
  access_key = "${var.aws_accessKey}"
  secret_key = "${var.aws_secretKey}"
  region     = "${var.aws_region}"
}
