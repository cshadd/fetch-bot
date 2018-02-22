#!/bin/bash

mkdir -p ~/bin
mvn package
cp ./target/fetch-bot-v0.4.2.jar ~/bin/fetch-bot-v0.4.2.jar
cp ./src/bash-start.sh ~/bin/StartFetchBot.sh
cp ./src/bash-stop.sh ~/bin/StopFetchBot.sh
sudo chmod 764 ~/bin/StartFetchBot.sh
sudo chmod 764 ~/bin/StopFetchBot.sh
sudo cp -R ./src/www /var/www/html/FetchBot
sudo chown -R $USER:www-data /var/www/html/FetchBot
sudo chmod -R 776 /var/www/html/FetchBot
