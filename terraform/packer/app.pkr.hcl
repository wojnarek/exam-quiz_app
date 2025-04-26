packer {
  required_plugins {
    amazon = {
      source  = "hashicorp/amazon"
      version = ">= 1.0.0"
    }
  }
}

variable "aws_region" {
  type    = string
  default = "eu-central-1"
}

source "amazon-ebs" "ubuntu" {
  region                  = var.aws_region
  instance_type           = "t2.micro"
  ami_name                = "custom-ami-app-{{timestamp}}"
  ssh_username            = "ubuntu"
  associate_public_ip_address = true
  source_ami_filter {
    filters = {
      name                = "ubuntu/images/hvm-ssd/ubuntu-focal-20.04-amd64-server-*"
      virtualization-type = "hvm"
      root-device-type    = "ebs"
    }
    owners      = ["099720109477"]
    most_recent = true
  }


  launch_block_device_mappings {
    device_name = "/dev/xvda"  
    volume_size = 20            
    volume_type = "gp2"
    delete_on_termination = true
  }

}

build {
  sources = ["source.amazon-ebs.ubuntu"]

  provisioner "shell" {
    inline = [
    "sudo apt-get update -y"
    "sudo apt-get install docker.io -y"
    "sudo systemctl enable docker"
    "sudo systemctl start docker"
    "sudo usermod -aG docker ubuntu"
    "sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose"
    "sudo chmod +x /usr/local/bin/docker-compose"
    "mkdir /home/ubuntu/app"
    "cd /home/ubuntu/app"
    "curl -L https://raw.githubusercontent.com/wojnarek/exam-quiz_app/refs/heads/main/docker-compose.yml -o docker-compose.yml"
    "sudo docker-compose up -d"

    ]
  }
}
