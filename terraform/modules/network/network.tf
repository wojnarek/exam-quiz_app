resource "aws_vpc" "app_vpc" {
  cidr_block       = var.vpc_cidr_block
  instance_tenancy = "default"


  tags = {
    Name = var.vpc_name
    Env = "dev"
  }
}

data "aws_availability_zones" "available" {
  state = "available"
}


resource "aws_subnet" "app_private_subnet" {
  count                   = length(var.private_subnet_cidr_block)
  vpc_id                  = aws_vpc.app_vpc.id
  cidr_block              = var.private_subnet_cidr_block[count.index]
  availability_zone = data.aws_availability_zones.available.names[count.index]
  map_public_ip_on_launch = false


  tags = {
    Name = "private_subnet-${count.index}"
    Env = "dev"
  }

}

resource "aws_subnet" "app_public_subnet" {
  count                   = length(var.public_subnet_cidr_block)
  vpc_id                  = aws_vpc.app_vpc.id
  cidr_block              = var.public_subnet_cidr_block[count.index]
  availability_zone = data.aws_availability_zones.available.names[count.index]
  map_public_ip_on_launch = false


  tags = {
    Name = "private_subnet-${count.index}"
    Env = "dev"
  }

}

resource "aws_internet_gateway" "igw_app" {
  vpc_id = aws_vpc.app_vpc.id

  tags = {
    Name = "igw_app"
    Env = "dev"
  }

}


resource "aws_route_table" "public_rt_app" {
  vpc_id = aws_vpc.app_vpc.id

  tags = {
    Name = "${var.vpc_name}_public_rt"
    Env = "dev"
  }
}

resource "aws_route" "public_route" {
  route_table_id         = aws_route_table.public_rt_app.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.igw_app.id
}

resource "aws_route_table_association" "public_assoc" {
  count = length(aws_subnet.app_public_subnet)
  subnet_id      = aws_subnet.app_public_subnet[count.index].id
  route_table_id = aws_route_table.public_rt_app.id

}