package com.windoor.e_business.RtmallMoblie;

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
import com.wd.selenium.driver.MobileUserAgent;
import com.wd.selenium.driver.UserAgent;
import com.windoor.count_save.CountSave;
import com.windoor.e_business.RunAll;
import com.windoor.exec.Result;
import com.windoor.lancher.Lancher;
import com.windoor.task.Task;

public class RunTmallMoblie extends RunAll{
	private static int timeout = 15;
	public  Result runOne(Task task, String ip,
			final int port) {
		String word = task.keyword;
		String id =task.id;
		System.out.println(word);
		Result goods = new Result();
		goods.setKey(word);
		goods.setHost(ip);
		goods.setPort(port);
		int ua =MobileUserAgent.get();
		String userAgent =MobileUserAgent.chrome[ua];
		Dimension POPULAR_DISPLAY_SIZE = new Dimension(MobileUserAgent.DPI[ua][0],
				MobileUserAgent.DPI[ua][1]);
		goods.setAgent(userAgent);
		goods.setType("401");
		WebDriver driver = null;
		final String url = "http://www.tmall.com";
		long t1 = System.currentTimeMillis();
		try {
			DesiredCapabilities dc = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--user-agent=" + userAgent);
			dc.setCapability(ChromeOptions.CAPABILITY, options);
			dc.setJavascriptEnabled(true);
			String proxyStr = ip + ":" + port;
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
			driver.manage().window().setSize(POPULAR_DISPLAY_SIZE);
			 driver.manage().timeouts()
			 .pageLoadTimeout(timeout, TimeUnit.SECONDS);
			 driver.manage().timeouts()
			 .setScriptTimeout(timeout, TimeUnit.SECONDS);
			final WebDriver dr = driver;
			
			
			Thread thread_start = new Thread(new Runnable() {
				public void run() {// 用一个独立的线程启动WebDriver
						dr.get(url);
				}
			});
			thread_start.start();
			int count = 0;
			while (true) {
				count++;
				sleep(1);
				if (count >=60 && thread_start.isAlive()) {
					thread_start.interrupt();
					throw new RuntimeException("线程获取网页超时");
				} else if(!thread_start.isAlive()){
					break;
				}else if(count <= 60 && thread_start.isAlive()){
					
				}
			}
			WebElement element = driver.findElement(By.id("mq"));
			element.sendKeys(word);
			sleep(5);
			Actions acts = new Actions(driver);
			try{
				acts.sendKeys(Keys.ENTER).perform();
			}catch(TimeoutException e){
				
			List<WebElement> buttons = driver
					.findElements(By.tagName("button"));
			WebElement button = getSubmitbutton(buttons);
			try {
				button.click();
			} catch (TimeoutException e1) {
				System.out.println("click time out ");
				button = getSubmitbutton(buttons);
				button.click();
			}
			}
			sleep(5);
			for(int i=0;i<150;i++){
				Thread.sleep(150);
				acts.sendKeys(Keys.DOWN).perform();
			}
			acts.sendKeys(Keys.HOME).perform();
			WebElement product =null;
			try{
				product = driver.findElement(By.xpath("//div[@class='product' and @data-id = '"+id+"']"));
			}catch(NoSuchElementException e){
				e.printStackTrace();
			}
			int count_page =0;
			while (product == null) {
				if(count_page>=11){
					break;
				}
				count_page++;
				acts.sendKeys(Keys.END).perform();
				WebElement next = driver.findElement(By.xpath("//select[@class='pagenav-select']"));
				next.click();
				acts.sendKeys(Keys.DOWN).perform();
				sleep(2);
				acts.sendKeys(Keys.ENTER).perform();;
				sleep(8);
				try{
					product = driver.findElement(By.xpath("//div[@class='product' and @data-id = '"+id+"']"));
				}catch(NoSuchElementException e){
					e.printStackTrace();
				}
			}
			try{
				product = driver.findElement(By.xpath("//div[@class='product' and @data-id = '"+id+"']"));
			}catch(NoSuchElementException e){
				e.printStackTrace();
			}
			product.click();
			sleep(10);	
			for(String handle: driver.getWindowHandles()){
				driver.switchTo().window(handle);
			} 
			for(int i=0;i<100;i++){
				Thread.sleep(new Random().nextInt(2000));
				acts.sendKeys(Keys.DOWN).perform();
			}
			goods.setIsSuccessed(1);
			
			task.success_state=1;
			goods.setUseTime((int) ((System.currentTimeMillis() - t1) / 2000)+"");
			synchronized (POPULAR_DISPLAY_SIZE) {
				Lancher.count.count++;
				System.out.println(Lancher.count.count);
				CountSave.save(Lancher.count);
			}
			acts.sendKeys(Keys.HOME).perform();
			//随机点击
			
			if(new Random().nextInt(2)==1){
				WebElement logo= driver.findElement(By.id("s-shop")).findElement(By.className("go-shop"));
				logo.click();
			}else{
				WebElement logo= driver.findElement(By.id("s-shop")).findElement(By.className("go-shop"));
				logo.click();
			}
			for(String handle: dr.getWindowHandles()){
				dr.switchTo().window(handle);
			}
			sleep(5);
			for(int i=0;i<100;i++){
				Thread.sleep(new Random().nextInt(2000));
				acts.sendKeys(Keys.DOWN).perform();
			}
			List<WebElement> products = driver.findElements(By.tagName("a"));
			products.get(new Random().nextInt(products.size())).click();
			sleep(15);
			return goods;
		} catch (MalformedURLException e) {
			task.success_state=-1;
			 System.out.println(word+"MalformedURLException");
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			task.success_state=-1;
			 System.out.println(word+"NoSuchElementException");
		} catch (TimeoutException e) {
			task.success_state=-1;
			System.out.println(word + "TimeoutException");
		} catch (Exception e) {
			task.success_state=-1;
			e.printStackTrace();
			System.out.println(word + "Exception");
		} finally {
			if(task.success_state==0){
				task.success_state=-1;	
			}
			if (driver != null) {
				driver.quit();
			}
		}
		return goods;
	}


	private static WebElement getSubmitbutton(List<WebElement> buttons) {
		for (WebElement e : buttons) {
			if ((e.getAttribute("type").trim()).equals("submit")) {
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
	//找到评价find assenssment
	public static WebElement getAssessment(List<WebElement> bars){
		for(WebElement w : bars){
			if(w.getText().contains("评价")){
				return w;
			}
		}
		return null;
	}
	
	public static WebElement getRecord(List<WebElement> bars){
		for(WebElement w : bars){
			if(w.getText().contains("记录")){
				return w;
			}
		}
		return null;
	}
}
