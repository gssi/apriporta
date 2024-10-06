sudo apt install default-jdk
sudo apt install maven
sudo apt install mariadb-server

refer to this guide: https://www.digitalocean.com/community/tutorials/how-to-install-mariadb-on-ubuntu-20-04

We need 2 services running on the rpi. 

# Doorcontroller service

Compose one as /etc/systemd/system/doorcontroller.service

[Unit]
Description=Run the best script ever

[Service]
Type=forking
WorkingDirectory=/home/administrator/apriporta/doorcontroller
ExecStart=tmux new-session -d -s "myTempSession" sudo mvn exec:java

[Install]
WantedBy=multi-user.target

then enable it: sudo systemctl enable doorcontroller.service

# Shell script for executing webserver: /home/administrator/myservices.sh      

#!/bin/bash

cd /home/administrator/apriporta/acs

sudo ./mvnw

# ACS service
create service at: /lib/systemd/system/acs.service

[Unit]
 Description=My Sample Service
 After=multi-user.target

 [Service]
 Type=idle
 ExecStart=sh /home/administrator/myservices.sh

 [Install]
 WantedBy=multi-user.target

then enable it: sudo systemctl enable acs.service

create database ACS from commandline. 

change root password and jdbc connector URL.

# reboot the device
The ACS web system is accessible at HTTP://ip-addr:8080
