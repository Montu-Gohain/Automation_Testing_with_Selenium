package SchedulePro;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class AutoSuggestiveDropdown {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();

        try{
            Properties prop = new Properties();
            File file = new File("testdata.properties");
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);


            String TEST_URL = prop.getProperty("dropdownTestingUrl");

            // Step 0 : Open the browser and hit the URL

            driver.get(TEST_URL);

            // Step 1 : Let's put some text in the input field
            driver.findElement(By.id("autosuggest")).sendKeys("Ind");


            Thread.sleep(3000);
            List<WebElement> options =  driver.findElements(By.cssSelector("li[class='ui-menu-item'] a"));

            // Let's try to access the options
            for(WebElement option : options){
                if(option.getText().equalsIgnoreCase("India")){
                    option.click();
                    break;
                }
            }

            // Let's check on some checkboxes using assertions.

            // Here we're learning about Assert.assertFalse and Assert.assertTrue

            Assert.assertFalse(driver.findElement(By.xpath("/html/body/form/div[4]/div[2]/div/div[5]/div[2]/div[2]/div[2]/div[3]/div/div[12]/div[1]/input")).isSelected());
            Thread.sleep(4000);

            driver.findElement(By.xpath("/html/body/form/div[4]/div[2]/div/div[5]/div[2]/div[2]/div[2]/div[3]/div/div[12]/div[1]/input")).click();

            Assert.assertTrue(driver.findElement(By.xpath("/html/body/form/div[4]/div[2]/div/div[5]/div[2]/div[2]/div[2]/div[3]/div/div[12]/div[1]/input")).isSelected());
            // It should check the checkbox.

            // Let's find out how many checkboxes are available in this webpage.
            // Using Assert.assertEquals(actual ,expected)
            
            Assert.assertEquals(driver.findElements(By.cssSelector("input[type='checkbox']")).size(), 6);

            // Close the browser windows after completions of all tests.
            Thread.sleep(4000);
            driver.quit();
        }
        catch (Exception E){
            E.printStackTrace();
        }
    }
}
