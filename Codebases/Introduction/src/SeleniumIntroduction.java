import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumIntroduction {
	  public static void main(String[] args) {
       // Step 1 : Invoke Chrome browser using the ChromeBrowser Driver.
		  WebDriver driver = new ChromeDriver();
		  
		  // Let's try to open chrome using this script.
		  
		  driver.get("https://rahulshettyacademy.com");
		  
		  
		  // Let's get the title of the page.
		  String page_title = driver.getTitle();
		  System.out.println(page_title);
		  
		  // Let's get the current URL of the page.
		  
		  System.out.println(driver.getCurrentUrl());
		  
		  
		  
		  // Now after running all the test, we want the browser to be automatically get closed.
		  
		  driver.close();
				  
	  }
}
