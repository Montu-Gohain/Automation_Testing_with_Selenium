
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SignInPage {


    public  static  void main(String[] args){

        // Todo: Step 1 -> Create an instance of webdriver using chrome driver
        WebDriver driver = new ChromeDriver();

       //Let's visit a website and test out whatever we've learned so far
        String TEST_URL = "https://rahulshettyacademy.com/locatorspractice/";
        driver.get(TEST_URL);

//        Username  : rahul
//        Password : rahulshettyacademy

        String valid_username = "rahul";
        String valid_password = "rahulshettyacademy";

        // Todo :  Let's fill up the form and try to login
        driver.findElement(By.xpath("/html/body/div/div/div/div/div/div/div[2]/form/input[1]")).sendKeys(valid_username);
        driver.findElement(By.xpath("/html/body/div/div/div/div/div/div/div[2]/form/input[2]")).sendKeys(valid_password);

        // Todo : Checking the checkboxes

        driver.findElement(By.xpath("/html/body/div/div/div/div/div/div/div[2]/form/div[1]/span[1]/input")).click();
        driver.findElement(By.xpath("/html/body/div/div/div/div/div/div/div[2]/form/div[1]/span[2]/input")).click();

        // Todo : Click on the SIGN IN Button

        driver.findElement(By.xpath("/html/body/div/div/div/div/div/div/div[2]/form/button")).click();

        try {
            Thread.sleep(4000);
            // Todo: Let's log out from this page.
            driver.findElement(By.xpath("/html/body/div/div/div/div/button")).click();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        closeBrowser(driver, 5);
    }
    public static void closeBrowser(WebDriver driver, Integer duration){
        // Todo : Closing the browser windows after 3 seconds.
        try {
            Thread.sleep(duration * 1000);
            driver.close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
