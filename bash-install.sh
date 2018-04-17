#!/bin/bash

./bash-uninstall.sh
sudo mkdir -p /usr/share/arduino/libraries/
wget https://github.com/bblanchon/ArduinoJson/releases/download/v5.13.1/ArduinoJson-v5.13.1.zip
unzip ArduinoJson-v5.13.1.zip
rm ArduinoJson-v5.13.1.zip
sudo cp -r ./ArduinoJson /usr/share/arduino/libraries/
rm -f -r ./ArduinoJson
cd ./src/arduino-slave-processor/FetchBot
make upload
rm -f -r ./build-uno
cd ..
cd ..
cd ..
mkdir -p ~/bin
mvn clean package
cp -r ./target/libs ~/bin
cp ./target/fetch-bot-v0.15.0.jar ~/bin/fetch-bot-v0.15.0.jar
cp ./src/bash-start.sh ~/bin/StartFetchBot.sh
sudo chmod 764 ~/bin/StartFetchBot.sh
sudo mkdir -p /var/www/html/
sudo cp -R ./src/www /var/www/html/FetchBot
sudo chown -R $USER:www-data /var/www/html/FetchBot
sudo chmod -R 776 /var/www/html/FetchBot
