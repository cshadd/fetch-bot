[![Open Source Love](https://badges.frapsoft.com/os/mit/mit.png?v=102)](https://github.com/ellerbrock/open-source-badge/)

# Fetch Bot
This is a repository of an AI based robot that uses image processing and data structures to transverse an area.

## Getting Started
This repository may be used for your own robot.

### Parts
* Recommended:
    - Raspberry Pi 3 Model B ()
    - JBtek Raspberry Pi Micro USB Cable with ON / OFF Switch - Easy Start / Reboot
    - A-Male to B-Male USB
    - GPIO Headers
    - Logitech HD Webcam C525, Portable HD 720p Video Calling with Autofocus
    - Male to Male HDMI Cable
    - "Monitor"
    - Adafruit DC & Stepper Motor HAT for Raspberry Pi - Mini Kit
    - KUNCAN DC 5V to DC 12V Converter Step Up Voltage Converter 5ft Am to DC 5.5 x 2.1mm
    - "12V to Port Connector"
    - 12V wires
    - "Stepper Motors"
    - "Omni Wheels"
    - "Omni Wheel Headers"
    - Arduino Uno
    - Keywish 5PCS HC-SR04 Ultrasonic Module Kit Distance Sensor for Arduino UNO Mega R3 Mega2560 Nano Duemilanove Nano Raspberry Pi 3 Robot
    - Breadboard
    - Jump Wires
    - Internet Router
    - Robot Chassis
* Optional:
    - Raspberry Pi Case
    - Raspberry Pi Heat Sink
    - _V 2 Port USB Battery Pack

### Assembly
Great care is needed to assemble this robot.
We specifically choosen these parts for our robot but it is up to you to decide which ones you will use.

### Why Raspberry Pi?
Lets face it. In our day and age of the 21st century, we want lightweight mobile systems for robots. The Raspberry Pi is perfect for it.
We cannot stress enough about the power of the Pi, and the features it contains that come preinstalled such as ``git``.
It is easy to setup and use as virtually its own computer.
We built this robot specifically on the Pi but made it as modular as possible to accomodate other systems.
If you want to use a full tower as a robot brain, go ahead. But the Raspberry Pi is much simpler.
Proceeding forward, we will be specifically talking about the Raspberry Pi unless otherwise. The system and Bash command lines will rely on the Raspberry Pi.

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

## Acknowledgements
* [Oracle](https://www.oracle.com/)
* [Raspberry Pi](https://www.raspberrypi.org/)
* [Apache](https://www.apache.org/)
* [Adafryut](https://www.adafruit.com/)
* [The Pi4J Project](http://pi4j.com/)
* [Pi4J Samples by OlivierLD](https://github.com/OlivierLD/raspberry-pi4j-samples)

[![<3 Raspberry Pi](https://www.raspberrypi.org/app/uploads/2017/06/Powered-by-Raspberry-Pi-Logo_Outline-Colour-Screen-500x153.png)](https://www.raspberrypi.org/)