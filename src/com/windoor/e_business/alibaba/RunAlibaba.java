package com.windoor.e_business.alibaba;

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

public class RunAlibaba extends RunAll{
	private static int timeout = 15;
	private static final Dimension POPULAR_DISPLAY_SIZE = new Dimension(1366,
			768);

	public Result runOne(Task task, String ip, int port) {
		String word = task.keyword;
		String id = task.id;
		System.out.println(word);
		Result t = new Result();
		t.setKey(word);
		t.setHost(ip);
		t.setPort(port);
		String userAgent = UserAgent.get(UserAgent.CHROME);
		t.setAgent(userAgent);
		t.setType("6");
		WebDriver driver = null;
		final String url = "http://www.1688.com";
		long t1 = System.currentTimeMillis();
		try {

			DesiredCapabilities dc = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--user-agent=" + userAgent);
			dc.setCapability(ChromeOptions.CAPABILITY, options);
			dc.setJavascriptEnabled(true);
			String proxyStr = ip + ":" + port;
			Proxy proxy = new Proxy();
			// 配置http、ftp、ssl代理（注：当前版本只支持所有的协议公用tap协议，下述代码等同于只配置http）
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
			driver = new RemoteWebDriver(new URL("http://localhost:"+DriverPortConfig.driverPort), dc);
			driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(timeout, TimeUnit.SECONDS);
			driver.manage().window().setSize(POPULAR_DISPLAY_SIZE);
			long geturlstarttime = System.currentTimeMillis();
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
				if (count >= 60 && thread_start.isAlive()) {
					thread_start.interrupt();
//					throw new RuntimeException("线程获取网页超时");
					break;
				} else if (!thread_start.isAlive()) {
					break;
				} else if (count <= 60 && thread_start.isAlive()) {

				}
			}
			WebElement input = driver.findElement(By.xpath("//input[@id='keywordinput' and @class='empty']"));
			sleep(1);
			input.sendKeys(word);
			sleep(5);
			Actions act = new Actions(driver);
			act.sendKeys(Keys.ENTER).perform();
			sleep(5);
			for(String handle: driver.getWindowHandles()){
				driver.switchTo().window(handle);
			} 
			WebElement product = null;
			int count1=0;
			
			while(product == null){
				count1++;
				if(count1>6)break;
				try{
					product  = driver.findElement(By.xpath("//li[@class='sm-offerShopwindow' and @offerid='"+id+"']"));
				}catch(NoSuchElementException e){
					for(int i=0;i<5;i++){
						Actions acts = new Actions(driver);
//						System.out.println("key down");
						acts.sendKeys(Keys.END).perform();
						sleep(1);
					}
				}
				try{
					product  = driver.findElement(By.xpath("//li[@class='sm-offerShopwindow' and @offerid='"+id+"']"));
				}catch(NoSuchElementException e){
					Actions acts = new Actions(driver);
					acts.sendKeys(Keys.HOME).perform();
					sleep(5);
					WebElement next_page = driver.findElement(By.xpath("//a[@class='page-next' and @rel='nofollow']"));
					next_page.click();
					sleep(5);
				}
				
			}
			product.click();
			sleep(15);
			t.setIsSuccessed(1);
			t.setUseTime((int) ((System.currentTimeMillis() - t1) / 1000)+"");
			task.success_state=1;
			synchronized (POPULAR_DISPLAY_SIZE) {
				Lancher.count.count++;
				System.out.println(Lancher.count.count);
				CountSave.save(Lancher.count);
			}
			System.out.println(word+"                      成功");
			return t;
			
			
		}catch (MalformedURLException e) {
//			e.printStackTrace();
			System.out.println(word+"MalformedURLException");
			task.success_state=-1;
		} catch (NoSuchElementException e) {
			task.success_state=-1;
//			e.printStackTrace();
			System.out.println(word + "NoSuchElementException");
		} catch (TimeoutException e) {
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
		return t;
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
