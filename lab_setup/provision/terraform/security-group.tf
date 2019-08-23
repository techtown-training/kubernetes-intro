resource "aws_security_group" "labVPC_Security_Group" {
  vpc_id       = "${aws_vpc.labVPC.id}"
  name         = "${var.aspe_courseID}-${var.aspe_sessionID}-Lab"
  description  = "${var.aspe_courseID}-${var.aspe_sessionID}-Lab Security Group"
  egress {
    cidr_blocks = "${var.aws_egressCIDRblock}"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
  }
  ingress {
    cidr_blocks = "${var.aws_ingressCIDRblock}"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
  }
  tags = {
    Course  = "${var.aspe_courseID}"
    Session = "${var.aspe_sessionID}"
    Name    = "${var.aspe_courseID}-${var.aspe_sessionID}-Lab"
  }
}
