package com.windoor.e_business.yhd;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.wd.config.DriverPortConfig;
import com.wd.selenium.driver.UserAgent;
import com.windoor.count_save.CountSave;
import com.windoor.e_business.RunAll;
import com.windoor.exec.Result;
import com.windoor.lancher.Lancher;
import com.windoor.task.Task;

public class RunYHD extends RunAll{
	private static int timeout = 30;
	private static final Dimension POPULAR_DISPLAY_SIZE = new Dimension(1366,
			768);

	@SuppressWarnings("finally")
	public Result runOne(Task task, String ip, final int port) {
		String word = task.keyword;
		String id = task.id;
		System.out.println(word);
		Result t = new Result();
		t.setKey(word);
		t.setHost(ip);
		t.setPort(port);
		String userAgent = UserAgent.get(UserAgent.CHROME);
		t.setAgent(userAgent);
		t.setType("4");
		WebDriver driver = null;
		final String url = "http://www.yhd.com";
		long t1 = System.currentTimeMillis();
		try {

			DesiredCapabilities dc = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--user-agent=" + userAgent);
			dc.setCapability(ChromeOptions.CAPABILITY, options);
			dc.setJavascriptEnabled(true);
			
			String proxyStr = ip + ":" + port;
//			String proxyStr = "127.0.0.1" + ":" + 3128;
			Proxy proxy = new Proxy();// 配置http、ftp、ssl代理（注：当前版本只支持所有的协议公用http协议，下述代码等同于只配置http）
			proxy.setHttpProxy(proxyStr);
			proxy.setFtpProxy(proxyStr);
			proxy.setSslProxy(proxyStr);
			proxy.setNoProxy(null);
			proxy.setProxyType(ProxyType.MANUAL);
			proxy.setAutodetect(false);
			dc.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY,
					true);
			dc.setCapability(
					CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC,
					true);
			System.setProperty("http.nonProxyHosts", "localhost");
			dc.setCapability(CapabilityType.PROXY, proxy);
			long driverstarttime = System.currentTimeMillis();
			driver = new RemoteWebDriver(new URL("http://localhost:"+DriverPortConfig.driverPort), dc);
			driver.manage().timeouts()
			 .pageLoadTimeout(timeout, TimeUnit.SECONDS);
			 driver.manage().timeouts()
			 .setScriptTimeout(timeout, TimeUnit.SECONDS);
			long geturlstarttime = System.currentTimeMillis();
			driver.manage().window().setSize(POPULAR_DISPLAY_SIZE);
			final WebDriver dr = driver;
			
			
			Thread thread_start = new Thread(new Runnable() {
				public void run() {// 用一个独立的线程启动WebDriver
					if(port>10000){
						dr.get("http://www.baidu.com");
						dr.findElement(By.id("kw")).sendKeys("一号店");
						Actions act = new Actions(dr);
						act.sendKeys(Keys.ENTER).perform();
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						dr.findElement(By.xpath("//div[@id='1']")).findElement(By.xpath("//h3[@class='t']")).findElement(By.tagName("a")).click();
						
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						dr.close();
						for(String handle: dr.getWindowHandles()){
							dr.switchTo().window(handle);
						}
					}else{
						dr.get(url);
					}
				}
			});
			thread_start.start();
			int count = 0;
			while (true) {
				count++;
				sleep(1);
				if (count >= 30 && thread_start.isAlive()) {
					thread_start.interrupt();
					throw new RuntimeException("线程获取网页超时");
				} else if (!thread_start.isAlive()) {
					break;
				} 
			}
//			sleep(5);
			
			Actions acts = new Actions(driver);
			WebElement input = driver.findElement(By.xpath("//input[@name='keyword' and @id='keyword']"));
			
			sleep(10);
			input.sendKeys(word);
			sleep(3);
			WebElement submitButton = driver.findElement(By.xpath("//button[@class='hd_search_btn']"));
			try{
				submitButton.click();
			}catch(Exception e){
				System.out.println("adfasdf");
			}
			sleep(3);
			
			WebElement p = null;
			int count1 = 0;
			while(p==null){
				count1++;
				if(count1>=15){
					System.out.println("前六页未找到产品");
					break;
				}
				sleep(5);
				try{
					p = driver.findElement(By.xpath("//li[@class='search_item' and @comproid='"+id+"']"));
				}catch(NoSuchElementException e){
					for(int i=0;i<20;i++){
						sleep(1);
						try{
							acts.sendKeys(Keys.PAGE_DOWN).perform();
						}catch(Exception e1){
							
						}
					}
 					acts.sendKeys(Keys.PAGE_UP).perform();
				}
				sleep(5);
				try{
					p = driver.findElement(By.xpath("//div[@class='search_item_box' and @comproid='"+id+"']"));
				}catch(NoSuchElementException e){
					for(int i=0;i<4;i++){
						sleep(1);
						acts.sendKeys(Keys.UP).perform();
					}
					WebElement nextpage = driver.findElement(By.xpath("//a[@class='page_next' and @rel='nofollow']"));
					nextpage.click();
				}
			}
			sleep(5);
			p.click();
			for(String handle: driver.getWindowHandles()){
				driver.switchTo().window(handle);
			} 
			sleep(10);
			for(int i=0;i<100;i++){
				Thread.sleep(new Random().nextInt(2500));
				acts.sendKeys(Keys.DOWN).perform();
			}
			
			t.setIsSuccessed(1);
			t.setUseTime((int) ((System.currentTimeMillis() - t1) / 1000)+"");
			System.out.println(word+"                      成功");
			
			task.success_state=1;
			synchronized (POPULAR_DISPLAY_SIZE) {
				Lancher.count.count++;
				System.out.println(Lancher.count.count);
				CountSave.save(Lancher.count);
			}
			
			WebElement assessment = getAssessment(driver);
			assessment.click();
			acts.sendKeys(Keys.HOME).perform();
			acts.sendKeys(Keys.PAGE_DOWN).perform();
			for(int i=0;i<80;i++){
				Thread.sleep(new Random().nextInt(3000));
				acts.sendKeys(Keys.DOWN).perform();
			}
			acts.sendKeys(Keys.HOME).perform();
//			List<WebElement> products = driver.findElement(By.className("grid_4")).findElements(By.tagName("a"));
//			int r = new Random().nextInt(products.size());
//			acts.moveToElement(products.get(r));
			sleep(3);
//			products.get(r).click();
			sleep(30);
//			for(String handle: driver.getWindowHandles()){
//				driver.switchTo().window(handle);
//			} 
//			for(int i=0;i<80;i++){
//				Thread.sleep(new Random().nextInt(3000));
//				acts.sendKeys(Keys.DOWN).perform();
//			}
			
			return t;
		}catch (MalformedURLException e) {
			e.printStackTrace();
			task.success_state=-1;
			System.out.println(word+"MalformedURLException");
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			task.success_state=-1;
			System.out.println(word + "NoSuchElementException");
		} catch (TimeoutException e) {
			e.printStackTrace();
			task.success_state=-1;
			System.out.println(word + "TimeoutException");
		} catch (Exception e) {
			e.printStackTrace();
			task.success_state=-1;
			System.out.println(word + "Exception");
		} finally {
			if(task.success_state==0){
				task.success_state=-1;	
			}
			if (driver != null) {
				driver.quit();
			}
		}
		return t;
	}
	private static WebElement getAssessment(WebDriver driver){
		List<WebElement> bars = driver.findElement(By.className("des_tab")).findElements(By.tagName("a"));
		for(WebElement e:bars){
			if(e.getText().contains("评价")){
				return e;
			}
		}
		return null;
	}
	private static void sleep(int second) {
		try {
			Thread.sleep(second * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
