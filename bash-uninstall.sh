#!/bin/bash

sudo pkill java
sudo rm -f -r /var/www/html/FetchBot
find ~/bin -name 'fetch-bot*.jar' -exec rm -f {} \;
rm -f ~/bin/FetchBot.log
rm -f -r ~/bin/libs/fetch-bot
rm -f ~/bin/StartFetchBot.sh
