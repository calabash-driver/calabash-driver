## Calabash Driver
The *calabash-driver* offers a java api to write end-to-end tests for Android using the [calabash-android project](http://github.com/calabash/calabash-android).
For scaling the [Selenium Grid](http://code.google.com/p/selenium/wiki/Grid2) can be used for scaling and parallel testing.

For details about how to get started please have a look at the [wiki](http://github.com/calabash-driver/calabash-driver/wiki/)

# Architecture

The driver is build primarily to integrated it into the *Selenium Grid* to test Android mobile apps in parallel on emulators or devices.

![The Architecture of Calabash-Driver][https://docs.google.com/drawings/pub?id=1Xs6yEaqXnPXa5wgrYGulpkRQBqdPCc9OltF4op3oY48&w=952&h=583]

The mobile devices specifics like the locale, the SDK version or the application under test (aut) are described by capabilities. 
This capabilities are used to register the driver instance into the *Selenium Grid*. When starting the *calabash-driver client*, the desired capabilities must be specified. Later on they are used during creation of the test session to find the right *calabash-driver server* instance.
If the session is initialized, the default json *calabash-android* commands like '{"arguments":["Accept"],"command":"press"}' are send through the different components to execute in the end the *calabash-android* command/action on the device.

*Calabash-Driver* is using a [forked version](https://github.com/calabash-driver/calabash-android) of *calabash-android* that contains additions like L10n support, which basically enables you to interact with UI elements based on the l10n key in the resource bundle of the app.