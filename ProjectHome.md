[JIntellitype](http://melloware.com/products/jintellitype/index.html) is a Java API for interacting with Microsoft Intellitype commands as well as registering for Global Hotkeys in your Java application. The API is a Java JNI library that uses a C++ DLL to do all the communication with Windows. NOTE: This library ONLY works on Windows. If you are looking for a Linux version please see [JxGrabKey](http://sourceforge.net/projects/jxgrabkey/) project on Sourceforge.  If you are looking for a Mac OSX version see [OSSupport](https://bitbucket.org/agynamix/ossupport-connector) on GitHub.

Have you ever wanted to have CTRL+SHIFT+G maximize your Swing application on the desktop? Even if that application did not have focus? Well, now you can! By registering a Windows Hotkey combination, your application will be alerted when the combination you select is pressed anywhere in Windows. Windows has the API call RegisterHotKey for registering a global hotkey combination so that your app receives that message no matter what else you are doing or even if your application has focus. This is a commonly requested feature in Java that has now been implemented by [JIntellitype](http://melloware.com/products/jintellitype/index.html). Check out the Quick Start Guide below for an example on how to use [JIntellitype](http://melloware.com/products/jintellitype/index.html).

Have you ever wanted your Java application to react to those special Play, Pause, Stop keys on some newer keyboards like Winamp and Windows Media Player do? Ever wonder how they do it? If you want your application to "listen" for those special keys, now you can with [JIntellitype](http://melloware.com/products/jintellitype/index.html)! Just register an IntellitypeListener and you will be notified when those messages are received. To read more about these special commands see the MSDN Documentation about the Intellitype commands. Check out the Quick Start Guide for an example on how to use [JIntellitype](http://melloware.com/products/jintellitype/index.html).

![http://jintellitype.googlecode.com/svn/wiki/jintellitype-banner.png](http://jintellitype.googlecode.com/svn/wiki/jintellitype-banner.png)

```
// Initialize JIntellitype
...
JIntellitype.getInstance();
...
// OPTIONAL: check to see if an instance of this application is already
// running, use the name of the window title of this JFrame for checking
if (JIntellitype.checkInstanceAlreadyRunning("MyApp")) {
   LOG.error("An instance of this application is already running");
   System.exit(1);
}

// Assign global hotkeys to Windows+A and ALT+SHIFT+B
JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_WIN, (int)'A');
JIntellitype.getInstance().registerHotKey(2, JIntellitype.MOD_ALT + JIntellitype.MOD_SHIFT, (int)'B');

//or you can use the Swing constants instead
JIntellitype.getInstance().registerSwingHotKey(3, Event.CTRL_MASK + Event.SHIFT_MASK, (int)'C');
        
// To unregister them just call unregisterHotKey with the unique identifier
JIntellitype.getInstance().unregisterHotKey(1);
JIntellitype.getInstance().unregisterHotKey(2);
JIntellitype.getInstance().unregisterHotKey(3);

//assign this class to be a HotKeyListener
JIntellitype.getInstance().addHotKeyListener(this);
        
// listen for hotkey
public void onHotKey(int aIdentifier) {
    if (aIdentifier == 1)
        System.out.println("WINDOWS+A hotkey pressed");
    }
}

//assign this class to be a IntellitypeListener
JIntellitype.getInstance().addIntellitypeListener(this);
        
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

// Termination, make sure to call before exiting
...
JIntellitype.getInstance().cleanUp();
System.exit(0);


```