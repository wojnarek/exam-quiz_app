variable "sg_name" {
  type        = string
  description = "Name of security group"
  default = "app-sg"
}

variable "vm_name" {
  type        = string
  description = "Name of vm instance"
  default = "vm-app"
}

variable "vm_type" {
  type        = string
  description = "Type of vm instance"
  default     = "t2.micro"
}

variable "ami_image" {
  type        = string
  description = "AMI Image"
}

variable "region" {
  type    = string
  default = "eu-central-1"

}

variable "vpc_id" {
  type        = string
  description = "ID of VPC network"
}

variable "subnet_id" {
  type        = string
  description = "ID of subnet network"
}

variable "security_group_lb_id" {
  type = list(string)
 
}