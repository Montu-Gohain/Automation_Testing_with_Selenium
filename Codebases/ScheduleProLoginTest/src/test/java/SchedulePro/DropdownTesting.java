package SchedulePro;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

public class DropdownTesting {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        try{
            // Access the properties.testdata file
            Properties prop = new Properties();
            File file = new File("testdata.properties");
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);

            String Test_URL = prop.getProperty("dropdownTestingUrl");

            // Step 1 : Open the webpage.
            driver.get(Test_URL);

            WebElement staticDropdown = driver.findElement(By.id("ctl00_mainContent_DropDownListCurrency"));
            Select dropdown = new Select(staticDropdown);
            dropdown.selectByIndex(3);
            System.out.println(dropdown.getFirstSelectedOption().getText());

            // Now let's select another option from the select element.
            Thread.sleep(3000);
            dropdown.selectByVisibleText("AED");
            System.out.println(dropdown.getFirstSelectedOption().getText());

            // Last but not the least , let's select an element by its value.
            Thread.sleep(3000);
            dropdown.selectByValue("INR");
            System.out.println(dropdown.getFirstSelectedOption().getText());


            // Closing the browser after done testing
            Thread.sleep(3000);
            driver.quit();
        }
        catch (Exception E){
            E.printStackTrace();
        }

    }
}
