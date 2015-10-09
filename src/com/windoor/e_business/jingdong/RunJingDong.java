package com.windoor.e_business.jingdong;

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

public class RunJingDong extends RunAll{
	private static int timeout = 15;
	private static final Dimension POPULAR_DISPLAY_SIZE = new Dimension(1366,
			768);

	public  Result runOne(Task task, String ip, final int port) {
		String word = task.keyword;
		String id = task.id;
		System.out.println(word);
		Result t = new Result();
		t.setKey(word);
		t.setHost(ip);
		t.setPort(port);
		String userAgent = UserAgent.get(UserAgent.CHROME);
		t.setAgent(userAgent);
		t.setType("3");
		WebDriver driver = null;
		final String url = "http://www.jd.com";
		long t1 = System.currentTimeMillis();
		try {
			// Map<String, Object> contentSettings = new HashMap<String,
			// Object>();
			// contentSettings.put("images", 2);
			// Map<String, Object> preferences = new HashMap<String, Object>();
			// preferences
			// .put("profile.default_content_settings", contentSettings);
			//
			//

			DesiredCapabilities dc = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions();
			// options.setExperimentalOption("prefs", preferences);
			options.addArguments("--user-agent=" + userAgent);
			dc.setCapability(ChromeOptions.CAPABILITY, options);
			dc.setJavascriptEnabled(true);
			// DesiredCapabilities dc = DesiredCapabilities.chrome();
			// ChromeOptions options = new ChromeOptions();
			// options.addArguments("--user-agent="+userAgent);
			// dc.setCapability(ChromeOptions.CAPABILITY, options);
			// dc.setJavascriptEnabled(true);
			//
			String proxyStr = ip + ":" + port;
			Proxy proxy = new Proxy();// 配置http、ftp、ssl代理（注：当前版本只支持所有的协议公用http协议，下述代码等同于只配置http）
			proxy.setHttpProxy(proxyStr);
			proxy.setFtpProxy(proxyStr);
			proxy.setSslProxy(proxyStr);
			proxy.setSocksProxy(proxyStr);
			// proxy.setProxyAutoconfigUrl(null);
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
			driver = new RemoteWebDriver(new URL("http://localhost:"+DriverPortConfig.driverPort), dc);
			driver.manage().window().setSize(POPULAR_DISPLAY_SIZE);
			 driver.manage().timeouts()
			 .pageLoadTimeout(timeout, TimeUnit.SECONDS);
			 driver.manage().timeouts()
			 .setScriptTimeout(timeout, TimeUnit.SECONDS);
			long geturlstarttime = System.currentTimeMillis();
			final WebDriver dr = driver;
			Thread thread_start = new Thread(new Runnable() {
				public void run() {// 用一个独立的线程启动WebDriver
					if(port>10000){
						dr.get("http://www.baidu.com");
						dr.findElement(By.id("kw")).sendKeys("京东");
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
				if (count >= 60 && thread_start.isAlive()) {
					thread_start.interrupt();
					throw new RuntimeException("线程获取网页超时");
				} else if (!thread_start.isAlive()) {
					break;
				} else if (count <= 60 && thread_start.isAlive()) {

				}
			}
			sleep(5);
//			driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
			
			Actions act = new Actions(driver);
			WebElement input =driver.findElement(By.id("key"));
			input.sendKeys(word);
			sleep(2);
			act.sendKeys(Keys.ENTER).perform();
//			WebElement button = driver.findElement(By.className("button"));
//			button.click();
//			System.out.println("dianji");
			sleep(5);
			
			System.out.println("获取第一个元素");
			WebElement product =null;
			int amount =0;
			while(product==null){
				try{
					product = driver.findElement(By.xpath("//li[@sku='"+id+"']"));
				}catch(NoSuchElementException e){
						try{
						WebElement next = driver.findElement(By.className("next-disabled"));
						throw new RuntimeException("产品未找到");
						}catch (NoSuchElementException e1){
//							act.sendKeys(Keys.RIGHT).perform();
//							sleep(3);
						}
						
				}
				if(amount>6){
					throw new RuntimeException("产品未找到");
				}
				amount++;
				if(product ==null){
					for(int i=0;i<5;i++){
						act.sendKeys(Keys.END).perform();
						sleep(1);
					}
				}
				try{
					product = driver.findElement(By.xpath("//li[@sku='"+id+"']"));
				}catch(NoSuchElementException e){
						try{
						WebElement next = driver.findElement(By.className("next-disabled"));
						throw new RuntimeException("产品未找到");
						}catch (NoSuchElementException e1){
							act.sendKeys(Keys.RIGHT).perform();
							sleep(3);
						}
						
				}
				
			}
			
			
			System.out.println("获取完成");
			product.click();
			for(String handle: dr.getWindowHandles()){
				dr.switchTo().window(handle);
			}
			sleep(100);
			t.setIsSuccessed(1);
			t.setUseTime((int) ((System.currentTimeMillis() - t1) / 1000)+"");
			System.out.println(word+"                      成功");
			task.success_state=1;
			synchronized (POPULAR_DISPLAY_SIZE) {
				Lancher.count.count++;
				Lancher.count.date = "";
				System.out.println(Lancher.count.count);
				CountSave.save(Lancher.count);
			}
			
			try{
				WebElement comm = driver.findElement(By.id("detail-tab-comm"));
				comm.click();
				for(int i=0;i<50;i++){
					act.sendKeys(Keys.DOWN).perform();
					sleep(1);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			act.sendKeys(Keys.HOME).perform();
			sleep(4);
			try{
				WebElement shop = driver.findElement(By.className("pop-shop-enter")).findElements(By.tagName("a")).get(0);
				shop.click();
				for(String handle: dr.getWindowHandles()){
					dr.switchTo().window(handle);
				}
				for(int i=0;i<30;i++){
					act.sendKeys(Keys.DOWN).perform();
					sleep(1);
				}
				List<WebElement> alist = driver.findElements(By.tagName("a"));
				alist.get(new Random().nextInt(alist.size())).click();
				for(String handle: dr.getWindowHandles()){
					dr.switchTo().window(handle);
				}
				for(int i=0;i<10;i++){
					act.sendKeys(Keys.DOWN).perform();
					sleep(1);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			return t;
		}catch (MalformedURLException e) {
			task.success_state=-1;
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			task.success_state=-1;
			e.printStackTrace();
			System.out.println(word + "NoSuchElementException");
		} catch (TimeoutException e) {
			task.success_state=-1;
			e.printStackTrace();
			System.out.println(word + "TimeoutException");
		} catch (Exception e) {
			task.success_state=-1;
			e.printStackTrace();
			System.out.println(word + "Exception");
		} finally {
			if (driver != null) {
				driver.quit();
			}
		}
		task.success_state=-1;
		return t;
	}
	private static WebElement getProduct(List<WebElement> products,String id){
		for(WebElement p : products){
			String pid =p.getAttribute("sku");
//			System.out.println(pid);
			if(pid!=null&&pid.equals(id)){
//		System.out.println("got it ");
				return p;
			}
		}
		return null;
		
	}
	private static void sleep(int second) {
		try {
			Thread.sleep(second * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
