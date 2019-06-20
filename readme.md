[![Maven](https://img.shields.io/maven-central/v/com.melloware/jintellitype.svg)](http://repo1.maven.org/maven2/com/melloware/jintellitype/)
[![Javadocs](http://javadoc.io/badge/com.melloware/jintellitype.svg)](http://javadoc.io/doc/com.melloware/jintellitype)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

# JIntellitype

[![JIntellitype Logo](https://melloware.com/wp-content/uploads/2012/03/jintellitype-150x150.png)](https://melloware.com)

## Contents

1. Overview
2. Features
3. Installation
4. Quick Usage
5. Acknowledgements
6. Feedback

---

## 1.Overview

   JIntellitype is an API for interacting with Microsoft Intellitype keyboard commands as well as registering for Global Hotkeys in your application.
   The API is a Java JNI library that uses a DLL to do all the communication with Windows.
   This library ONLY works on Windows.

   Have you ever wanted to have CTRL+SHIFT+G maximize your Swing application on the desktop even if that application did not have focus?
   Now you can by registering a Windows Hotkey combination your application will be alerted when the combination you select is pressed anywhere in Windows.

   Have you ever wanted to react to those special Play, Pause, Stop keys on some Microsoft and Logitech keyboards?
   Even some laptops now have those special keys built in and if you want your application to "listen" for them, now you
   can!

## 2.Features
* Can register global hotkey combinations in Windows.

* Application is notified even if it does not have focus.

* Can react to those Play, Pause, Stop, Next, Forward Media keys like Winamp.

* Very little code, easy to use API.

* Examples included in JIntellitypeTester.java.

## 3.Installation

FOR USERS:

 The 32/64 bit dlls get extracted automatically.
 
 Currently does not work on Java 11, probably starting with Java 9. Latest recommended version is Java 8.

FOR DEVELOPERS:

* To build you need [Apache Maven](https://maven.apache.org/) or higher installed from Apache.
  Just run "mvn package" from the directory where the pom.xml is located to build the project.

* To build the C++ code you need [Bloodshed C++ IDE](http://www.bloodshed.net/devcpp.html).
  When you load the .dev project included do not forget to edit Project->Options and under Directories Tab change the Includes directory to contain.

      both:

        /java5/include
        /java5/include/win32

      Where "java5" is the location of your Java JDK.


## 4.Quick Usage

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

See demo at  [JIntellitype Demo Application](https://github.com/melloware/jintellitype/blob/master/src/main/java/com/melloware/jintellitype/JIntellitypeDemo.java)  for more info..

## 5.Acknowledgements

   JIntellitype is distributed with a small number of libraries on which it depends.

   Those libraries are:

 * None currently

## 6.Feedback
   Your feedback on JIntellitype (hopefully constructive) is always welcome.

 Please visit http://www.melloware.com/ for links to browse and join mailing lists, file bugs and submit feature requests.
