[![Open Source Love](https://badges.frapsoft.com/os/mit/mit.svg?v=102)](https://github.com/ellerbrock/open-source-badge/)

# Fetch Bot
This is a repository of an AI based robot that uses image processing and data structures to transverse an area.

## Getting Started
This repository may be used for your own robot.

### Parts
* Recommended:
* Optional:

### Prerequisites
* Recommended:
    - Raspbian OS
    - Bash
    - Chromium Browser
    - Unclutter
    - x11-xserver-utils
    - Java SE Development Kit 8.0 or higher
    - Java SE Runtime Environment 8.0 or higher
    - Apache Maven 3.5.2 or higher
    - Apache Server 2.4.10 or higher
    - PHP 5.0 or higher
    - Text Editor/IDE
* Optional:
    - Visual Studio 2017 or higher

### Installing
Clone/fork this repository and save it. Then use ``./bash-install.sh`` to install to your system (Bash).
``~/bin/fetch-bot-x.x.x.jar`` is the compiled JAR.
``~/bin/StartFetchBot.sh`` is the launcher.
``~/bin/StopFetchBot.sh`` is the terminator.
``/var/www/html/FetchBot/`` is the webserver.

## Deployment

### Recommended Deployment
``~/bin/StartFetchBot.sh`` (Bash) is the launcher that you use to deploy the application and run it. ``~/bin/StopFetchBot.sh`` (Bash) is how you will terminate the application.
``/var/www/html/FetchBot/`` is the server that you can access at http://localhost/FetchBot. You may need to disable the cache. We suggest you deploy this on a Raspberry PI with Apache server and a static/set ip address.

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
* [Apache](https://www.apache.org/)
* [The Pi4J Project](http://pi4j.com/)
