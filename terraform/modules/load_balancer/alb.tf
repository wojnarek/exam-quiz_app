resource "aws_lb" "app_lb" {

  name               = "app-lb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.lb_sg.id]
  subnets            = var.subnet_ids

  tags = {
    Name = "app-lb"
    Env = "dev"
  }

}

resource "aws_security_group" "lb_sg" {
  name        = "lb-sg"
  description = "Allow http to load balancer"
  vpc_id      = var.vpc_id


  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_lb_target_group" "lb_tg" {
  name     = "app-tg"
  port     = var.target_port
  protocol = "HTTP"
  vpc_id   = var.vpc_id


  health_check {
    path                = "/"
    protocol            = "HTTP"
    matcher             = "200-499"
    interval            = 30
    timeout             = 5
    healthy_threshold   = 2
    unhealthy_threshold = 2
  }

}

resource "aws_lb_target_group_attachment" "lb_attachment" {
  target_group_arn = aws_lb_target_group.lb_tg.arn
  target_id        = var.target_instance_id
  port             = var.target_port
}

resource "aws_lb_listener" "lb_listner" {
  load_balancer_arn = aws_lb.app_lb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.lb_tg.arn
  }

}