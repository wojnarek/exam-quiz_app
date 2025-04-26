output "lb_dns_name" {
    value = aws_lb.app_lb.dns_name
}

output "lb_security_grup_id" {

    value = aws_security_group.lb_sg.id
  
}