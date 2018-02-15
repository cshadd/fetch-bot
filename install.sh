#!/bin/bash

mkdir -p ~/java-bin/libraries/
mkdir -p ./bin
cd ./bin
jar cvfm fetch-bot-0.1.0.jar ../MANIFEST.txt io
mv ./fetch-bot-0.1.0.jar ~/java-bin/libraries/fetch-bot-0.1.0.jar
mv ./www /var/www/html/FetchBot