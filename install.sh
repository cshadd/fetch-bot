#!/bin/bash

mkdir -p ~/bin
cp -r ./src/www /var/www/html/FetchBot
mvn package
cp ./target/fetch-bot-0.0.0.jar ~/bin/fetch-bot-0.0.0.jar
cp ./src/start.sh ~/bin/StartFetchBot.sh
