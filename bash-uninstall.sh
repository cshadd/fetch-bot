#!/bin/bash

sudo rm -f -r /var/www/html/FetchBot
find . -name 'fetch-bot*' -exec rm -f -r {} \;
rm -f -r ~/bin/StartFetchBot.sh
rm -f -r ~/bin/StopFetchBot.sh
