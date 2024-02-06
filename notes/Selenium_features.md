# These are the features of Selenium

1. Selenium is an open source Automation testing tool
2. It is exclusively for Web Based applications
3. Selenium Supports multiple browsers -
   Chrome, Firefox, Safari
4. Selenium works with multiple platforms.e.g. Windows, AppleOS and Linux
5. Selenium can be coded in multiple languages -
   Java, C# , Python , javascript , php, Ruby, etc

---

# Selenium Webdriver Architecture Simplified :

![Selenium Architecture](https://github-production-user-asset-6210df.s3.amazonaws.com/76866991/301434838-30d21065-a89c-44e6-bdbd-f8448746b994.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAVCODYLSA53PQK4ZA%2F20240201%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20240201T082533Z&X-Amz-Expires=300&X-Amz-Signature=1a7cba99553896dffebd80fcfd4e471e85c4e8c26eb7a57e71926f153d640706&X-Amz-SignedHeaders=host&actor_id=76866991&key_id=0&repo_id=747507811)

1. After you trigger the test, complete Selenium Code (client) Which we have written will be converted to Json Format.

2. Generated Json is sent to Browser Driver (Server) through http Protocol. Note : Each browser contains a separate browser driver.

3. Browser drivers communicate with its respective broswer and executes the commands by interpreting Json which it received on the browser.

4. Browser Driver receives responses back from the browser adn it sends json response back to client.

---

> Date : 05/02/2024

1. Learned how to get elements by using Selenium Selectors.These are :
   Id, className, name,tagname, Xpath, cssSelector

   ```java
     Webdriver driver = new ChromeDriver();

     // By using the id locator we can target html element
     driver.findElement(By.id("inputUsername")).sendKeys("rahul");

     // By using name in the input fields we can target html element
     driver.findElement(By.id("inputUsername")).sendKeys("rahul");

     // By using classname in the input fields we can target html element
     driver.findElement(By.className("signInBtn")).click();

   ```

2. Implicit wait for asynchronous operations

   ```java
   // To perform any asynchronous operation or let's say we need to wait for some time , for something to happen, in this case
   Webdriver driver = new ChromeDriver();
   driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
   ```

3. After finding the targeted elemlent , how to emulate keyboard entry along with mouse click. i.e. Use of .click() and .sendKeys() method.

   ```java

    Webdriver driver = new ChromeDriver();
    driver.findElement(By.name("inputPassword")).sendKeys("hello123");

   // To emulate mouse click action

   driver.findElement(By.className("signInBtn")).click();

   ```

> Date : 06 / 02 / 2024

1. Learned how to visit an anchor tag with only the link text

```java

   Webdriver driver = new ChromeDriver();
   driver.findElement(By.linkText("Forgot your password ?")).click();

```

2. How to access something using x-path

> // htmltagname[@attribute="value"]

```html
<!-- For an input element inside a form -->
<input type="text" placeholder="Name" />
//input[@palceholder="Name"]
<!-- This above is the x-path -->
```

3. What if we don't have any unique identifier, in this case we can use indexing

```java
 // Let's say we have multiple elements with input type="text"

 Webdriver driver = new ChromeDriver();
 driver.findElement(By.xpath("//input[@type='text'][2]")).clear();

```
