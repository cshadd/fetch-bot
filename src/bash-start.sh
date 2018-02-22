#!/bin/bash

LOCATION="localhost"

export DISPLAY=:0.0
java -jar ~/bin/fetch-bot-v0.4.2.jar &
/usr/bin/chromium-browser --incognito --start-maximized --kiosk http://$LOCATION/FetchBot/face.html
unclutter
