<div align="center">
    <a href="https://github.com/melloware/jintellitype" alt="JIntellitype">
        <img src="https://github.com/melloware/jintellitype/blob/master/src/test/resources/jintellitype.png?raw=true" />
    </a>
 
# JIntellitype
</div>

[![Maven](https://img.shields.io/maven-central/v/com.melloware/jintellitype.svg?style=for-the-badge)](https://repo1.maven.org/maven2/com/melloware/jintellitype/)
[![Javadocs](http://javadoc.io/badge/com.melloware/jintellitype.svg?style=for-the-badge)](https://javadoc.io/doc/com.melloware/jintellitype)
[![License](http://img.shields.io/:license-apache-blue.svg?style=for-the-badge&color=yellow)](http://www.apache.org/licenses/LICENSE-2.0.html)

JIntellitype is a Java JNI library that provides an interface to Microsoft Intellitype keyboard commands and global hotkey registration in Windows applications. The library enables Java applications to respond to special media keys (Play, Pause, Stop, etc.) and register global hotkey combinations.

> [!IMPORTANT]
> This library is Windows-only as it uses Windows-specific API calls.

**If you like this project, please consider supporting me ❤️**

[![GitHub Sponsor](https://img.shields.io/badge/GitHub-FFDD00?style=for-the-badge&logo=github&logoColor=black)](https://github.com/sponsors/melloware)
[![PayPal](https://img.shields.io/badge/PayPal-00457C?style=for-the-badge&logo=paypal&logoColor=white)](https://www.paypal.me/mellowareinc)

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Examples](#examples)
- [Building from Source](#building-from-source)
- [Contributing](#contributing)
- [License](#license)

## Features

- Register global hotkey combinations that work system-wide in Windows
- Receive notifications for special media keys (Play, Pause, Stop, Next, Previous)
- Support for modifier keys (CTRL, ALT, SHIFT, WIN) in hotkey combinations
- Automatic DLL management (32/64 bit)
- Simple and intuitive API
- Thread-safe implementation
- Comprehensive error handling

## Installation

### Maven

```xml
<dependency>
    <groupId>com.melloware</groupId>
    <artifactId>jintellitype</artifactId>
    <version>[VERSION]</version>
</dependency>
```

### Gradle

```groovy
implementation 'com.melloware:jintellitype:[VERSION]'
```

The native DLLs are automatically extracted and loaded at runtime. No manual installation steps are required.

## Usage

### Basic Setup

```java
// Initialize JIntellitype
JIntellitype.getInstance().addHotKeyListener(identifier -> {
    System.out.println("Hotkey pressed: " + identifier);
});

JIntellitype.getInstance().addIntellitypeListener(command -> {
    System.out.println("Intellitype command: " + command);
});

// Register hotkeys
JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_WIN, 'A');  // Windows + A
JIntellitype.getInstance().registerHotKey(2, JIntellitype.MOD_ALT + JIntellitype.MOD_SHIFT, 'B');  // Alt + Shift + B

// Clean up on exit
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    JIntellitype.getInstance().cleanUp();
}));
```

### Supported Modifier Keys

- `JIntellitype.MOD_ALT` - Alt key
- `JIntellitype.MOD_CONTROL` - Control key
- `JIntellitype.MOD_SHIFT` - Shift key
- `JIntellitype.MOD_WIN` - Windows key

### Supported Media Commands

- `APPCOMMAND_MEDIA_PLAY_PAUSE` - Play/Pause
- `APPCOMMAND_MEDIA_STOP` - Stop
- `APPCOMMAND_MEDIA_NEXTTRACK` - Next Track
- `APPCOMMAND_MEDIA_PREVIOUSTRACK` - Previous Track
- `APPCOMMAND_VOLUME_MUTE` - Mute
- `APPCOMMAND_VOLUME_UP` - Volume Up
- `APPCOMMAND_VOLUME_DOWN` - Volume Down

## Examples

See the [JIntellitypeDemo](https://github.com/melloware/jintellitype/blob/master/src/main/java/com/melloware/jintellitype/JIntellitypeDemo.java) class for a complete working example.

## Building from Source

### Prerequisites

- Java JDK 8 or higher
- Apache Maven
- Visual Studio C++ (for native code compilation)

### Build Steps

1. Clone the repository
```bash
git clone https://github.com/melloware/jintellitype.git
```

2. Build with Maven
```bash
mvn clean package
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](http://www.apache.org/licenses/LICENSE-2.0.html) file for details.


## Support

- Visit [Melloware](https://www.melloware.com/) for additional information and support
- Report issues on [GitHub Issues](https://github.com/melloware/jintellitype/issues)
