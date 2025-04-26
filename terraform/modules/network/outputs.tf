output "vpc_id" {
    description = "VPC id"
    value = aws_vpc.app_vpc.id
}

output "vm_subnets_ids" {
    description = "private subnet id"
    value = aws_subnet.app_private_subnet[*].id
}

output "alb_subnets_ids" {
    description = "public subnet id"
    value = aws_subnet.app_public_subnet[*].id
}