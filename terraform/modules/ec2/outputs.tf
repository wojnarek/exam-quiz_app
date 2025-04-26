output "instance_ip" {
    value = aws_instance.app_vm.public_ip
}

output "instace_id" {

    value = aws_instance.app_vm.id
  
}