resource "aws_instance" "dockerLab" {
  ami                         = "${lookup(var.aws_amis, var.aws_region)}"
  instance_type               = "${var.aws_instance_type}"
  key_name                    = "${var.aws_keyPairName}"
  associate_public_ip_address = true
  subnet_id                   = "${aws_subnet.labVPC_Subnet.id}"
  vpc_security_group_ids      = ["${aws_security_group.labVPC_Security_Group.id}"]
  count                       = "${var.instanceCount}"
  provisioner "local-exec" {
    command = "ANSIBLE_HOST_KEY_CHECKING=False AWS_ACCESS_KEY_ID=${var.aws_accessKey} AWS_SECRET_ACCESS_KEY=${var.aws_secretKey} ansible-playbook -i ec2.py playbooks/provision.yml -u ubuntu --become --private-key ~/.ssh/ASPE-Default.pem --limit ${self.public_ip} --extra-vars 'ip=${self.public_ip}' --extra-vars 'hostname=lab${count.index}' --extra-vars 'dockerversion=${var.dockerVersion}'"
  }
  root_block_device {
    volume_type = "${var.aws_volume_type}"
    volume_size = "${var.aws_volume_size}"
  }
  tags = {
    Course  = "${var.aspe_courseID}"
    Session = "${var.aspe_sessionID}"
    Name    = "${var.aspe_courseID}-${var.aspe_sessionID}-lab${count.index}"
  }
}
