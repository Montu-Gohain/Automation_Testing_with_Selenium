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
