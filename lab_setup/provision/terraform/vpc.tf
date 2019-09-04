resource "aws_vpc" "labVPC" {
  cidr_block           = "${var.aws_vpcCIDRblock}"
  instance_tenancy     = "${var.aws_vpcTenancy}"
  enable_dns_support   = "${var.aws_vpcDnsSupport}"
  enable_dns_hostnames = "${var.aws_vpcDnsHostNames}"
  tags = {
    Course  = "${var.aspe_courseID}"
    Session = "${var.aspe_sessionID}"
    Name    = "${var.aspe_courseID}-${var.aspe_sessionID}-Lab"
  }
}

resource "aws_subnet" "labVPC_Subnet" {
  vpc_id                  = "${aws_vpc.labVPC.id}"
  cidr_block              = "${var.aws_subnetCIDRblock}"
  map_public_ip_on_launch = "${var.aws_subnetMapPublicIP}"
  availability_zone       = "${var.aws_availabilityZone}"
  tags = {
    Course  = "${var.aspe_courseID}"
    Session = "${var.aspe_sessionID}"
    Name    = "${var.aspe_courseID}-${var.aspe_sessionID}-Lab"
  }
}

resource "aws_network_acl" "labVPC_Security_ACL" {
  vpc_id     = "${aws_vpc.labVPC.id}"
  subnet_ids = [ "${aws_subnet.labVPC_Subnet.id}" ]
  egress {
    rule_no    = 200
    from_port  = 0
    to_port    = 0
    protocol   = "-1"
    cidr_block = "0.0.0.0/0"
    action     = "allow"
  }
  ingress {
    rule_no    = 100
    from_port  = 0
    to_port    = 0
    protocol   = "-1"
    cidr_block = "0.0.0.0/0"
    action     = "allow"
  }
  tags = {
    Course  = "${var.aspe_courseID}"
    Session = "${var.aspe_sessionID}"
    Name    = "${var.aspe_courseID}-${var.aspe_sessionID}-Lab"
  }
}

resource "aws_internet_gateway" "labVPC_GW" {
  vpc_id = "${aws_vpc.labVPC.id}"
  tags = {
    Course  = "${var.aspe_courseID}"
    Session = "${var.aspe_sessionID}"
    Name    = "${var.aspe_courseID}-${var.aspe_sessionID}-Lab"
  }
}

resource "aws_route_table" "labVPC_route_table" {
    vpc_id = "${aws_vpc.labVPC.id}"
    tags = {
      Course  = "${var.aspe_courseID}"
      Session = "${var.aspe_sessionID}"
      Name    = "${var.aspe_courseID}-${var.aspe_sessionID}-Lab"
    }
}

resource "aws_route" "labVPC_internet_access" {
  route_table_id         = "${aws_route_table.labVPC_route_table.id}"
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = "${aws_internet_gateway.labVPC_GW.id}"
}

resource "aws_route_table_association" "labVPC_association" {
    subnet_id      = "${aws_subnet.labVPC_Subnet.id}"
    route_table_id = "${aws_route_table.labVPC_route_table.id}"
}
