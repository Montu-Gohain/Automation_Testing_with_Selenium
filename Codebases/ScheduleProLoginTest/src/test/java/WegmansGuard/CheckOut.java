package WegmansGuard;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class CheckOut {
    public static void main(String[] args) {
        // Todo : Setting up ExtentReport package to give us test results.
        ExtentReports extent = new ExtentReports();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = sdf.format(new Date());
        String filename = "target/Reports/Check_out-" + timestamp + ".html";
        ExtentSparkReporter spark = new ExtentSparkReporter(filename);
        spark.config().setTheme(Theme.DARK);
        extent.attachReporter(spark);

        // Todo : Initializing the Webdriver in our case ChromeDriver.

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1500,968");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(120));

        try{
            Properties prop = new Properties();
            File file = new File("testdata_wegmansguard.properties");
            FileInputStream fis = new FileInputStream(file);
            prop.load(fis);

//            Auto sign in to the Check in Page
            auto_sign_in(driver, prop);

//            Check In
            check_out(driver, prop, extent);

            Thread.sleep(3900); // Wait for 3.9 seconds before closing the browser.
            driver.quit();
            extent.flush();
        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Tests failed , something went wrong.");
        }
    }
    public static void auto_sign_in(WebDriver driver, Properties prop ){
        try{
            String TEST_URL = prop.getProperty("testUrl");
            System.out.println("Opening up the Test url");
            driver.get(TEST_URL);
        }
        catch (Exception E){
            E.printStackTrace();
        }
    }
    public static void check_out(WebDriver driver, Properties prop, ExtentReports extent){
       try{
           // Click on Check Out section and wait for the data to load in the page.
           ExtentTest test_check_out = extent.createTest("Testing Check Out a Truck by trailer number.","In this test we're going to Checkout a truck from the CheckOut " +
                   "Page and we'll verify the successful Checkout");

           test_check_out.info("Selecting the Check Out Option from the Top Menu");

           driver.findElement(By.xpath("//div[@class=\"navText\" and text()=\"Check Out\"]")).click();
           Thread.sleep(6000);

           test_check_out.info("Grab the target trailer number from testdata file");
           String targeted_trailer_no = prop.getProperty("target_trailer_no");

           //        Click on the Truck Icon present the targeted Trailer no row.
           List<WebElement> total_trucks_live = driver.findElements(By.xpath("//tbody/tr"));
           int current_live_trucks_before = total_trucks_live.size();
           test_check_out.info("Total Live trucks before Checkout : " + current_live_trucks_before);


           System.out.println("Before Checkout Total live trucks : " + current_live_trucks_before);

           test_check_out.info("Clicking the CheckOut Button for trailer number : " + targeted_trailer_no);
           String target_checkout_btn_xpath = "//tr/td[text() = '" + targeted_trailer_no + "']//parent::tr/td[10]/div/div/button";

           driver.findElement(By.xpath(target_checkout_btn_xpath)).click();

           Thread.sleep(4000);

            // Click Yes on the confirmation button.
           test_check_out.info("Clicking on Yes in the confirmation Modal.");
           driver.findElement(By.xpath("//button[@type=\"button\" and text()='Yes']")).click();

           Thread.sleep(3000);


           // After Checking Out one trailer, number of live trucks now.
           int total_trucks_live_after = driver.findElements(By.xpath("//tbody/tr")).size();
           test_check_out.info("After Checkout Total live trucks : " + total_trucks_live_after);
           System.out.println("After Checkout Total live trucks : " + total_trucks_live_after);

         if (current_live_trucks_before == (total_trucks_live_after + 1)){
             test_check_out.pass("Test passed,since number of total live trucks decreased by one.");
             System.out.println("Successfully Checked out the Truck with trailer number : " + targeted_trailer_no);
         }
         else{
             test_check_out.fail("Test failed,since number of total live trucks haven't decreased.");
             System.out.println("Failed to Check Out the Truck with trailer number : " + targeted_trailer_no);
         }
       }
       catch (Exception E){
           E.printStackTrace();
       }


    }

}
