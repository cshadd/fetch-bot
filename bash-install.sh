#!/bin/bash

./bash-uninstall.sh
cd ./git-submodules/ArduinoJson
git submodule init
git submodule update
cd ..
sudo mkdir -p /usr/share/arduino/libraries/
sudo cp -R ./ArduinoJson/ /usr/share/arduino/libraries/
sudo cp ./ArduinoJson.h /usr/share/arduino/libraries/ArduinoJson.h
cd ..
cd ./src/arduino-slave-processor/
make upload
rm -f -r ./build-uno
cd ..
cd ..
mkdir -p ~/bin
mvn clean package
cp -r ./target/libs ~/bin
cp ./target/fetch-bot-v0.14.0.jar ~/bin/fetch-bot-v0.14.0.jar
cp ./src/bash-start.sh ~/bin/StartFetchBot.sh
sudo chmod 764 ~/bin/StartFetchBot.sh
sudo cp -R ./src/www /var/www/html/FetchBot
sudo chown -R $USER:www-data /var/www/html/FetchBot
sudo chmod -R 776 /var/www/html/FetchBot
