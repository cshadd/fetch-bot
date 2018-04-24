#!/bin/bash

export DISPLAY=:0
xset -display :0 s off
xset -display :0 -dpms
xset -display :0 s noblank
sudo pkill java
cd ~/bin/
sudo java -jar fetch-bot-v0.15.1.jar "v0.15.1" &
/usr/bin/chromium-browser --incognito --start-maximized --kiosk http://localhost/FetchBot/face.html
unclutter
