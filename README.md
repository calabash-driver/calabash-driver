[![Build Status](https://secure.travis-ci.org/calabash-driver/calabash-driver.png)](https://secure.travis-ci.org/calabash-driver/calabash-driver.png)
## Calabash Driver
The *calabash-driver* offers a java api to write end-to-end tests for Android using the [calabash-android project](http://github.com/calabash/calabash-android).
The [Selenium Grid](http://code.google.com/p/selenium/wiki/Grid2) can be used for scaling and parallel testing.

# How to get started

For details about how to get started please have a look at the [wiki](http://github.com/calabash-driver/calabash-driver/wiki/).

A practical example, that we have shown at the GDG DevFest in Zürich, you can find [here](https://github.com/DominikDary/gdg-devfest-zrh) and a getting started blog post [here](http://dary.de/2012/10/gdg-devfest-in-zurich/).

The session was recorded and is now available at YouTube:
<iframe width="560" height="315" src="http://www.youtube.com/embed/BExAKDslV9I" frameborder="0" allowfullscreen></iframe>

# Architecture

The mobile devices specifics like the locale, the SDK version or the application under test (aut) are described by capabilities. 
This capabilities are used to register the driver instance into the *Selenium Grid*. When starting the *calabash-driver client*, the desired capabilities must be specified. Later on they are used during creation of the test session to find the right *calabash-driver server* instance.
If the session is initialized, the default json *calabash-android* commands like:
 	
	'{"arguments":["Accept"],"command":"press"}'

are used to send them through the different components to execute in the end the *calabash-android* command/action on the device.

## Architectural overiew about the different components
![The Architecture of Calabash-Driver](https://docs.google.com/drawings/pub?id=1Xs6yEaqXnPXa5wgrYGulpkRQBqdPCc9OltF4op3oY48&w=952&h=583)

*Calabash-Driver* is using the [default version](https://github.com/calabash/calabash-android) of *calabash-android*.

## Dynamic View of Calabash-Driver's Architecture
![Dynamic View of Calabash-Driver's Architecture ](http://calabash-driver.github.com/calabashDriverDynamicView.png)

