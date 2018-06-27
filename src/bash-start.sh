#!/bin/bash

export DISPLAY=:0
xset -display :0 s off
xset -display :0 -dpms
xset -display :0 s noblank
sudo pkill java
sudo pkill unclutter
java -jar fetch-bot-v2.0.0-alpha.3.jar $1 &
unclutter -display :0 &
/usr/bin/chromium-browser --incognito --start-maximized --kiosk http://localhost/FetchBot/face.html
