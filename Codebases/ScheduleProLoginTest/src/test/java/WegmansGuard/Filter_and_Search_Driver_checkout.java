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
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class Filter_and_Search_Driver_checkout {
    public static void main(String[] args) {
        // Todo : Setting up ExtentReport package to give us test results.
        ExtentReports extent = new ExtentReports();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = sdf.format(new Date());
        String filename = "target/Reports/Filter_and_search_Driver_Checkout-" + timestamp + ".html";
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
            navigate_to_driver_checkout(driver, extent);

//            Setting date range.
            set_date_range(driver, prop, extent);


//            counting after applying filters
            count_test_with_filters(driver, extent);

//          Search data with trailer number.
            search_with_trailer_number(driver, prop, extent);

//            finding_edit_option(driver, extent);



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
    public static void navigate_to_driver_checkout(WebDriver driver, ExtentReports extent){
        try{
            ExtentTest test_navigate_to_driver_checkout_page = extent.createTest("Navigate to Driver Checkout.", "In this test we'll verify that we've successfully able to navigate to Driver Checkout page.");

            test_navigate_to_driver_checkout_page.info("Click on Reports Section.");
            driver.findElement(By.xpath("//span[text()='Reports']")).click();

            test_navigate_to_driver_checkout_page.info("Select Driver Checkout from the Dropdown options");
            driver.findElement(By.xpath("//a[text()=\"Driver Checkout\"]")).click();

            Thread.sleep(4000);

            test_navigate_to_driver_checkout_page.info("Verify that we've reached Driver Checkout page by checking Driver Checkout Element is appearing or not.");
            WebElement live_trucks_text_element = driver.findElement(By.xpath("//div[@class='fw-bold' and text()=\"Driver Checkout\"]"));

            if(live_trucks_text_element.isDisplayed()){
                test_navigate_to_driver_checkout_page.pass("Test passed, We've reached Driver Checkout page successfully.");
            }
            else{
                test_navigate_to_driver_checkout_page.fail("Test failed, we've failed to reach Driver Checkout page.");
            }
        }
        catch (Exception E){
            E.printStackTrace();
            System.out.println("Test failed, something went wrong.");
        }
    }
    public static void set_date_range(WebDriver driver, Properties prop, ExtentReports extent){
        try{
//            Todo : Before counting let's change the date to a range from 8th march to 11 march.

            ExtentTest test_set_date_range = extent.createTest("Set date range","In this test we'll set a specific date range which will " +
                    "provide us enough data to perform the tests effectively.");

            test_set_date_range.info("Clicking on the calender icon");
            driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[1]/div[2]/div[1]/div/div/button/div/div")).click();
            Thread.sleep(2000);

            test_set_date_range.info("Grabbing start and end date from testdata file");
            String start_date = prop.getProperty("date_range_start");
            String end_date = prop.getProperty("date_range_end");

            test_set_date_range.info("Setting date range from day : " + start_date + " to end day : " + end_date);

            driver.findElement(By.xpath("//div[@role='option' and text() = '" + start_date + "']")).click();
            Thread.sleep(2000);
            driver.findElement(By.xpath("//div[@role='option' and text() = '" + end_date + "']")).click();

            Thread.sleep(4000);

            String selected_date_range_text = driver.findElement(By.xpath("//span[@data-testid='DAY_LABEL']")).getText();


            // Regular expression to match numbers
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(selected_date_range_text);

            int[] numbers = new int[2];
            int [] selected_dates = {Integer.parseInt(start_date), Integer.parseInt(end_date)};


            int i = 0;
            while (matcher.find()) {
                numbers[i++] = Integer.parseInt(matcher.group());
            }

            if(Arrays.equals(numbers, selected_dates)){
                System.out.println("Test passed, date range set successfully");
                test_set_date_range.pass("Test passed, successfully set the date range as : " + selected_date_range_text);
            }
            else{
                System.out.println("Test failed. we couldn't set date range successfully.");
            }
        }
        catch (Exception E){
            E.printStackTrace();
        }
    }

    public static void count_test_with_filters(WebDriver driver,ExtentReports extent){
        try{
            Thread.sleep(3000);
            ExtentTest test_count_all_data = extent.createTest("Count no. of rows while All Load is selected.","In this test we verify the no." +
                    " of total count in the live trucks and total no. of rows in individual sections. ");

            test_count_all_data.info("Checking total count while filter is set to All Load");
            System.out.println("Checking total count while filter is set to All Load");

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
            //            Now lets' select Other Load

            ExtentTest test_count_other_load = extent.createTest("Count no. of rows while Other Load is selected.", "While selecting Other load in the filter we'll check if there are " +
                    "actually n number of rows present in the table which equals to total count for Other load");


//            test_count_other_load.info("Clicking On Inbound Load.");
            driver.findElement(By.xpath("//span[text()=\"All Load\"]")).click();
            Thread.sleep(3000);
            test_count_other_load.info("Select Other Load from the dropdown menu.");
            driver.findElement(By.xpath("//a[@class=\"dropdown-item\" and text()=\"Other Load\"]")).click();

            Thread.sleep(5000);

            test_count_other_load.info("Checking total count while filter is set to Other Load");

            WebElement Other_total_count_text = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[3]/div[3]/div/div/div[1]/span[2]"));

            if(Other_total_count_text.isDisplayed()){
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
            else{
                test_count_other_load.fail("Test failed, because there is no data available to perform the counting.");
            }

//            Todo : Let's test out while selecting the Inbound Load option.
            ExtentTest test_counting_inbound_load = extent.createTest("Count no. of rows while Inbound Load is selected.", "While selecting Inbound load in the filter we'll check if there are " +
                    "actually n number of rows present in the table which equals to total count for Inbound load");

            test_counting_inbound_load.info("Clicking on Other Load");

            driver.findElement(By.xpath("//span[text()=\"Other Load\"]")).click();
            Thread.sleep(2000);

            test_counting_inbound_load.info("Selecting on Inbound Load.");
            driver.findElement(By.xpath("//a[@class=\"dropdown-item\" and text()=\"Inbound Load\"]")).click();
            Thread.sleep(5000);

            WebElement no_data_message  = driver.findElement(By.xpath("//*[@id=\"pdf-content\"]/tbody/tr/td/div/h6"));
            if(no_data_message.isDisplayed()){
                System.out.println("Inbound load doesn't have any data");
                test_counting_inbound_load.fail("Test failed, due to unavailability of data in Inbound Load.");
            }
            else{
                System.out.println("Inbound load has some data");
                test_counting_inbound_load.info("Checking total count while filter is set to Inbound Load");

                WebElement Inbound_total_count_text = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[3]/div[3]/div/div/div[1]/span[2]"));
                String Inbound_total_count = Inbound_total_count_text.getText();

                String[] inbound_words = Inbound_total_count.split("\\s+");
                String total_inbound_count = inbound_words[inbound_words.length - 1];

                test_counting_inbound_load.info("Total count found while Inbound Load filter is selected : " + total_inbound_count);

                List<WebElement> inbound_load_data_rows = driver.findElements(By.xpath("//tbody//tr"));

                if(inbound_load_data_rows.size() == Integer.parseInt(total_inbound_count)){
                    test_counting_inbound_load.pass("Test passed, since there are total " + total_inbound_count + " no. of rows available in the table which is " +
                            "equal to the count.");
                }
                else{
                    test_counting_inbound_load.fail("Test Failed, since there are total " + total_inbound_count + " no. of rows available in the table and total " +
                            "count is not matching.");
                }
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


            test_search_function_with_trailer_no.info("Selecting Other Load in the filter");


            driver.findElement(By.xpath("//span[text()=\"Inbound Load\"]")).click();
            Thread.sleep(2000);
            driver.findElement(By.xpath("//a[@class=\"dropdown-item\" and text()=\"Other Load\"]")).click();
            Thread.sleep(5000);

            test_search_function_with_trailer_no.info("Grabbing the target trailer number from testdata file.");
            String target_trailer_no = prop.getProperty("target_trailer_no_for_searching_");

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
}
