package com.extentreport.practise.ExtentReportDemo;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;

public class ExtentReportCreator 
{
	WebDriver driver;
	static ExtentReports extent;
	ExtentTest testDesktop,testMobile;
	
	public void delay(long time)
	  {
		  try
		  {
			  Thread.sleep(time);
		  } 
		  catch (InterruptedException e) 
		  {
			  e.printStackTrace();
		  }
	  }
	
	public String takeScreenshot(String name)
	{
		TakesScreenshot ts= (TakesScreenshot)driver;
		File source=ts.getScreenshotAs(OutputType.FILE);
		String dest="Resource/Screenshot"+name+".png";
		File destination=new File(dest);
		
		try {
			FileUtils.copyFile(source, destination);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return dest;
	}
	
@Parameters("platform")
  @BeforeTest
  public void beforeTest(String str) 
  {
	 ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("TwitterExtentReport.html");
//	 htmlReporter.setAppendExisting(true);
	 extent = new ExtentReports();
	 extent.attachReporter(htmlReporter);
	 
	if(str.equals("desktop"))
	{
		  testDesktop = extent.createTest("DesktopTest");
		  testDesktop.info("Testing website on Desktop");
		  
		  System.setProperty("webdriver.chrome.driver", "Resource/chromedriver.exe");
		  driver=new ChromeDriver();
		  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		  try 
		  {
			  driver.navigate().to("https://twitter.com/");
			  testDesktop.pass("Required website is opened");
		  }
		  catch (Exception e) 
		  {
			  testDesktop.fail("Unable to open website");
		  }
		  
		  driver.manage().window().maximize();	
	}
	else if(str.equals("mobile"))
	{
		testMobile = extent.createTest("MobileTest");
		testMobile.info("Testing website on Mobile");
		
		DesiredCapabilities cap=new DesiredCapabilities();
		cap.setCapability("platformName", "Android");
		cap.setCapability("platformVersion", "9");
		cap.setCapability("deviceName", "Redmi Note 7 Pro");
		cap.setCapability("browserName", "chrome");
		
		URL url = null;
		try 
		{
			url = new URL("http://0.0.0.0:4723/wd/hub");
		}
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		driver= new RemoteWebDriver(url, cap);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		  try 
		  {
			  driver.get("https://mobile.twitter.com/login?lang=en");
			  testMobile.pass("Required website is opened");
		  }
		  catch (Exception e) 
		  {
			  testMobile.fail("Unable to open website");
		  }
		
	}
  }

@Parameters("platform")
  @Test
  public void start(String str) 
  {
	if(str.equals("desktop"))
	{
		  try 
		  {
			  WebElement loginBtn1 = driver.findElement(By.xpath("(//a[contains(text(),'Log in')])[2]"));
			  loginBtn1.click();
			  testDesktop.pass("Login button on signup page clicked");
			  delay(1000);
		  }
		  catch (Exception e) 
		  {
			  testDesktop.fail("Unable to click login button on signup page");
		  }
		
		  try 
		  {
			  WebElement email= driver.findElement(By.xpath("//input[contains(@placeholder,'Phone, email or username')]"));
			  email.sendKeys("kunalwagh1163@gmail.com");
			  testDesktop.pass("Email id entered");
			  delay(1000);
		  }
		  catch (Exception e) 
		  {
			  testDesktop.fail("Unable to enter email id");
		  }
	
		  try 
		  {
			  WebElement password= driver.findElement(By.xpath("(//input[@placeholder='Password'])[2]"));
			  password.sendKeys("kunal12345");
			  testDesktop.pass("Passowrd entered");
			  delay(1000);
		  }
		  catch (Exception e) 
		  {
			  testDesktop.fail("Unable to enter password");
		  }
		  
		  try 
		  {
			  WebElement loginBtn2 = driver.findElement(By.xpath("//button[@type='submit']"));
			  loginBtn2.click();
			  testDesktop.pass("After entering credentials login button clicked",MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot("1")).build());
		  }
		  catch (Exception e) 
		  {
			  testDesktop.fail("Unable to click login button");
		  }
		
	}
	else if(str.equals("mobile"))
	{
		try 
		{
			  WebElement email= driver.findElement(By.xpath("//input[@name='session[username_or_email]'][@type='text']"));
			  email.sendKeys("kunalwagh1163@gmail.com");
			  testMobile.pass("Email id entered");
			  delay(1000);
		} 
		catch (Exception e) 
		{
			 testMobile.fail("Unable to enter email id");
		}
		 
		try 
		{
			  WebElement password= driver.findElement(By.xpath("//input[@type='password']"));
			  password.sendKeys("1163kunal1727");
			  testMobile.pass("Password entered");
			  delay(1000);
		} 
		catch (Exception e) 
		{
			 testMobile.fail("Unable to enter password");
		}
		
		try 
		{
			  WebElement loginBtn2 = driver.findElement(By.xpath("(//div[@role='button'])[1]"));
			  loginBtn2.click();  
			  testMobile.pass("After entering credentials login button clicked",MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot("2")).build());
		} 
		catch (Exception e) 
		{
			 testMobile.fail("Unable to click login button");
		}
	
	}

  }
  
  @AfterTest
  public void afterTest()
  {
	  delay(5000);
	  driver.quit();
	  extent.flush();
  }
}
