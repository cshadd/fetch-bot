#!/bin/bash

export DISPLAY=:0
xset -display :0 s off
xset -display :0 -dpms
xset -display :0 s noblank
sudo pkill unclutter
unclutter -display :0 &
sudo pkill java
/usr/bin/chromium-browser --incognito --start-minimized --kiosk http://localhost:8885/ &
/usr/bin/chromium-browser --incognito --start-maximized --kiosk http://localhost/FetchBot/face.html &
java -jar fetch-bot-v2.0.0-alpha.1.jar $1
sudo pkill chromium