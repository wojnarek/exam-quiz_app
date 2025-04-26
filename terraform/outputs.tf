output "vm_public_ip" {
    value = module.vm_instance.instance_ip

}

output "alb_dns_name" {
    value = module.load_balancer.lb_dns_name 
}