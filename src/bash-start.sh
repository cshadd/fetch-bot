#!/bin/bash

vncserver :1
export DISPLAY=:0
xset -display :0 s off
xset -display :0 -dpms
xset -display :0 s noblank
sudo pkill java
sudo pkill unclutter
export DISPLAY=:1
java -jar fetch-bot-v2.0.0-alpha.jar $1 &
export DISPLAY=:0
unclutter -display :0 &
/usr/bin/chromium-browser --incognito --start-maximized --kiosk http://localhost/FetchBot/face.html
