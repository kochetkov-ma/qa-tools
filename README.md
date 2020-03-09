QA Tools Java Library
=================================
![build](https://github.com/kochetkov-ma/qa-tools/workflows/Build%20/%20Make%20Release%20/%20Publish%20to%20OSS/badge.svg?branch=master)
![jdk11](https://camo.githubusercontent.com/f3886a668d85acf93f6fec0beadcbb40a5446014/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6a646b2d31312d7265642e737667)
![gradle](https://camo.githubusercontent.com/f7b6b0146f2ee4c36d3da9fa18d709301d91f811/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f746f6f6c2d677261646c652d626c75652e737667)
![junit](https://camo.githubusercontent.com/d2ba89c41121d7c6223c1ad926380235cf95ef82/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6a756e69742d706c6174666f726d2d627269676874677265656e2e737667)



## qa-tools
Different simple tools for auto testing:
- unit-testing
- web ui
- mobile ui
- api
- other

## Getting
```groovy
implementation 'io.rest-assured:rest-assured:0.1.1'
```

## Possibilities 
#### Selenium element highlighting
`WebDriverEventListener` to highlight element with which one be interacting now (active element).
Use static constructor.
```java
        final WebDriver sourceDriver = new ChromeDriver()
        final EventFiringWebDriver driver = new EventFiringWebDriver(sourceDriver);
        driver.register(HighlighterListener.newSingleThreadHighlighterListener());
```
Also you can register this event handler in `com.codeborne.selenide` via `WebDriverRunner`
#### Publishing html page for web testing and unit testing on localhost
If you want check you Page Object or you want provide simple html page on localhost in your unit (integration) tests
you can use this simplest and fastest single html page http server from JDK.
```java
        LocalSimpleHtmlServer server = LocalSimpleHtmlServer.of(8080, "/");
        server.publish("<!DOCTYPE html><html><body>test</body></html>");
        server.close()      
``` 
or in Junit as test rule
```java
        @Rule
        public LocalSimpleHtmlServer.TestHtmlServer server = LocalSimpleHtmlServer.of(8080, "/")
            .asTestRule("<!DOCTYPE html><html><body>test</body></html>");      
``` 
#### WithValue interface
If you have some class with any value field especially String type just implement `WithValue` interface.  
And you will get very convenient method `ru.iopump.qa.support.api.WithValue.hasValue` (see javadoc).  
This smart small method can equals every types of values especially String type with ignore case, 
also it execute smart cast objectValue or expectedValue to String if any of them is String type.    

### Utilities
#### ClassUtil
Provide several JDK's methods with a null-safe behavior.
#### EnumUtil
- create your enum implemented `WithValue<String>`
```java
    @AllArgsConstructor
    @Getter
    private enum EnumTmp implements WithValue<String> {
        ONE("one_value"), TWO("two_value"), THREE("three_value");
        private final String value;
    }
```
- find enum constants by String value and don't worry about letter's case or get clear exception with very useful message
 ```java
EnumTmp result = EnumUtil.getByValue(EnumTmp.class, "ONE_VALUE")
assert result == EnumTmp.ONE
```
- you may change generic type to any other and use it
- also you may find enum by its name
```java
EnumTmp result = EnumUtil.getByName(EnumTmp.class, "two")
assert result == EnumTmp.TWO
```
#### FileUtil

#### ReflectionUtil

#### ResourceUtil

#### Str

#### StreamUtil

#### VarUtil
Merge environment and system variables.  
```java
        /* get */
        Optional<String> envOrSysProp = VarUtil.get("OS");
        assertThat(envOrSysProp).isNotEmpty();
        /* getOrDefault */
        String envOrSysPropOrDefault = VarUtil.getOrDefault("NOT_EXISTS", "DEFAULT_VALUE") // can be null
        assertThat(VarUtil.getOfDefault("NOT_EXISTS", "DEFAULT_VALUE")).isEqualTo("DEFAULT_VALUE");
```
