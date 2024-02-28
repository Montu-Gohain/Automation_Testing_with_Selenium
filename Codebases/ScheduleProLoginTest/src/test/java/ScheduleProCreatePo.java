import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;
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
            ExtentTest test_reach_book_appointments = extent.createTest("Reach Book Appointment Page", "Login and Reach Book Appointments page.");
            Login(driver,  prop, test_reach_book_appointments);


            // Step 2 : Go the PO Management Page.
            ExtentTest test_reach_PO_Management = extent.createTest("Reach PO Management Page", "Click on the option in sidebar and reach PO Management Page.");
            Goto_PO_Management(driver, test_reach_PO_Management);

            // Step 3 : Fill the form to create a new PO
            ExtentTest test_create_new_PO = extent.createTest("Create a new PO", "Fill the input fields and create a new PO");
            CreatePO(driver,test_create_new_PO, prop);


             // Step 4 : Edit the latest PO.
            ExtentTest test_Edit_existing_PO = extent.createTest("Edit an existing PO", "By clicking on the pencil icon we can edit the existing value for that PO.");
            EditPO(driver, test_Edit_existing_PO);

            Thread.sleep(3000);
            driver.quit();
            extent.flush();
        }

        catch (Exception E){
            E.printStackTrace();
            System.out.println("Something went wrong, Test failed");
        }
    }
    public  static void Login(WebDriver driver,Properties prop,ExtentTest test_){
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
            Thread.sleep(8000);

            test_.info("Checking an HTML element that's present in the book appointments page.");

            WebElement target_element =  driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/div/div/ul/li[4]/a/div"));

            if(Objects.equals(target_element.getText(), "PO Management")){
                test_.pass("We've reached the book appointments page after successful login.");
            }
            else{
                test_.fail("Test failed, failed to reach book appointments page.");
            }
            System.out.println("We've Reached the home page");
        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Something went wrong, Test failed");
        }
    }
    public static void Goto_PO_Management(WebDriver driver, ExtentTest test_){
        try{
            driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/div/div/ul/li[4]/a/div")).click();
            Thread.sleep(8000);

            test_.info("Checking if we've reached the PO Management Page.");

            WebElement target_element =  driver.findElement(By.xpath("//h4"));

            if(Objects.equals(target_element.getText(), "PO Management")){
                test_.pass("We've reached PO Management Page");
            }
            else{
                test_.fail("Test failed, we've Failed to reach PO Management Page.");
            }

        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Something went wrong, Test failed.");
        }
    }
    public static void CreatePO(WebDriver driver, ExtentTest test_, Properties prop){
        try{

            // Click on the Create PO button
            driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div[1]/div[1]/div/div[1]/button")).click();
            System.out.println("Here we can add a new PO");
            // Wait for 2 seconds.
            Thread.sleep(2000);

            String po_number = prop.getProperty("po_number");
            String vender_name = prop.getProperty("vender_name");
            String commodity = prop.getProperty("commodity_name");
            String buyer_name = prop.getProperty("buyer_name");
            String items_count =  prop.getProperty("items_total");
            String cases_count =  prop.getProperty("cases_total");
            String total_weight =  prop.getProperty("weight_total");
            String pallet_count =  prop.getProperty("pallet_count");
            String dock_type_value =  prop.getProperty("dock_type_value");
            String dueDate = prop.getProperty("due_date");


            // Todo : Let's fill up the form. add these input values in Extent report.
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
            dock_type_options.selectByValue(dock_type_value);
            System.out.println("Selected option : "+ dock_type_options.getFirstSelectedOption().getText());

            Thread.sleep(2000);
            // 10. Select Due Date.
            driver.findElement(By.xpath("//input[@name='custom-date-picker']")).sendKeys("10-03-2024");

            Thread.sleep(2000);
            // Finally we click on the Save button.
            driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[2]/div/div/div[2]/button[2]")).click();
            System.out.println("Clicked the save button.");


            test_.info("Creating a new PO with these values : \n\n" + "PO Number : "+ po_number + "\n Vender Name : " + vender_name + "\n Buyer Name : " + buyer_name + "\n Dock Type : Dairy" + "\n Due date : " + dueDate + "\n Commodity : " + commodity  + "\n Total Items : " + items_count + "\n Total cases : " + cases_count + "\n Total weight : " + total_weight + "\n Pallet Count : " + pallet_count);



            Thread.sleep(5000); // Waiting to update the table with our new PO.

            String new_po_xpath = "//*[@id='pdf-content']/tbody/tr/td[2][text() = '" + po_number + "']";

            WebElement target_po_in_table = driver.findElement(By.xpath(new_po_xpath));

            if(target_po_in_table.isDisplayed()){
             test_.pass("Test passed, since we can see the new PO");
            }
            else{
                test_.fail("Test failed, Failed to create a new PO");
            }

        }
        catch (Exception E){
            test_.fail("Test failed, Failed to Create a new PO");
            E.printStackTrace();
            System.out.println("Something went wrong, Test failed.");
        }
    }
    public static void EditPO(WebDriver driver, ExtentTest test_){
        try{

            String targetCellValue = "666777";
//            WebElement targetRow = driver.findElement(By.xpath("//tr//td[text() = '" + targetCellValue + "']//parent::tr"));
//
            String target_row_xpath = "//tr//td[text() = '" + targetCellValue + "']//parent::tr/td[9]/div/img[1]";

            driver.findElement(By.xpath(target_row_xpath)).click();
//
//            // Clicking on the edit button (Represented by a pen)
//            driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div/div[2]/div/div[1]/table/tbody/tr[1]/td[9]/div/img[1]")).click();

            // Update the value for PO number
            String new_commodity = "Cottage Cheese";
            test_.info("Updating the Commodity to " + new_commodity + " where PO number is " + targetCellValue);


            // 4. Commodity
            WebElement  commodityInputField = driver.findElement(By.xpath("//input[@name='Commodity']"));
//            WebElement po_number_element = driver.findElement(By.xpath("//input[@name='PoNumber']"));
            //Since the clear method is not working, we'll use ctrl + a + del
            commodityInputField.sendKeys(Keys.CONTROL, "a");
            commodityInputField.sendKeys(Keys.DELETE);


            commodityInputField.sendKeys(new_commodity);
            Thread.sleep(2000);
            // Finally we click on the Save button.
            driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[2]/div/div/div[2]/button[2]")).click();
            System.out.println("Clicked the save button.");
            test_.pass("Commodity got updated successfully to " + new_commodity);
        }
        catch (Exception E){
            test_.fail("Test Failed, Failed to update the commodity");
            E.printStackTrace();
            System.out.println("Something went wrong,Test failed");
        }
    }
}
