[![Maven](https://img.shields.io/maven-central/v/com.melloware/jintellitype.svg)](https://repo1.maven.org/maven2/com/melloware/jintellitype/)
[![Javadocs](http://javadoc.io/badge/com.melloware/jintellitype.svg)](https://javadoc.io/doc/com.melloware/jintellitype)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

# JIntellitype

[![JIntellitype Logo](https://github.com/melloware/jintellitype/blob/master/src/test/resources/jintellitype.png?raw=true)](https://melloware.com)

## Contents

1. Overview
2. Features
3. Installation
4. Quick Usage
5. Acknowledgements
6. Restrictions

---

## 1. Overview

   JIntellitype is an API for interacting with Microsoft Intellitype keyboard commands as well as registering for Global Hotkeys in your application.
   The API is a Java JNI library that uses a DLL to do all the communication with Windows, and uses a dynamic library to do the communication with macOS.
   This library works on Windows and macOS.

   Have you ever wanted to have CTRL+SHIFT+G maximize your Swing application on the desktop even if that application did not have focus?
   Now you can: by registering a Windows or macOS Hotkey combination your application will be alerted when the combination you select is pressed anywhere.

   Have you ever wanted to react to those special Play, Pause, Stop keys on some Microsoft and Logitech keyboards?
   Even some laptops now have those special keys built in and if you want your application to "listen" for them, now you
   can!

## 2. Features
* Can register global hotkey combinations in Windows and macOS.

* Application is notified even if it does not have focus.

* Can react to the Play, Pause, Stop, Next, Forward Media keys like Winamp in Windows.

* Very little code, easy to use API.

* Examples included in `JIntellitypeDemo.java`.

## 3. Installation

FOR USERS:

 The 32/64 bit dlls get extracted automatically.

FOR DEVELOPERS:

* To build you need [Apache Maven](https://maven.apache.org/) or higher installed from Apache.
  Just run "mvn package" from the directory where the pom.xml is located to build the project.

* To build the C++ code you need Visual Studio C++.

* To build the Swift code you need Xcode.


## 4. Quick Usage

```java
       // Create JIntellitype
       ...
        JIntellitype.getInstance().addHotKeyListener(new HotKeyListener() {...);
        JIntellitype.getInstance().addIntellitypeListener(new IntellitypeListener() {...);
       ...

        // Assign global hotkeys to Windows+A and ALT+SHIFT+B
        JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_WIN, (int)'A');
        JIntellitype.getInstance().registerHotKey(2, JIntellitype.MOD_ALT + JIntellitype.MOD_SHIFT, (int)'B');

        // listen for hotkey
        public void onHotKey(int aIdentifier) {
           if (aIdentifier == 1)
             System.out.println("WINDOWS+A hotkey pressed");
           }
        }

        // listen for intellitype play/pause command
        public void onIntellitype(int aCommand) {
            switch (aCommand) {
                case JIntellitype.APPCOMMAND_MEDIA_PLAY_PAUSE:
                    System.out.println("Play/Pause message received " + Integer.toString(aCommand));
                break;
                default:
                    System.out.println("Undefined INTELLITYPE message caught " + Integer.toString(aCommand));
                break;
            }
        }

       // Termination
       ...
       JIntellitype.getInstance().cleanUp();
       System.exit(0);
```

Example with [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/):

```kotlin
key(true) {
  DisposableEffect(Unit) {
    val listener = HotkeyListener { println("Hot key pressed") }
    JIntellitype.getInstance().addHotKeyListener(listener)
    JIntellitype.getInstance().registerHotKey(1, "COMMAND+PERIOD")

    onDispose {
        JIntellitype.getInstance().removeHotKeyListener(listener)
    }
  }
}
```

See demo at [JIntellitype Demo Application](https://github.com/snowbagoly/jintellitype-multiplatform/blob/master/src/main/java/com/melloware/jintellitype/JIntellitypeDemo.java)  for more info.

## 5. Acknowledgements

   JIntellitype was originally implemented by Melloware, please [check out their other projects](https://www.melloware.com/projects/).


## 6. Restrictions
 Note that on Mac the given key always considers the [physical place of the key](https://boredzo.org/blog/archives/2007-05-22/virtual-key-codes). This means that if you use a keyboard layout other than the ANSI-standard US keyboard, then you might have to provide different keys (e.g. if you would like to press `-` as a hotkey on a German layout, you have to provide `SLASH` as the hotkey). This also means that the Mac version works with a restricted keyset, you can find all available mappings in `JIntellitype.swift`.