variable "vpc_name" {
    type = string
    description = "Name of vpc"
    default = "vpc-app"
  
}

variable "subnet_name" {
    type = string
    description = "Name of subnet"
    default = "subnet-app"
  
}

variable "region" {
  type = string
  description = "Region"
  default = "eu-central-1"
  }

variable "vpc_cidr_block" {
    type = string
    description = "CIDR block for VPC"
    default = "10.0.0.0/16"
  }

variable "private_subnet_cidr_block" {
    type = list(string)
    description = "CIDR block for private subnet"
    default = ["10.0.3.0/24", "10.0.4.0/24"]
    
  }


variable "public_subnet_cidr_block" {
    type = list(string)
    description = "CIDR block for public subnet"
    default = ["10.0.1.0/24", "10.0.2.0/24"]
    
  }