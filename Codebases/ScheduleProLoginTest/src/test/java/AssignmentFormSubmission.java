import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

public class AssignmentFormSubmission {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        try{
            Properties prop = new Properties();
            File file = new File("testdata.properties");
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);

            String TEST_URL = prop.getProperty("assignment2Url");

            // Step 0 : Open the browser and hit the URL
            driver.get(TEST_URL);

            // Todo : Fill up the form step by step

            // Filling up the name input field
            driver.findElement(By.xpath("/html/body/app-root/form-comp/div/form/div[1]/input")).sendKeys("Montu Gohain");

            Thread.sleep(2000);
            // Filling up the email input field
            driver.findElement(By.xpath("/html/body/app-root/form-comp/div/form/div[2]/input")).sendKeys("leomontugohain@gmail.com");
            // Filling up the password input field
            driver.findElement(By.id("exampleInputPassword1")).sendKeys("password");

            Thread.sleep(2000);
            // Check the checkbox.
            driver.findElement(By.id("exampleCheck1")).click();

            // Selecting the gender
            WebElement staticSelector = driver.findElement(By.id("exampleFormControlSelect1"));
            Select dropdown = new Select(staticSelector);

            dropdown.selectByIndex(0);
            // Let's print out what we've selected from the select element.
//            System.out.println(dropdown.getFirstSelectedOption());

            Thread.sleep(2000);
            // Now let's select the employment status
            driver.findElement(By.id("inlineRadio2")).click();


            // Click on the Calender Form
            driver.findElement(By.xpath("/html/body/app-root/form-comp/div/form/div[7]/input")).click();

            // Wait for at least 3 seconds.
            Thread.sleep(3000);

            driver.findElement(By.xpath("/html/body/app-root/form-comp/div/form/div[7]/input")).sendKeys("14/02/2024");

            // Finally Click on the Submit button.
            driver.findElement(By.xpath("/html/body/app-root/form-comp/div/form/input")).click();
            Thread.sleep(2000); // waiting for again 2 seconds.

            // Last Task : Capture the final text and print it out.

            System.out.println(driver.findElement(By.xpath("/html/body/app-root/form-comp/div/div[2]/div")).getText());

            // close the browser after completion of all tests
            Thread.sleep(3000);
            driver.quit();
        }
        catch (Exception E){
            E.printStackTrace();
        }
    }
}
