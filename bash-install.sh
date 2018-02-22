#!/bin/bash

mkdir -p ~/bin
mvn package
cp ./target/fetch-bot-v0.4.0.jar ~/bin/fetch-bot-v0.4.0.jar
cp ./src/bash-start.sh ~/bin/StartFetchBot.sh
cp ./src/bash-stop.sh ~/bin/StopFetchBot.sh
sudo chmod 764 ~/bin/StartFetchBot.sh
sudo chmod 764 ~/bin/StopFetchBot.sh
sudo cp -r ./src/www /var/www/html/FetchBot
sudo chown $USER:www-data /var/www/html/FetchBot
sudo chown $USER:www-data /var/www/html/FetchBot/comms
