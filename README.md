[![Release](https://img.shields.io/github/release/cshadd/fetch-bot/all.svg)](https://github.com/cshadd/fetch-bot/releases)
[![License](https://img.shields.io/github/license/cshadd/fetch-bot.svg)](LICENSE)

# Fetch Bot
This is a repository of an AI based robot that uses image processing and data structures to transverse an area.

## Getting Started
This repository may be used for your own robot.

### Items
* Setup:
    - x1 Computer
    - x1 Ethernet Cable
    - x1 Tool Kit
* Recommended:
    - x1 Raspberry Pi 3 Model B
    - x1 JBtek Raspberry Pi Micro USB Cable with ON / OFF Switch - Easy Start / Reboot
    - x1 A-Male to B-Male USB
    - x1 Micro USB
    - x1 KUNCAN DC 5V to DC 12V Converter Step Up Voltage Converter 5ft Am to DC 5.5 x 2.1mm
    - x1 Logitech HD Webcam C525, Portable HD 720p Video Calling with Autofocus
    - x1 Male to Male HDMI Cable
    - x1 Elecrow RPA05010R HDMI 5-Inch 800x480 TFT LCD Display with Touch Screen Monitor for Raspberry Pi B+/2B/3B
    - x2 Adafruit Stepper Motor NEMA17 12V 350mA
    - x2 Mounting Bracket for Nema 17 Stepper Motor (Geared Stepper) Hobby CNC/3D Printer
    - ~~x3~~ x2 58mm Omni Wheel With Hubs For DIY Arduino Robot Competition Car Supporting LEGO
    - x1 Arduino Uno
    - x1 Keywish 5PCS HC-SR04 Ultrasonic Module Kit Distance Sensor for Arduino UNO, Mega, R3, Mega 2560, Nano, Duemilanove, Raspberry Pi 3
    - x1 Non-Soldering Breadboard
    - x1 Non-Soldering Jump Wires
    - x1 Internet Router
    - x1 Robot Chassis
    - x1 15000mAh Dual USB Output and 2A Input Battery Pack
* Optional:
    - x1 Raspberry Pi Case Kit
    - ~~x1 2x20-pin Extra Tall Female Stacking Header 0.1"~~

### Assembly
Great care is needed to assemble this robot.
We specifically chose these parts for our robot but it is up to you to decide which ones you will use.
```
1. Assemble the chassis with the...
```

### Why Raspberry Pi and Arduino Uno?
Let's face it. In our day and age of the 21st century, we want lightweight mobile systems for robots. The Pi and Uno is perfect for it.
We cannot stress enough about the power of the Pi and Uno. The features on the Pi contains preinstalled software such as ``git``.
It is easy to setup and use as it is virtually a computer itself.
We built this robot specifically on the Pi and Uno but made it as modular as possible to accomodate other systems.
If you want to use a full tower as a robot brain, go ahead. But the Pi and Uno is much simpler.
Proceeding forward, we will be specifically talking about the Pi and Uno unless otherwise. The system and Bash command lines will rely on the Pi.

### Prerequisites
* Recommended (Raspberry Pi):
    - Raspbian OS Stretch or higher
    - Chromium Browser
    - Java SE Development Kit 8.0 or higher
    - Java SE Runtime Environment 8.0 or higher
    - Ino 0.3 or higher
    - Apache Maven 3.5.2 or higher
    - Apache Server 2.4.10 or higher
    - PHP 5.0 or higher
    - Packaged Apps (Bash):
        - Unclutter
        - ~~xscreensaver~~
        - arduino-core
        - arduino-mk
* Recommended (Computer):
    - Text Editor/IDE
* Optional (Raspberry Pi):
    - VNC Server
* Optional (Computer):
    - Visual Studio 2017 or higher
    - VNC Viewer

### Installing
Clone/fork this repository and save it. Then use ``./bash-install.sh`` to install to your system (Bash).
``~/bin/StartFetchBot.sh`` is the launcher.
``/var/www/html/FetchBot/`` is the webserver.

### Further Considerations
You may want to:
* Configure remote SSH.
* Configure serial access.
* Check the serial ports when connecting the Arduino to the Raspberry Pi and change them if necessary in accordance with this program.
* Check the wiring when connecting the sensors and motors to the Arduino and change them if necessary in accordance with this program.

## Deployment

### Recommended Deployment
``~/bin/StartFetchBot.sh`` (Bash) is the launcher that you use to deploy the application.
To run or stop it you will need to use the web server.
``/var/www/html/FetchBot/`` is the web server that you can access at http://localhost/FetchBot. You may need to disable the cache.

### Visual Studio Solution (Optional)
You may use Visual Studio 2017 or higher as an editor. ``fetch-bot.sln`` is the solution file.

## Contributing
See [here](CONTRIBUTING.md).

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
See [here](LICENSE).

## Development Standards
See [here](/docs/DevelopmentStandards.pdf).

## Acknowledgements
* [Raspberry Pi](https://www.raspberrypi.org/)
* [Arduino](https://www.arduino.cc/)
* [Ed's Blog](http://pblog.ebaker.me.uk/)
* [Ino](http://inotool.org/)
* [Adafruit](https://www.adafruit.com/)
* [Oracle](https://www.oracle.com/)
* [The Pi4J Project](http://pi4j.com/)
* [Apache](https://www.apache.org/)
* [JitPack](https://www.jitpack.io/)
* [ZenHub](https://www.zenhub.com/)
* [W3Schools](https://www.w3schools.com/)
* [The PHP Group](https://php.net/)
* [Microsoft](https://www.microsoft.com/)

[![<3 Raspberry Pi](https://www.raspberrypi.org/app/uploads/2017/06/Powered-by-Raspberry-Pi-Logo_Outline-Colour-Screen-500x153.png)](https://www.raspberrypi.org/)
