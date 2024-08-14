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

# Shell script for executing webserver: /home/administrator/myservices.sh      

#!/bin/bash
cd /home/administrator/apriporta/acs
sudo ./mvnw

# reboot the device
