package WegmansGuard;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class Filter_and_Search_LiveTrucks {
    public static void main(String[] args) {
        // Todo : Setting up ExtentReport package to give us test results.
        ExtentReports extent = new ExtentReports();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = sdf.format(new Date());
        String filename = "target/Reports/Filter_and_search_LiveTrucks-" + timestamp + ".html";
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
            auto_sign_in(driver, prop, extent);

//            Navigate to Live Trucks
            navigate_to_live_trucks(driver, extent);

            count_test_with_filters(driver, extent);

            search_with_trailer_number(driver, prop, extent);

            finding_edit_option(driver, extent);



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
    public static void navigate_to_live_trucks(WebDriver driver, ExtentReports extent){
        try{
            ExtentTest test_navigate_to_live_trucks_page = extent.createTest("Navigate to Live Trucks.", "In this test we'll verify that we've successfully able to navigate to Live Trucks page.");

            test_navigate_to_live_trucks_page.info("Click on Reports Section.");
            driver.findElement(By.xpath("//span[text()='Reports']")).click();

            test_navigate_to_live_trucks_page.info("Select Live trucks from the Dropdown options");
            driver.findElement(By.xpath("//a[text()=\"Live Trucks\"]")).click();

            Thread.sleep(4000);

            test_navigate_to_live_trucks_page.info("Verify that we've reached Live trucks page by checking Live Trucks Element is appearing or not.");
            WebElement live_trucks_text_element = driver.findElement(By.xpath("//div[@class='fw-bold' and text()=\"Live Trucks\"]"));

            if(live_trucks_text_element.isDisplayed()){
                test_navigate_to_live_trucks_page.pass("Test passed, We've reached Live Trucks page successfully.");
            }
            else{
                test_navigate_to_live_trucks_page.fail("Test failed, we've failed to reach Live trucks page.");
            }
        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Test failed, something went wrong.");
        }
    }

    public static void count_test_with_filters(WebDriver driver, ExtentReports extent){
        try{
            ExtentTest test_count_all_data = extent.createTest("Count no. of rows while All Load is selected.","In this test we verify the no." +
                    " of total count in the live trucks and total no. of rows in individual sections. ");

            test_count_all_data.info("Checking total count while filter is set to All Load");

            WebElement total_count_text = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[3]/div[3]/div/div/div[1]/span[2]"));
            String total_count = total_count_text.getText();

            String[] words = total_count.split("\\s+");
            String total_count_value_all_load = words[words.length - 1];

            test_count_all_data.info("Total count found while All Load filter is selected : " + total_count_value_all_load);


            List<WebElement> all_load_data_rows = driver.findElements(By.xpath("//tbody//tr"));

            if(all_load_data_rows.size() == Integer.parseInt(total_count_value_all_load)){
              test_count_all_data.pass("Test passed, since there are total " + total_count_value_all_load + " no. of rows available in the table which is " +
                      "equal to the count.");
            }
            else{
                test_count_all_data.fail("Test Failed, since there are total " + total_count_value_all_load + " no. of rows available in the table and total " +
                        "count is not matching.");
            }

            ExtentTest test_count_inbound_load = extent.createTest("Count no. of rows while Inbound Load is selected.", "While selecting Inbound load in the filter we'll check if there are " +
                    "actually n number of rows present in the table which equals to total count for Inbound load");
            //            Now lets' select Inbound Load

            test_count_inbound_load.info("Clicking on All Load filter option.");
            driver.findElement(By.xpath("//span[text()=\"All Load\"]")).click();
            Thread.sleep(3000);
            test_count_inbound_load.info("Select Inbound Load from the dropdown menu.");
            driver.findElement(By.xpath("//a[@class=\"dropdown-item\" and text()=\"Inbound Load\"]")).click();

            Thread.sleep(5000);

            test_count_inbound_load.info("Checking total count while filter is set to Inbound Load");

            WebElement Inbound_total_count_text = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[3]/div[3]/div/div/div[1]/span[2]"));
            String Inbound_total_count = Inbound_total_count_text.getText();

            String[] inbound_words = Inbound_total_count.split("\\s+");
            String total_inbound_count = inbound_words[inbound_words.length - 1];

            test_count_inbound_load.info("Total count found while Inbound Load filter is selected : " + total_inbound_count);

            List<WebElement> inbound_load_data_rows = driver.findElements(By.xpath("//tbody//tr"));

            if(inbound_load_data_rows.size() == Integer.parseInt(total_inbound_count)){
                test_count_inbound_load.pass("Test passed, since there are total " + total_inbound_count + " no. of rows available in the table which is " +
                        "equal to the count.");
            }
            else{
                test_count_inbound_load.fail("Test Failed, since there are total " + total_inbound_count + " no. of rows available in the table and total " +
                        "count is not matching.");
            }

           //            Now lets' select Other Load

            ExtentTest test_count_other_load = extent.createTest("Count no. of rows while Other Load is selected.", "While selecting Other load in the filter we'll check if there are " +
                    "actually n number of rows present in the table which equals to total count for Other load");


            test_count_other_load.info("Clicking On Inbound Load.");
            driver.findElement(By.xpath("//span[text()=\"Inbound Load\"]")).click();
            Thread.sleep(3000);
            test_count_other_load.info("Select Inbound Load from the dropdown menu.");
            driver.findElement(By.xpath("//a[@class=\"dropdown-item\" and text()=\"Other Load\"]")).click();

            Thread.sleep(5000);

            test_count_other_load.info("Checking total count while filter is set to Other Load");

            WebElement Other_total_count_text = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[3]/div[3]/div/div/div[1]/span[2]"));
            String Other_total_count = Other_total_count_text.getText();

            String[] other_words = Other_total_count.split("\\s+");
            String total_other_load_count = other_words[other_words.length - 1];

            test_count_other_load.info("Total count found while Other Load filter is selected : " + total_other_load_count);

            List<WebElement> other_load_data_rows = driver.findElements(By.xpath("//tbody//tr"));

            if(other_load_data_rows.size() == Integer.parseInt(total_other_load_count)){
                test_count_other_load.pass("Test passed, since there are total " + total_other_load_count + " no. of rows available in the table which is " +
                        "equal to the count.");
            }
            else{
                test_count_other_load.fail("Test Failed, since there are total " + total_other_load_count + " no. of rows available in the table and total " +
                        "count is not matching.");
            }

        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Test failed, something went wrong.");
        }
    }

    public static void search_with_trailer_number(WebDriver driver,Properties prop, ExtentReports extent){
        try{
            ExtentTest test_search_function_with_trailer_no = extent.createTest("Test out the search Functionality by providing Trailer number.","In this test we'll test the Search Functionality by providing " +
                    " the trailer no. and see if we can successfully find the data liked with the given trailer number.");

            test_search_function_with_trailer_no.info("Grabbing the target trailer number from testdata file.");
            String target_trailer_no = prop.getProperty("target_trailer_no_for_searching");

            test_search_function_with_trailer_no.info("Entering trailer number : " + target_trailer_no + " in the search option.");
            driver.findElement(By.xpath("//input[@placeholder='Search']")).sendKeys(target_trailer_no);
            Thread.sleep(4000);

            test_search_function_with_trailer_no.info("Checking if we found any data liked with trailer no : " + target_trailer_no);

                String found_trailer_no = driver.findElement(By.xpath("//tbody/tr/td[1]")).getText();
            System.out.println(found_trailer_no.equals(target_trailer_no));
                if(found_trailer_no.equals(target_trailer_no)){
                    test_search_function_with_trailer_no.info("Data found with this trailer no : " + target_trailer_no);
                    test_search_function_with_trailer_no.info("Driver name : " + driver.findElement(By.xpath("//tbody/tr/td[5]")).getText());
                    test_search_function_with_trailer_no.info("Phone number : " + driver.findElement(By.xpath("//tbody/tr/td[6]")).getText());
                    test_search_function_with_trailer_no.info("License number : " + driver.findElement(By.xpath("//tbody/tr/td[7]")).getText());
                    test_search_function_with_trailer_no.pass("Test passed, we've found data by providing the trailer no : " + target_trailer_no);
                }
                else{
                    test_search_function_with_trailer_no.fail("Test failed, we've found some data but trailer no is not matching with our targeted trailer no.");
                }
        }
        catch (Exception E){
            E.printStackTrace();
        }
    }
    public static void finding_edit_option(WebDriver driver, ExtentReports extent){
        try{

//            Xpath for tdata where checkout time is not mentioned.
            //tbody/tr/td[4][contains(text(),'-')] in live trucks
            //tbody/tr/td[6][contains(text(),'-')] in daily arrivals
            ExtentTest test_edit_option = extent.createTest("Testing edit option.","In this test we'll check whether we can find the edit option where checkout time is not available.");

            test_edit_option.info("Clearing out the search Input field.");
            driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[3]/div[1]/div[2]/div[2]/div/div/span/div")).click();
            Thread.sleep(4000);

            test_edit_option.info("Finding the first row in which Check Out time is not available.");

            WebElement row_without_checkout_time = driver.findElement(By.xpath("(//tbody/tr/td[4][contains(text(),'-')])[1]"));

            if(row_without_checkout_time.isDisplayed()){
                String trailer_without_checkout_time =  driver.findElement(By.xpath("(//tbody/tr/td[4][contains(text(),'-')])[1]//parent::tr/td[1]")).getText();
                test_edit_option.info("We've found one row without Checkout time, with trailer no : " + trailer_without_checkout_time);
             Thread.sleep(3000);
//                Checking the availability of edit option for this row.
                WebElement edit_option = driver.findElement(By.xpath("(//tbody/tr/td[4][contains(text(),'-')])[1]//parent::tr/td[10]//img[@alt='Edit Po']"));

                if(edit_option.isDisplayed()){
                    test_edit_option.pass("Test passed, Edit option is enabled for this row with trailer no : " + trailer_without_checkout_time);
                }
                else {
                    test_edit_option.fail("Test failed, Edit option is not available for this row with trailer no : " + trailer_without_checkout_time + " even though checkout time is not available.");
                }

            }
            else{
              test_edit_option.fail("We've not found any row without Checkout Time. Hence, failed to perform the test.");
            }

        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Test failed, something went wrong.");
        }
    }
}
