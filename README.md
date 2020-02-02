## qa-tools
Different simple tools for auto testing:
- unit-testing
- web ui
- mobile ui
- api
- other

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

...