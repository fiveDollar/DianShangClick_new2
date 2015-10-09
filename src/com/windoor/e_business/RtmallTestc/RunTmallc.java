package com.windoor.e_business.RtmallTestc;

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
import com.windoor.goods.Goods;
import com.windoor.lancher.Lancher;
import com.windoor.task.Task;

public class RunTmallc extends RunAll{
	private static int timeout = 15;
	private static final Dimension POPULAR_DISPLAY_SIZE = new Dimension(1366,
			768);

	public  Result runOne(Task task, String ip,
			int port) {
		String word = task.keyword;
		String id =task.id;
		System.out.println(word);
		Result goods = new Result();
		goods.setKey(word);
		goods.setHost(ip);
		goods.setPort(port);
		String userAgent = UserAgent.get(UserAgent.CHROME);
//		String userAgent = UserAgent.get(UserAgent.CHROME)+"proxy:"+ip+":"+port;
		goods.setAgent(userAgent);
		goods.setType("1993");
		WebDriver driver = null;
		final String url = "http://www.tmall.com";
		long t1 = System.currentTimeMillis();
		try {
//			Map<String, Object> contentSettings = new HashMap<String, Object>();
//			contentSettings.put("images", 2);
//			Map<String, Object> preferences = new HashMap<String, Object>();
//			preferences
//					.put("profile.default_content_settings", contentSettings);
//			
//			
			
			DesiredCapabilities dc = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions();
//			options.setExperimentalOption("prefs", preferences);
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
//			String proxyStr = "127.0.0.1" + ":" + 3128;
			Proxy proxy = new Proxy();// 配置http、ftp、ssl代理（注：当前版本只支持所有的协议公用http协议，下述代码等同于只配置http）
			proxy.setHttpProxy(proxyStr);
			proxy.setFtpProxy(proxyStr);
			proxy.setSslProxy(proxyStr);
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
			long driverstarttime = System.currentTimeMillis();
			driver = new RemoteWebDriver(new URL("http://localhost:"+DriverPortConfig.driverPort), dc);
			driver.manage().window().setSize(POPULAR_DISPLAY_SIZE);
//			synchronized (LauncherlinuxTmall.l) {
//				LauncherlinuxTmall.l.logMessage(word+",启动driver用时,"+(System.currentTimeMillis()-driverstarttime));
//			}
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
			List<WebElement> we = driver.findElements(By.className("product"));
			WebElement product = getProduct(we, id);
			
			int count_page =0;
			while (product == null) {
				if(count_page>=11){
					break;
				}
				count_page++;
				WebElement next_button = driver.findElement(By.xpath("//a[@class='ui-page-s-next' and @title='下一页']"));
				next_button.click();
				sleep(5);
				we = driver.findElements(By.className("product"));
				product = getProduct(we, id);
			}
			we = driver.findElements(By.className("product"));
			product = getProduct(we, id);
			product.click();
			sleep(10);	
			for(String handle: driver.getWindowHandles()){
				driver.switchTo().window(handle);
			} 
			for(int i=0;i<10;i++){
				acts.sendKeys(Keys.END).perform();
				sleep(1);
			}
			sleep(20);
			System.out.println(driver.getTitle());
			List<WebElement> relate_products = driver.findElement(By.xpath("//div[@class='bd']")).findElements(By.xpath("//div[@class='baobei']"));
			Random r = new Random();
			System.out.println("nnnnnnn"+relate_products.size());
			WebElement rp = relate_products.get(r.nextInt(relate_products.size()));
			rp.click();
			sleep(20);
			goods.setIsSuccessed(1);
			sleep(40);
			goods.setUseTime((int) ((System.currentTimeMillis() - t1) / 1000)+"");
			task.success_state=1;
			synchronized (POPULAR_DISPLAY_SIZE) {
				Lancher.count.count++;
				System.out.println(Lancher.count.count);
				CountSave.save(Lancher.count);
			}
			return goods;
		} catch (MalformedURLException e) {
//			e.printStackTrace();
			task.success_state=-1;
			 System.out.println(word+"MalformedURLException");
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			task.success_state=-1;
			 System.out.println(word+"NoSuchElementException");
		} catch (TimeoutException e) {
//			e.printStackTrace();
			task.success_state=-1;
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
		return goods;
	}

	private static WebElement getProduct(List<WebElement> we, String id) {
		for (WebElement e : we) {
			if ((e.getAttribute("data-id").trim()).equals(id)) {
				return e;
			}
		}
		return null;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void sleepRandom(long millis, int base) {
		long time = (long) (Math.random() * millis + base);
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
