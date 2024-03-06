package SchedulePro;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class AddItemsToCart {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();

        try{
            Properties prop = new Properties();
            File file = new File("testdata.properties");
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);

            String TEST_URL  = prop.getProperty("addItemsToCartUrl");
            driver.get(TEST_URL);

            String[] itemsNeeded= {"Cucumber","Brocolli","Beetroot"};
            Thread.sleep(3000);
            addItems(driver,itemsNeeded);

            System.out.println("All tests passed");
            // Close the browser windows after completion of tests.
            Thread.sleep(4000);
            driver.quit();
        }
        catch (Exception E){
            E.printStackTrace();
        }
    }
    public static  void addItems(WebDriver driver,String[] itemsNeeded)

    {
        int j=0;
        List<WebElement> products=driver.findElements(By.cssSelector("h4.product-name"));
        for(int i=0;i<products.size();i++)
        {
            String[] name = products.get(i).getText().split("-");

            String formattedName=name[0].trim();
            List itemsNeededList = Arrays.asList(itemsNeeded);
            if(itemsNeededList.contains(formattedName))
            {
                j++;
                driver.findElements(By.xpath("//div[@class='product-action']/button")).get(i).click();
                if(j==itemsNeeded.length)
                {
                    break;
                }

            }

        }

    }
}
