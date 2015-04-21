# Introduction #

JIntellitype is available as a Maven artifact in the Central Repository.


# Details #

Maven Central Repo: [http://search.maven.org](http://search.maven.org/#browse|-1527101277)

Use this in your pom.xml:


```
<dependency>
    <groupId>com.melloware</groupId>
    <artifactId>jintellitype</artifactId>
    <version>1.3.9</version>
</dependency>
```


**Note:** The DLL does not get downloaded by Maven as Maven has no notion of Native DLL's.  Make sure JIntellitype.dll or JIntellitype64.dll are on the path.