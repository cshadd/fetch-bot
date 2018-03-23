#!/bin/bash

./bash-uninstall.sh
cd ./src/arduino-slave-processor/
make upload
sudo rm -f -r ./build-uno
cd ..
cd ..
mkdir -p ~/bin
mvn package
cp -r ./target/libs ~/bin
cp ./target/fetch-bot-v0.11.0.jar ~/bin/fetch-bot-v0.11.0.jar
cp ./src/bash-start.sh ~/bin/StartFetchBot.sh
cp ./src/bash-stop.sh ~/bin/StopFetchBot.sh
sudo chmod 764 ~/bin/StartFetchBot.sh
sudo chmod 764 ~/bin/StopFetchBot.sh
sudo cp -R ./src/www /var/www/html/FetchBot
sudo chown -R $USER:www-data /var/www/html/FetchBot
sudo chmod -R 776 /var/www/html/FetchBot
