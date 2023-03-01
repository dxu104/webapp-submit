#!/bin/bash


  
# Install Java 17 JDK
sudo amazon-linux-extras enable corretto8
sudo yum install -y java-17-amazon-corretto-devel




# Update the system
sudo yum update -y
#sudo apt-get update 这个是 specific to Debian and Ubuntu-based Linux distributions.
     sleep 5

sudo yum install -y tcl
sudo yum install -y expect

sudo yum install -y coreutils


sudo yum install -y mariadb-server
#even teacher said we should close, but yao said 开着吧，后面测试RDS 的数据库可能用的到
#sudo systemctl start mariadb
#
#sudo systemctl enable mariadb









  









