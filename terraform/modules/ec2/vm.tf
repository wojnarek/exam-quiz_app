resource "aws_security_group" "app_sg" {
  name        = var.sg_name
  description = "Security group for ec2"
  vpc_id      = var.vpc_id


  ingress {
    description = "HTTP/HTTPS"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    security_groups = var.security_group_lb_id
  }

  ingress {
    description = "ssh"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }


  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "SG-app"
    Env = "dev"
  }

}

resource "aws_instance" "app_vm" {
    ami = var.ami_image
    instance_type = var.vm_type
    subnet_id = var.subnet_id
    vpc_security_group_ids = [aws_security_group.app_sg.id]
    associate_public_ip_address = false
    key_name = "my-keypair"

      root_block_device {
  volume_size = 20
  volume_type = "gp2"
}

    tags = {
      Name = var.vm_name
      Env = "dev"
    }

}