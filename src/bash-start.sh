#!/bin/bash

export DISPLAY=:0.0
java -jar ~/bin/fetch-bot-v0.11.0.jar "v0.11.0" &
/usr/bin/chromium-browser --incognito --start-maximized --kiosk http://localhost/FetchBot/face.html
unclutter
