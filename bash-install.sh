#!/bin/bash

mkdir -p ~/bin
sudo cp -r ./src/www /var/www/html/FetchBot
mvn package
cp ./target/fetch-bot-0.1.0.jar ~/bin/fetch-bot-0.1.0.jar
cp ./src/bash-start.sh ~/bin/StartFetchBot.sh
