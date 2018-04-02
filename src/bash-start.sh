#!/bin/bash

export DISPLAY=:0
xset -display :0 s off
xset -display :0 -dpms
xset -display :0 s noblank
sudo pkill java
sudo java -jar ~/bin/fetch-bot-v0.12.0.jar "v0.12.0" &
/usr/bin/chromium-browser --incognito --start-maximized --kiosk http://localhost/FetchBot/face.html
unclutter
