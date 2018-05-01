#!/bin/bash

export DISPLAY=:0
xset -display :0 s off
xset -display :0 -dpms
xset -display :0 s noblank
sudo pkill java
sudo java -jar fetch-bot-v1.0.0-alpha.jar &
/usr/bin/chromium-browser --incognito --start-maximized --kiosk http://localhost/FetchBot/face.html
unclutter
