import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginPage {
    public  static  void main(String[] args){

        // Todo: Step 1 -> Create an instance of webdriver using chrome driver
        WebDriver driver = new ChromeDriver();
       //Let's visit a website and test out whatever we've learned so far
        String TEST_URL = "https://rahulshettyacademy.com/locatorspractice/";
        driver.get(TEST_URL);


        // Todo : Closing the browser windows after 3 seconds.
        try {
            Thread.sleep(3000);
            driver.close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
