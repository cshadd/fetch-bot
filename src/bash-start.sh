#!/bin/bash

export DISPLAY=:0.0
java -jar ~/bin/fetch-bot-0.1.0.jar
/usr/bin/chromium-browser --incognito --start-maximized --kiosk http://localhost/FetchBot/face.html
unclutter
