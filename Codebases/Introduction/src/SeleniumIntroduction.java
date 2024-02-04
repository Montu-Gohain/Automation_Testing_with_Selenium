import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class SeleniumIntroduction {
	  public static void main(String[] args) {
		  
		  
		  String TEST_URL = "http://www.coingecko.com";
		  
		  
       // Step 1 : Invoke Chrome browser using the ChromeBrowser Driver.
		  WebDriver chrome_driver = new ChromeDriver();
		  
	   // Todo: To Invoke Firefox we need a driver named as gecko driver
		  // Let's try to open chrome using this script.
		  chrome_driver.get("https://rahulshettyacademy.com");
		  
		  
		  // Let's get the title of the page.
		  String page_title = chrome_driver.getTitle();
		  System.out.println("Webpage title in chrome :- " + page_title);
		  
		  // Let's get the current URL of the page.
		  
		  System.out.println("Currently hitting this URL :- " + chrome_driver.getCurrentUrl());
		  
		  // Now after running all the test, we want the browser to be automatically get closed.
		  
		   //driver.close(); // This closes just the current window.
		  
		  // driver.quit(); closes all the windows and tabs that being opened by the selenium script.
			
		  chrome_driver.quit();
		  
		  
		  // Todo : Let's invoke Microsoft Edge with the help of edge driver
		  
		  System.setProperty("webdriver.edge.driver", "E:/Exuber_work/Course_Learnings/msedgedriver.exe");
		  WebDriver edge_driver = new EdgeDriver();
		  
		  edge_driver.get(TEST_URL);
		
		  // Let's get the title of this webpage
		  
		  String page_title_2 = edge_driver.getTitle();
		  
		  System.out.println("Webpage title in Edge :- " + page_title_2);
		  
		  
		  edge_driver.close();
		  
		  
	  }
}
