import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumIntroduction {
	  public static void main(String[] args) {

      // Step 1 : Invoking the browser with ChromeDriver
		  WebDriver driver = new ChromeDriver(); // Here Webdriver is an interface
		 
		  
		  String SITE_URL = "https://www.udemy.com";
		  
		  // To hit a URL on the browser we can use this method.
		  driver.get(SITE_URL);
		  
		  
		  // To get the title of the webpage.
		 String webpage_title = driver.getTitle();		 
		 System.out.println(webpage_title);
	  }
}
