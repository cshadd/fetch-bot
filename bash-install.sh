#!/bin/bash

mkdir -p ~/bin
mvn package
cp ./target/fetch-bot-0.1.0.jar ~/bin/fetch-bot-0.1.0.jar
cp ./src/bash-start.sh ~/bin/StartFetchBot.sh
sudo chmod 764 ~/bin/StartFetchBot.sh
sudo cp -r ./src/www /var/www/html/FetchBot
sudo chown $USER:www-data /var/www/html/FetchBot
sudo chown $USER:www-data /var/www/html/FetchBot/comms
