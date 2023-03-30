#!/bin/bash


  
# Install Java 17 JDK
sudo amazon-linux-extras enable corretto8
sudo yum install -y java-17-amazon-corretto-devel




# Update the system
sudo yum update -y
#sudo apt-get update 这个是 specific to Debian and Ubuntu-based Linux distributions.
     sleep 5

#sudo yum install -y tcl
#sudo yum install -y expect
#如果您使用的是自定义的、精简的 Linux 发行版或者基于云的虚拟机，那么可能需要手动安装 coreutils
#sudo yum install -y coreutils


sudo yum install -y mariadb




#for assignment_8 log
#this kind of automatically installing is not a best practice, we should choose region to provide effect.
#sudo yum install -y amazon-cloudwatch-agent
#the following step of installing cloudwatch is best practice.
#sudo yum -y install wget
#us-west-2 we can customize the region.
wget https://s3.us-west-2.amazonaws.com/amazoncloudwatch-agent-us-west-2/amazon_linux/amd64/latest/amazon-cloudwatch-agent.rpm
sudo rpm -U ./amazon-cloudwatch-agent.rpm

sudo systemctl enable amazon-cloudwatch-agent

sudo systemctl start amazon-cloudwatch-agent



sudo yum install -y mariadb

#even teacher said we should close, but yao said 开着吧，后面测试RDS 的数据库可能用的到
#sudo systemctl start mariadb
#
#sudo systemctl enable mariadb









  









