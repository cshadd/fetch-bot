#!/bin/bash

export DISPLAY=:0.0
sudo pkill java
sudo java -jar ~/bin/fetch-bot-v0.12.0.jar "v0.12.0" &
/usr/bin/chromium-browser --incognito --start-maximized --kiosk http://localhost/FetchBot/face.html
unclutter
