terraform {
  required_providers {
    aws = {
        source = "hashicorp/aws"
        version = "~>5.0"
    }
  }
}


provider "aws" {
    region = "eu-central-1"
    profile = "default"
}


module "vm_instance" {
  source = "./modules/ec2"

  vpc_id = module.network.vpc_id
  subnet_id = module.network.vm_subnets_ids[0]
  security_group_lb_id = [module.load_balancer.lb_security_grup_id]
  ami_image = "[your ami image]"
}

module "network" {
    source = "./modules/network"
  
}

module "load_balancer" {
  source = "./modules/load_balancer"
  
  vpc_id = module.network.vpc_id
  subnet_ids = module.network.alb_subnets_ids
  target_port = 8080
  target_instance_id = module.vm_instance.instace_id

}