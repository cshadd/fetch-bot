[![Release](https://img.shields.io/github/release/cshadd/fetch-bot/all.svg)](https://github.com/cshadd/fetch-bot/releases)
[![License](https://img.shields.io/github/license/cshadd/fetch-bot.svg)](LICENSE)

# Fetch Bot
This is a repository of an AI based robot that uses image processing and data structures to transverse an area.

## Getting Started
This repository may be used for your own robot.

### Parts
* Recommended:
    - Raspberry Pi 3 Model B ()
    - JBtek Raspberry Pi Micro USB Cable with ON / OFF Switch - Easy Start / Reboot
    - A-Male to B-Male USB
    - KUNCAN DC 5V to DC 12V Converter Step Up Voltage Converter 5ft Am to DC 5.5 x 2.1mm
    - HitLights LED Lightstrip DC Jack Connection - Female to Screw Terminal, Four Pack
    - ~~12V wires~~
    - Logitech HD Webcam C525, Portable HD 720p Video Calling with Autofocus
    - Male to Male HDMI Cable
    - HDMI Monitor
    - ~~Adafruit DC & Stepper Motor HAT for Raspberry Pi - Mini Kit~~
    - Adafruit Stepper Motor NEMA17 12V 350mA
    - ~~48mm Omniwheels for LEGO NXT, Servo~~
    - 5mm Aluminum Mounting Hubs
    - Arduino Uno
    - Keywish 5PCS HC-SR04 Ultrasonic Module Kit Distance Sensor for Arduino UNO, Mega, R3, Mega 2560, Nano, Duemilanove, Raspberry Pi 3
    - Non-Soldering Breadboard
    - Non-Soldering Jump Wires
    - Internet Router
    - Robot Chassis
* Optional:
    - Raspberry Pi Case
    - Raspberry Pi Heat Sink
    - Ethernet Cable
    - 15000mAh Dual USB Output and 2A Input Battery Pack
    - 2x20-pin Extra Tall Female Stacking Header 0.1"

### Assembly
Great care is needed to assemble this robot.
We specifically chose these parts for our robot but it is up to you to decide which ones you will use.

### Why Raspberry Pi and Arduino Uno?
Let's face it. In our day and age of the 21st century, we want lightweight mobile systems for robots. The Pi and Uno is perfect for it.
We cannot stress enough about the power of the Pi and Uno. The features on the Pi contains preinstalled software such as ``git``.
It is easy to setup and use as it is virtually a computer itself.
We built this robot specifically on the Pi and Uno but made it as modular as possible to accomodate other systems.
If you want to use a full tower as a robot brain, go ahead. But the Pi and Uno is much simpler.
Proceeding forward, we will be specifically talking about the Pi and Uno unless otherwise. The system and Bash command lines will rely on the Pi.

### Prerequisites
* Recommended:
    - Raspbian OS Jessie or higher
    - Chromium Browser
    - Unclutter
    - x11-xserver-utils
    - Java SE Development Kit 8.0 or higher
    - Java SE Runtime Environment 8.0 or higher
    - Apache Maven 3.5.2 or higher
    - Apache Server 2.4.10 or higher
    - PHP 5.0 or higher
    - "Arduino Compiler"
    - Text Editor/IDE
* Optional:
    - Visual Studio 2017 or higher
    - VNC Server
    - VNC Viewer

### Installing
Clone/fork this repository and save it. Then use ``./bash-install.sh`` to install to your system (Bash).
``~/bin/fetch-bot-x.x.x.jar`` is the compiled JAR.
``~/bin/StartFetchBot.sh`` is the launcher.
``~/bin/StopFetchBot.sh`` is the terminator.
``/var/www/html/FetchBot/`` is the webserver.

## Deployment

### Recommended Deployment
``~/bin/StartFetchBot.sh`` (Bash) is the launcher that you use to deploy the application and run it. ``~/bin/StopFetchBot.sh`` (Bash) is how you will terminate the application.
``/var/www/html/FetchBot/`` is the server that you can access at http://localhost/FetchBot. You may need to disable the cache.

### Visual Studio Solution (Optional)
You may use Visual Studio 2017 or higher as an editor. ``fetch-bot.sln`` is the solution file.

## Contributing
Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning
We use [SemVer](http://semver.org/) for versioning. For the versions avalible, see the [tags on this repository](https://github.com/cshadd/fetch-bot/tags).

## Authors
* [Christian Shadd](https://github.com/cshadd)
* [Maria Verna Aquino](https://github.com/anrev09)
* [Thanh Vu](https://github.com/Vu-Thanh)
* [walterk4](https://github.com/walterk4)
* [gio-oro](https://github.com/gio-oro)

And the [contributers](https://github.com/cshadd/fetch-bot/graphs/contributors).

## License
This project is licensed under the MIT License. Please check the [LICENSE](LICENSE).

## Standards
Please check the [Standards.pdf](/docs/Standards.pdf)

## Acknowledgements
* [Raspberry Pi](https://www.raspberrypi.org/)
* [Arduino](https://www.arduino.cc/)
* [Adafruit](https://www.adafruit.com/)
* [Oracle](https://www.oracle.com/)
* [Apache](https://www.apache.org/)
* [The Pi4J Project](http://pi4j.com/)
* [ZenHub](https://www.zenhub.com/)

[![<3 Raspberry Pi](https://www.raspberrypi.org/app/uploads/2017/06/Powered-by-Raspberry-Pi-Logo_Outline-Colour-Screen-500x153.png)](https://www.raspberrypi.org/)
