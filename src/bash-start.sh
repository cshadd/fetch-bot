#!/bin/bash

java -jar ~/bin/fetch-bot-0.1.0.jar
/usr/bin/chromium-browser --incognito --start-maximized --kiosk http://localhost/FetchBot/face.html
unclutter
xset s off
xset s noblank
xset -dpms