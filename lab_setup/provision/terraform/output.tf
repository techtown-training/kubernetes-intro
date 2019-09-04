output "ami" {
  value = "${lookup(var.aws_amis, var.aws_region)}"
}

output "lab_instances" {
  value = "${aws_instance.dockerLab.*.tags.Name}"
}

output "public_dns" {
  value = "${aws_instance.dockerLab.*.public_dns}"
}

output "public_ips" {
  value = "${aws_instance.dockerLab.*.public_ip}"
}

output "private_ips" {
  value = "${aws_instance.dockerLab.*.private_ip}"
}
