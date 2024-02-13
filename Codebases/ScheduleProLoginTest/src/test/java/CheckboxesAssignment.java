import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

public class CheckboxesAssignment {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();

        try{
            // Access the Test Url from testdata.properties file
            Properties prop = new Properties();
            File file = new File("testdata.properties");
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);


            String TEST_URL = prop.getProperty("assignmentUrl");

            // Step 0 : Open the browser and hit the URL
            driver.get(TEST_URL);


            // Task 1 : Check the first checkbox among these 3 checkboxes.
            driver.findElement(By.xpath("//*[@id=\"checkBoxOption1\"]")).click();

            // Task 2 : Verify that the checkbox is selected.
            Assert.assertTrue(driver.findElement(By.xpath("//*[@id=\"checkBoxOption1\"]")).isSelected());
            System.out.println("First checkbox is checked");

            Thread.sleep(3000);
            // Task 3 : Uncheck that checkbox.
            driver.findElement(By.xpath("//*[@id=\"checkBoxOption1\"]")).click();
            System.out.println("First checkbox is unchecked");

            // Task 4 : Verify that the checkbox is not selected.
            Assert.assertFalse(driver.findElement(By.xpath("//*[@id=\"checkBoxOption1\"]")).isSelected());

            // Task 5 : Get the count of number of checkboxes present in the page.
            int total_checkboxes = driver.findElements(By.cssSelector("input[type='checkbox']")).size();
            System.out.println("There are total " + total_checkboxes + " checkboxes in the page.");

            // Close the browser after completion of all tests.
            Thread.sleep(4000);
            driver.quit();
        }
        catch (Exception E){
            E.printStackTrace();
        }

    }
}
