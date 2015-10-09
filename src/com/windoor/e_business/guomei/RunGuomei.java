package com.windoor.e_business.guomei;

import java.net.MalformedURLException;
import java.net.URL;
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
/**
 * 
 * @author 张显
 * 进行国美的模拟点击
 *
 */
public class RunGuomei extends RunAll{
	private static int timeout = 15;
	private static final Dimension POPULAR_DISPLAY_SIZE = new Dimension(1366,
			768);

	@SuppressWarnings("finally")
	public  Result runOne(Task task, String ip,
			int port) {
		String word = task.keyword;
		String id = task.id;
		System.out.println(word);
		Result goods = new Result();
		goods.setKey(word);
		goods.setHost(ip);
		goods.setPort(port);
		String userAgent = UserAgent.get(UserAgent.CHROME);
		goods.setAgent(userAgent);
		goods.setType("8");
		WebDriver driver = null;
		final String url = "http://www.gome.com.cn/";
		long t1 = System.currentTimeMillis();
		try {
//			Map<String, Object> contentSettings = new HashMap<String, Object>();
//			contentSettings.put("images", 2);
//			Map<String, Object> preferences = new HashMap<String, Object>();
//			preferences
//					.put("profile.default_content_settings", contentSettings);
			
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
			sleep(3);
			WebElement input = driver.findElement(By.xpath("//input[@type='text' and @id='searchInput']"));
			sleep(1);
			input.sendKeys(word);
			Actions act = new Actions(driver);
			act.sendKeys(Keys.ENTER).perform();
			sleep(5);
			WebElement product =null;
			int pageCount = 0;
			while(product==null){
				pageCount++;
				if(pageCount>=6){
					break;
				}
				try{
					product = driver.findElement(By.xpath("//li[@g-li='"+id+"']"));
				}catch(NoSuchElementException e){
					act.sendKeys(Keys.END).perform();
					sleep(2);
					WebElement next_page = driver.findElement(By.xpath("//a[@class='next']"));
					next_page.click();
					sleep(5);
				}
				try{
					product = driver.findElement(By.xpath("//li[@g-li='"+id+"']"));
				}catch(NoSuchElementException e){
					continue;
				}
			}
			product.click();
			System.out.println(word +"成功");
			goods.setIsSuccessed(1);
			sleep(35);
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
//			e.printStackTrace();
			task.success_state=-1;
			 System.out.println(word+"NoSuchElementException");
		} catch (TimeoutException e) {
//			e.printStackTrace();
			task.success_state=-1;
			System.out.println(word + "TimeoutException");
		} catch (Exception e) {
//			e.printStackTrace();
			task.success_state=-1;
			System.out.println(word + "Exception");

		} finally {
			if (driver != null) {
				driver.quit();
			}
		}
		task.success_state=-1;
		return goods;
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
