#!/bin/bash

./bash-uninstall.sh
cd ./src/arduino-slave-processor/FetchBot
make upload
rm -f -r ./build-uno
cd ../../..
mkdir -p ~/bin
mvn clean package
cp -r ./target/libs ~/bin
cp ./target/fetch-bot-v1.0.0.jar ~/bin/fetch-bot-v1.0.0-.jar
cp ./src/bash-start.sh ~/bin/StartFetchBot.sh
sudo cp -R ./src/www /var/www/html/FetchBot
sudo chmod 764 ~/bin/StartFetchBot.sh
sudo chown -R $USER:www-data /var/www/html/FetchBot
sudo chmod -R 776 /var/www/html/FetchBot
