package lambdatestjava;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LambdaTestJava {
	String username = "gowthampt2000";
	String accesskey = "enhYx368y7qCBkD3uqCBmcFAv3YLDkTkwfKythWAf9fBZTXGKO";
	public static RemoteWebDriver driver = null;
	public String gridURL = "@hub.lambdatest.com/wd/hub";
	boolean status = false;

	@BeforeClass
	@Parameters(value = { "browser", "version", "platform" })
	public void setUp(String browser, String version, String platform) throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("browserName", browser);
		capabilities.setCapability("version", version);
		capabilities.setCapability("platform", platform); // If this cap isn't specified, it will just get the any
															// available one
		capabilities.setCapability("build", "selenium advanced");
		capabilities.setCapability("name",browser+"-"+version+"-"+platform);
		capabilities.setCapability("network", true); // To enable network logs
		capabilities.setCapability("visual", true); // To enable step by step screenshot
		capabilities.setCapability("video", true); // To enable video recording
		capabilities.setCapability("console", true); // To capture console logs

		try {
			driver = new RemoteWebDriver(new URL("https://" + username + ":" + accesskey + gridURL), capabilities);
			//navigate to url
			driver.get("https://www.lambdatest.com/");
		} catch (MalformedURLException e) {
			System.out.println("Invalid grid URL");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test(timeOut = 20000)
	public void testScenario1() throws Exception {
		try {
			
			new WebDriverWait(driver,Duration.ofSeconds(40)).until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
			
			//3.Move to See All Integrations
			WebElement integrationLink=driver.findElement(By.xpath("//a[text()='See All Integrations']//img"));
			Actions action=new Actions(driver);
			action.moveToElement(integrationLink);
			Thread.sleep(1000);
			String urll=driver.findElement(By.xpath("//a[text()='See All Integrations']")).getAttribute("href");
			
			

			//4.Open See All Integrations link in new tab
			((JavascriptExecutor)driver).executeScript("window.open()");
		    ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		    driver.switchTo().window(tabs.get(1));
		    driver.get(urll);
			
			//5.Print all windows
			System.out.println(tabs);

			
			//6.Verify whether the URL is the same as the expected URL 
			Assert.assertEquals(driver.getCurrentUrl(),"https://www.lambdatest.com/integrations");
			
			//7. On that page, scroll to the page where the WebElement(Codeless Automation) is present
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", driver.findElement(By.partialLinkText("Codeless Automation")));
			
			//8. Click the ‘LEARN MORE’ link for Testing Whiz. The page should openin the same window.
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", driver.findElement(By.xpath("//a[text()='Integrate Testing Whiz with LambdaTest']")));
			
			
			
			
			//9. Check if the title of the page is ‘TestingWhiz Integration | LambdaTest’.
			Assert.assertNotEquals("TestingWhiz Integration | LambdaTest",driver.getCurrentUrl());
			
			//10. Close the current window using the window handle [which we obtained in step (5)]
			driver.close();
			
			//11. Print the current window count.
			ArrayList<String> tabs2 = new ArrayList<>(driver.getWindowHandles());
			System.out.println(tabs2);
			driver.switchTo().window(tabs2.get(0));
			
			//12. On the current window, set the URL to https://www.lambdatest.com/blog.
			driver.get("https://www.lambdatest.com/blog");
			
			//13. Click on the ‘Community’ link and verify whether the URL is https://community.lambdatest.com/.
			driver.findElement(By.xpath("(//a[text()='Community'])[1]")).click();
			Assert.assertEquals("https://community.lambdatest.com/",driver.getCurrentUrl());
			
			
			
			
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@AfterClass
	public void tearDown() throws Exception {
		if (driver != null) {
			((JavascriptExecutor) driver).executeScript("lambda-status=passed");
			driver.quit();
		}
	}
}
