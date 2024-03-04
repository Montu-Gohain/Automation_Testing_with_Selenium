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
import java.text.ParseException;
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

//             Step 3 : Fill the form to create a new PO
            ExtentTest test_create_new_PO = extent.createTest("Create a new PO", "Fill the input fields and create a new PO");
            CreatePO(driver,test_create_new_PO, prop, extent);


//              Step 4 : Edit the latest PO.
            ExtentTest test_Edit_existing_PO = extent.createTest("Edit an existing PO", "By clicking on the pencil icon we can edit the existing value for that PO.");
            EditPO(driver, test_Edit_existing_PO, prop, extent);

            Thread.sleep(3000);
            // Final wait before closing the browser.
        }

        catch (Exception E){
            E.printStackTrace();
            System.out.println("Something went wrong, Test failed");
        }
        driver.quit();
        extent.flush();
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
    public static void CreatePO(WebDriver driver, ExtentTest test_, Properties prop, ExtentReports extent){
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
            String dock_type_text =  prop.getProperty("dock_type_text");
            String dueDate = prop.getProperty("due_date");

            // Todo : Let's fill up the form. add these input values in Extent report.
            // 1. PO number
            test_.info("Entering PO Number : " + po_number);
            driver.findElement(By.xpath("//input[@name='PoNumber']")).sendKeys(po_number);

            // 2. Vender name
            test_.info("Entering vendor name : " + vender_name);
            driver.findElement(By.xpath("//input[@name='VendorName']")).sendKeys(vender_name);

            // 3. Buyer name
            test_.info("Entering Buyer name : " + buyer_name);
            driver.findElement(By.xpath("//input[@name='BuyerName']")).sendKeys(buyer_name);

            // 4. Commodity
            test_.info("Entering Commodity : " + commodity);
            driver.findElement(By.xpath("//input[@name='Commodity']")).sendKeys(commodity);

            // 5. Items
            test_.info("Entering items count : " + items_count);
            driver.findElement(By.xpath("//input[@name='Quantity']")).sendKeys(items_count);

            // 6. Cases
            test_.info("Entering total cases : " + cases_count);
            driver.findElement(By.xpath("//input[@name='Cases']")).sendKeys(cases_count);

            // 7. Weight in lbs
            test_.info("Entering total weight : " + total_weight);
            driver.findElement(By.xpath("//input[@name='Weight']")).sendKeys(total_weight);

            // 8. Pallet Count
            test_.info("Entering pallet count : " + total_weight);
            driver.findElement(By.xpath("//input[@name='Pallets']")).sendKeys(pallet_count);

            // 9. Select Dock Type
            // Clicking on the select option
//            driver.findElement(By.xpath("//select[@name='ProduceType']")).click();
//            Thread.sleep(1600);
            WebElement dock_type_selector = driver.findElement(By.xpath("//select[@name='ProduceType']"));
            Select dock_type_options = new Select(dock_type_selector);

            // Let's select Dairy.
            test_.info("Selecting Dock Type : Dairy");
            dock_type_options.selectByVisibleText(dock_type_text);
            System.out.println("Selected option : "+ dock_type_options.getFirstSelectedOption().getText());

            Thread.sleep(2000);
            // 10. Select Due Date.
            test_.info("Entering Due date : " + dueDate);
            driver.findElement(By.xpath("//input[@name='custom-date-picker']")).sendKeys(dueDate);

            Thread.sleep(2000);
            // Finally we click on the Save button.
            test_.info("Finally clicking on the save button");
            driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[2]/div/div/div[2]/button[2]")).click();
            System.out.println("Clicked the save button.");

            test_.info("Waiting for 5 seconds , so that the new PO appears in the table.");
            Thread.sleep(5000); // Waiting to update the table with our new PO.

            String new_po_xpath = "//*[@id='pdf-content']/tbody/tr/td[2][text() = '" + po_number + "']";

            test_.info("Finding new PO by the PO number : "  + po_number + " in the table");
            WebElement target_po_in_table = driver.findElement(By.xpath(new_po_xpath));

            if(target_po_in_table.isDisplayed()) {
                test_.pass("Test passed, since we can see the new PO");

               // Todo : Verifying the values in the PO cardView with the inputs given in the card.

                System.out.println("Testing validation of our inputs in the PO cardView");
                Thread.sleep(3000);

                ExtentTest test_validate_all_inputs_in_card = extent.createTest("Validating all input values with the values shown in PO cardView", "In this test  we are comparing the values we've used as the " +
                        "input while creating the PO , with the values displayed in the PO cardView.");

                test_validate_all_inputs_in_card.info("Click on the Schedule/Not Scheduled button on the table for PO with PO number : " + po_number);
                String target_row_xpath = "//tr//td[text() = '" + po_number + "']//parent::tr/td[8]/div";

                driver.findElement(By.xpath(target_row_xpath)).click();

                Thread.sleep(3000);
                test_validate_all_inputs_in_card.info("Comparing the values shown in the cardView with our inputs in the scripts.");
                System.out.println("Checking the input values displayed on the card.");

//              Todo : Here we'll compare the values shown in the card with the values written in the script.

                String po_number_in_card = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div/div[2]/div/div[1]/div/div[2]/div[1]/div[1]/div[2]")).getText();

                boolean match_po = Objects.equals(po_number, po_number_in_card);

                test_validate_all_inputs_in_card.info("Matching PO number. PO Number shown in card : " + po_number_in_card + " & written in script : " + po_number);
                test_validate_all_inputs_in_card.info("Matching status of PO number : " + match_po);

                String vendor_in_card = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div/div[2]/div/div[1]/div/div[2]/div[1]/div[5]/div[2]")).getText();
                boolean match_vendor = Objects.equals(vender_name, vendor_in_card);
                test_validate_all_inputs_in_card.info("Matching vendor name. Vender name shown in card : " + vendor_in_card + " & written in script : " + vender_name);
                test_validate_all_inputs_in_card.info("Matching status of vendor name : " + match_vendor);

                String buyer_name_in_card = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div/div[2]/div/div[1]/div/div[2]/div[1]/div[6]/div[2]")).getText();
                boolean match_buyer = Objects.equals(buyer_name, buyer_name_in_card);
                test_validate_all_inputs_in_card.info("Matching buyer name. Vender name shown in card : " + buyer_name_in_card + " & written in script : " + buyer_name);
                test_validate_all_inputs_in_card.info("Matching status of buyer name : " + match_buyer);

                String commodity_name_in_card = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div/div[2]/div/div[1]/div/div[2]/div[1]/div[7]/div[2]")).getText();
                boolean match_commodity = Objects.equals(commodity, commodity_name_in_card);
                test_validate_all_inputs_in_card.info("Matching Commodity name. Commodity shown in card : " + commodity_name_in_card + " & written in script : " + commodity);
                test_validate_all_inputs_in_card.info("Matching status of commodity name : " + match_commodity);

                String DocsAndPallets = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div/div[2]/div/div[1]/div/div[2]/div[1]/div[8]/div[2]")).getText();

                String dockType_in_card = DocsAndPallets.split("\\|")[0]; // Split using "|" and get the first element (index 0)
                String pallet_count_in_card = DocsAndPallets.split("\\|")[1]; // Split using "|" and get the first element (index 0)

                boolean match_docktype = Objects.equals(dock_type_text, dockType_in_card.trim());
                test_validate_all_inputs_in_card.info("Matching Dock type.Dock type shown in card : " + dockType_in_card + " & written in script : " + dock_type_text);
                test_validate_all_inputs_in_card.info("Matching status of Dock type : " + match_docktype);


                boolean match_pallet_count = Objects.equals(pallet_count_in_card.trim(), pallet_count);
                test_validate_all_inputs_in_card.info("Matching Pallet count.Total pallets shown in card : " + pallet_count_in_card + " & written in script : " + pallet_count);
                test_validate_all_inputs_in_card.info("Matching status of Pallets : " + match_pallet_count);

                String itemsAndCases = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div/div[2]/div/div[1]/div/div[2]/div[1]/div[9]/div[2]")).getText();
                String items_count_in_card = itemsAndCases.split("\\|")[0];
                String cases_count_in_card = itemsAndCases.split("\\|")[1];


                boolean match_items_count = Objects.equals(items_count, items_count_in_card.trim());
                test_validate_all_inputs_in_card.info("Matching Items count.Total items shown in card : " + items_count_in_card + " & written in script : " + items_count);
                test_validate_all_inputs_in_card.info("Matching status of Items : " + match_items_count);

                boolean match_cases_count = Objects.equals(cases_count_in_card.trim(), cases_count);
                test_validate_all_inputs_in_card.info("Matching Cases count.Total cases shown in card : " + cases_count_in_card + " & written in script : " + cases_count);
                test_validate_all_inputs_in_card.info("Matching status of Cases : " + match_cases_count);

                String due_date_in_card =  driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div/div[2]/div/div[1]/div/div[2]/div[1]/div[10]/div[2]")).getText();

//                Todo : Converting date in script input to proper date format
                try {
                    String outputFormat = "MM/dd/yyyy";
                    SimpleDateFormat inputFormatter = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);

                    Date date = inputFormatter.parse(dueDate);
                    String convertedDate = outputFormatter.format(date);

                    System.out.println("Converted date: " + convertedDate);

                    boolean match_due_date = Objects.equals(convertedDate,due_date_in_card);
                    test_validate_all_inputs_in_card.info("Matching Due date. Date shown in card : " + due_date_in_card + " & written in script : " + dueDate);
                    test_validate_all_inputs_in_card.info("Matching status of Due date : " + match_due_date);

                    if(match_po && match_vendor && match_buyer && match_commodity && match_docktype && match_items_count && match_pallet_count && match_cases_count && match_due_date){
                        test_validate_all_inputs_in_card.pass("Everything is matching as exptected. Hence we've successfully validated all input values with the newly created PO");
                    }
                    else{
                        test_validate_all_inputs_in_card.fail("Test failed, All of the fields in cardView are not matching with the script inputs");
                    }
                } catch (ParseException e) {
                    System.out.println("Error parsing date: " + e.getMessage());
                    e.printStackTrace();
                }
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
    public static void EditPO(WebDriver driver, ExtentTest test_, Properties prop, ExtentReports extent){
        try{
            // Navigating to PO Management page
            driver.findElement(By.xpath("/html/body/div/div[2]/div/div[1]/div/div/ul/li[4]/a/div")).click();
            Thread.sleep(6000);

            String targetCellValue = prop.getProperty("editable_po_number");
//
            String target_row_xpath = "//tr//td[text() = '" + targetCellValue + "']//parent::tr/td[9]/div/img[1]";

            driver.findElement(By.xpath(target_row_xpath)).click();

//            Todo : Let's update the values of current PO form.

            String new_commodity = prop.getProperty("editable_new_commodity");
            test_.info("Updating the Commodity to " + new_commodity + " where PO number is " + targetCellValue);


            // 4. Commodity
            WebElement  commodityInputField = driver.findElement(By.xpath("//input[@name='Commodity']"));

            commodityInputField.sendKeys(Keys.CONTROL, "a");
            commodityInputField.sendKeys(Keys.DELETE);
            commodityInputField.sendKeys(new_commodity);
            Thread.sleep(2000);

            String new_buyer_name = prop.getProperty("editable_new_buyer");
            WebElement buyer_name_input_field = driver.findElement(By.xpath("//input[@name='BuyerName']"));
            // Clearing the existing value in teh buyer name field
            buyer_name_input_field.sendKeys(Keys.CONTROL, "a");
            buyer_name_input_field.sendKeys(Keys.DELETE);

            buyer_name_input_field.sendKeys(new_buyer_name);
            // Finally we click on the Save button.
            driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[2]/div/div/div[2]/button[2]")).click();
            System.out.println("Clicked the save button.");
            test_.pass("Commodity got updated successfully to " + new_commodity);

            ExtentTest test_validate_values_after_edit = extent.createTest("Validate inputs after edit", "In this test we're validating the values in PO cardView after Editing some of the existing values.");

//            Todo : Now we'll do validation on the updated values.

            test_validate_values_after_edit.info("Clicking on Schedule/Not Schedule button to see the PO cardView");
            String target_row_xpath_ = "//tr//td[text() = '" + targetCellValue + "']//parent::tr/td[8]/div";

            driver.findElement(By.xpath(target_row_xpath_)).click();
            Thread.sleep(3000);

            // Todo : now let's match the values.
            String commodity_name_in_card = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div/div[2]/div/div[1]/div/div[2]/div[1]/div[7]/div[2]")).getText();
            boolean match_new_commodity = Objects.equals(new_commodity, commodity_name_in_card);
            test_validate_values_after_edit.info("Matching commodity. After edit commodity : " + commodity_name_in_card + " & new commodity on script : " + new_commodity);
            System.out.println("Edited Commodity matching status : " + match_new_commodity);

            String buyer_name_in_card = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div[2]/div/div/div[2]/div/div[1]/div/div[2]/div[1]/div[6]/div[2]")).getText();
            boolean match_new_buyer = Objects.equals(new_buyer_name,buyer_name_in_card);
            test_validate_values_after_edit.info("Matching Buyer name. After edit new Buyer name : " + buyer_name_in_card + " & new commodity on script : " + new_buyer_name);
            System.out.println("Edited Buyer matching status : " + match_new_buyer);

            if(match_new_commodity && match_new_buyer){
                test_validate_values_after_edit.pass("Test passed, since all the edited fields are matching with the given input in the script");
            }
            else{
                test_validate_values_after_edit.fail("Test failed, since all edited fields are not matching with the given input in the script");
            }
        }
        catch (Exception E){
            test_.fail("Test Failed, Failed to update the commodity");
            E.printStackTrace();
            System.out.println("Something went wrong,Test failed");
        }
    }
}
