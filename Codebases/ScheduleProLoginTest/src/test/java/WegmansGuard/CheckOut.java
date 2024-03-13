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


           //     Auto sign in to the Check in Page
            auto_sign_in(driver, prop,extent);


            //      Navigate to checkout page
            navigate_to_checkout_page(driver, prop, extent);

            //            Check out
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
    public static void auto_sign_in(WebDriver driver, Properties prop,ExtentReports extent ){
        try{
            ExtentTest test_auto_signIn = extent.createTest("Testing Auto SignIn", "In this test we're check if we're able to see the " +
                    "Auto Sign In feature of WegmansGuard site.");

            test_auto_signIn.info("Grabbing the Test Site Url from testdata file");
            String TEST_URL = prop.getProperty("testUrl");
            System.out.println("Opening up the Test url");
            test_auto_signIn.info("Opening the url in the browser.");
            driver.get(TEST_URL);

            // Now we'll check if we've reached the page which we've been looking for
            WebElement select_an_option_text = driver.findElement(By.xpath("//span[@class=\"inputTitle\"]"));

            test_auto_signIn.info("Checking if we can see the Select a Type text in the Page.");
            if(select_an_option_text.isDisplayed()){
                System.out.println("Test passed,since the target text is shown in the page.");
                test_auto_signIn.pass("Test passed, We've successfully reached the Check In page.");
            }
            else{
                System.out.println("Test failed,target text is not visible on the page.");
                test_auto_signIn.fail("Test Failed, Failed to reach the Check In Page");
            }
        }
        catch (Exception E){
            E.printStackTrace();
        }
    }
    public static void navigate_to_checkout_page(WebDriver driver, Properties prop,ExtentReports extent ){
        try {
            ExtentTest test_navigate_to_checkout_page = extent.createTest("Navigate to CheckOut Page.", "In this test we're checking whether we're able to navigate to the Check Out page.");

            test_navigate_to_checkout_page.info("Click on Check Out option from the top menu.");
//            Click On Check Out section.
            driver.findElement(By.xpath("//div[@class='navText' and text()='Check Out']")).click();
            Thread.sleep(4000);
            WebElement driver_checkout_text = driver.findElement(By.xpath("//div[@class='fw-bold' and text()='Driver Check Out']"));
            if(driver_checkout_text.isDisplayed()){
                test_navigate_to_checkout_page.pass("Test passed, we've reached Driver Check Out Page successfully.");
            }
            else {
                test_navigate_to_checkout_page.fail("Failed to reach Driver Check Out Page.");
            }

        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Tests failed, something went wrong.");
        }
    }
    public static void check_out(WebDriver driver, Properties prop, ExtentReports extent){
       try{

           String target_traler_no = prop.getProperty("checkout_data_trailer_no");
           ExtentTest test_check_out = extent.createTest("Check out","In this test we'll checkout one Truck using its trailer number and check the total count for live trucks got decreased or not.");

           String current_live_trucks = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[3]/div[3]/div/div/div[1]/span[2]")).getText();

           String[] words = current_live_trucks.split(" ");
           String total_count = words[words.length - 1];
           System.out.println(total_count);

           test_check_out.info("Current live trucks : " + total_count);

           test_check_out.info("Clicking on the Truck icon ");

           driver.findElement(By.xpath("(//tr/td[3 and text()='"+target_traler_no+"'])//parent::*/td[10]/div/div/button")).click();

           Thread.sleep(1000);

           test_check_out.info("Clicking on Yes in the confirmation pop-up");
           driver.findElement(By.xpath("//button[text()='Yes']")).click();

           Thread.sleep(5000);

           String updated_live_trucks = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[3]/div[3]/div/div/div[1]/span[2]")).getText();

           String[] words_ = updated_live_trucks.split(" ");
           String total_count_new = words_[words_.length - 1];
           System.out.println("After checkout current total count : " + total_count_new);

           if(Integer.parseInt(total_count_new) < Integer.parseInt(total_count)){
               test_check_out.info("Total live trucks available : " + total_count_new);
               test_check_out.pass("Test passed, we've successfully Checked out the truck with Trailer no : " + target_traler_no);
           }
           else {
               test_check_out.fail("Test failed, we've failed to Checkout one Truck with trailer no :  " + target_traler_no);
           }
       }
       catch (Exception E){
           E.printStackTrace();
       }
    }
}
