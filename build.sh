#!/bin/bash

rm -r ./bin
mkdir -p ./bin
javac -classpath ~/java-bin/libraries/* -d ./bin -sourcepath ./test:./src -verbose -Xlint:unchecked ./src/io/github/cshadd/fetch_bot/FetchBot.java
cp -r www ./bin