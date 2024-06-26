

variable "aws_region" {
  type = string
  default = "us-west-2"
}


variable "source_ami" {
  type = string
  default = "ami-0f1a5f5ada0e7da53"
}

variable "ami_users" {
  type = list(string)
  default = ["814613584038","160774145825", "187570859166"]
}

source "amazon-ebs" "Amazon_IMA" {
  //  access_key = "${env("AWS_ACCESS_KEY_ID")}"
  //   secret_key = "${env("AWS_SECRET_ACCESS_KEY")}"
  ami_name      = "CSYE6225webapp${formatdate("YYYY_MM_DD_hh_mm_ss",timestamp())}"
  ami_users     =  var.ami_users
  instance_type = "t2.micro"
  region        = var.aws_region
  ssh_username  = "ec2-user"
  source_ami    = var.source_ami
//ssh into the instance, using the df -h, you will see the /dev/xvda1
  launch_block_device_mappings {
    // launch_block_device_mappings 挂载点
    device_name           = "/dev/xvda"

    volume_size           = 50
    volume_type           = "gp2"
    delete_on_termination = true
  }

  tags = {
    Name = "My_IMA-{{timestamp}}"
  }
}

build  {
  sources =["source.amazon-ebs.Amazon_IMA"]


  provisioner "file" {

    source   = "../target/HomeWork1-0.0.1-SNAPSHOT.jar"
    destination = "/tmp/"
  }

  provisioner "shell" {

    inline  = [
      //drwx-xr-x; root root;
      "sudo mkdir -p /var/log/myapp",

      "sudo cp /tmp/HomeWork1-0.0.1-SNAPSHOT.jar /opt/HomeWork1-0.0.1-SNAPSHOT.jar"
    ]
  }
 # "sudo chmod -v 755 /var/log/myapp/",

  provisioner "file" {
    source      = "./cloudWatchAgentConfig.json"
    destination = "/tmp/"
  }


  provisioner "shell" {

    inline  = [
      "sudo cp /tmp/cloudWatchAgentConfig.json /opt/cloudWatchAgentConfig.json"
    ]
  }

  provisioner "shell" {

    script  = "setup.sh"
  }

#  provisioner "shell" {
#
#    script  = "mariadbSecureInstallation.sh"
#  }
#
#  provisioner "shell" {
#
#    script  = "javaWebApp.sh"
#  }

  provisioner "file" {

    source      = "JavaService.service"
    destination = "/tmp/"
  }

  provisioner "shell" {

    inline  = [
      "sudo cp /tmp/JavaService.service /etc/systemd/system/JavaService.service"
    ]
  }



  provisioner "shell" {

    script  = "JavaSystemD.sh"
  }





}




