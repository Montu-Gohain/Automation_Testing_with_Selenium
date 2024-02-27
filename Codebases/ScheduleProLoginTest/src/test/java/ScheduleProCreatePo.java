import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import javax.swing.plaf.basic.BasicTreeUI;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

public class ScheduleProCreatePo {
    public static void main(String[] args) {
        // Todo : Setting up ExtentReport package to give us test results.

        ExtentReports extent = new ExtentReports();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        // Convert the timestamp to string
        String timestamp = sdf.format(new Date());
        // Create dynamic filename with the timestamp.
        String filename = "target/Reports/ExtentReport-" + timestamp + ".html";
        ExtentSparkReporter spark = new ExtentSparkReporter(filename);
        spark.config().setTheme(Theme.DARK);
        extent.attachReporter(spark);

        // Todo : Initializing the Webdriver in our case ChromeDriver.

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1500,968");
        WebDriver driver = new ChromeDriver(options);
        // Due to longer loading times  let's add an implicit wait of 3 seconds
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(180));
        try{
            Properties prop = new Properties();
            File file = new File("testdata.properties");
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);

            // Step 1 : Login to the home page.
            Login(driver,  prop);
            // Step 2 : Go the PO Management Page.
            Goto_PO_Management(driver);
            // Step 3 : Fill the form to create a new PO
            // CreatePO(driver);
            EditPO(driver);

//            Thread.sleep(3000);
//            driver.quit();
        }

        catch (Exception E){
            E.printStackTrace();
            System.out.println("Something went wrong, Test failed");
        }
    }
    public  static void Login(WebDriver driver,Properties prop){
        try{
            String TEST_URL =  prop.getProperty("scheduleProUrl");
            String userEmail = prop.getProperty("userEmail");
            String userPassword = prop.getProperty("userPass");

            driver.get(TEST_URL);
            // Step 1 : Login
            driver.findElement(By.xpath("//input[@placeholder=\"Enter Email\"]")).sendKeys(userEmail);
            Thread.sleep(2000);
            driver.findElement(By.xpath("//input[@name=\"password\"]")).sendKeys(userPassword);
            Thread.sleep(2000);
            // Finally click on Login button.
            driver.findElement(By.className("btn-submit")).click();
            System.out.println("We've Reached the home page");
        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Something went wrong, Test failed");
        }
    }
    public static void Goto_PO_Management(WebDriver driver){
        try{
            Thread.sleep(8000);
            driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/div/div/ul/li[4]/a/div")).click();
            Thread.sleep(8000);

        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Something went wrong, Test failed.");
        }
    }
    public static void CreatePO(WebDriver driver){
        try{
            // Click on the Create PO button
            driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div[1]/div[1]/div/div[1]/button")).click();
            System.out.println("Here we can add a new PO");
            // Wait for 2 seconds.
            Thread.sleep(2000);

            String po_number = "165244";
            String vender_name = "Montu Testing";
            String commodity = "Dairy";
            String buyer_name = "Mr Smith";
            String items_count = "12000";
            String cases_count = "1500";
            String total_weight = "8280";
            String pallet_count = "900";
            // Todo : Let's fill up the form.
            // 1. PO number
            driver.findElement(By.xpath("//input[@name='PoNumber']")).sendKeys(po_number);

            // 2. Vender name
            driver.findElement(By.xpath("//input[@name='VendorName']")).sendKeys(vender_name);

            // 3. Buyer name
            driver.findElement(By.xpath("//input[@name='BuyerName']")).sendKeys(buyer_name);

            // 4. Commodity
            driver.findElement(By.xpath("//input[@name='Commodity']")).sendKeys(commodity);

            // 5. Items
            driver.findElement(By.xpath("//input[@name='Quantity']")).sendKeys(items_count);

            // 6. Cases
            driver.findElement(By.xpath("//input[@name='Cases']")).sendKeys(cases_count);

            // 7. Weight in lbs
            driver.findElement(By.xpath("//input[@name='Weight']")).sendKeys(total_weight);

            // 8. Pallet Count
            driver.findElement(By.xpath("//input[@name='Pallets']")).sendKeys(pallet_count);

            // 9. Select Dock Type
            // Clicking on the select option
//            driver.findElement(By.xpath("//select[@name='ProduceType']")).click();
//            Thread.sleep(1600);
            WebElement dock_type_selector = driver.findElement(By.xpath("//select[@name='ProduceType']"));
            Select dock_type_options = new Select(dock_type_selector);

            // Let's select Dairy.
            dock_type_options.selectByValue("D1576416-B198-4CA0-95CA-4CC824832043");
            System.out.println("Selected option : "+ dock_type_options.getFirstSelectedOption().getText());

            Thread.sleep(2000);
            // 10. Select Due Date.
            driver.findElement(By.xpath("//input[@name='custom-date-picker']")).sendKeys("10-03-2024");

            Thread.sleep(2000);
            // Finally we click on the Save button.
            driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[2]/div/div/div[2]/button[2]")).click();
            System.out.println("Clicked the save button.");
        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Something went wrong, Test failed.");
        }
    }
    public static void EditPO(WebDriver driver){
        try{
            // Clicking on the edit button (Represented by a pen)
            driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div[2]/div/div[1]/table/tbody/tr[1]/td[9]/div/img[1]")).click();

            // Update the value for commodity
            String commodity = "Cow Milk";
            // 4. Commodity
            WebElement  commodityInputField = driver.findElement(By.xpath("//input[@name='Commodity']"));
            //Since the clear method is not working, we'll use ctrl + a + del
            commodityInputField.sendKeys(Keys.CONTROL, "a");
            commodityInputField.sendKeys(Keys.DELETE);


            driver.findElement(By.xpath("//input[@name='Commodity']")).sendKeys(commodity);
            Thread.sleep(2000);
            // Finally we click on the Save button.
            driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[2]/div/div/div[2]/button[2]")).click();
            System.out.println("Clicked the save button.");
        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Something went wrong,Test failed");
        }
    }
}
