package com.windoor.e_business.taobao;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
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
/**
 * 
 * @author 张显
 * 进行淘宝的模拟点击
 *
 */
public class RunTaobao extends RunAll{
	private static int timeout = 15;
	private static final Dimension POPULAR_DISPLAY_SIZE = new Dimension(1366,
			768);

	public Result runOne(Task task, String ip,
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
		goods.setType("7");
		WebDriver driver = null;
		final String url = "http://www.taobao.com/";
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
			WebElement input = driver.findElement(By.xpath("//input[@id='q' and @name='q']"));
			input.sendKeys(word);
			sleep(2);
			Actions act = new Actions(driver);
			act.sendKeys(Keys.ENTER).perform();
			sleep(5);
			WebElement product =null;
			int max_page = 6;
			 while(product==null){
				 if(max_page<=0){
					 break;
				 }
				 List<WebElement> products = driver.findElements(By.xpath("//a[@class='pic-link']"));
				 product = getProduct(products, id);
				 if(product!=null){
					 break;
				 }
				 WebElement next_page = driver.findElement(By.xpath("//a[@title='下一页']"));
				 next_page.click();
				 sleep(5);
				 max_page--;
			 }
			 product.click();
			System.out.println(word +"成功");
			goods.setIsSuccessed(1);
			sleep(50);
			goods.setUseTime((int) ((System.currentTimeMillis() - t1) / 1000)+"");
			task.success_state=1;
			synchronized (POPULAR_DISPLAY_SIZE) {
				Lancher.count.count++;
				System.out.println(Lancher.count.count);
				CountSave.save(Lancher.count);
			}
			return goods;
		} catch (MalformedURLException e) {
			task.success_state=-1;
			 System.out.println(word+"MalformedURLException");
		} catch (NoSuchElementException e) {
			task.success_state=-1;
			 System.out.println(word+"NoSuchElementException");
		} catch (TimeoutException e) {
			task.success_state=-1;
			System.out.println(word + "TimeoutException");
		} catch (Exception e) {
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

	private static WebElement getProduct(List<WebElement> we, String id) {
		for (WebElement e : we) {
			try{
				String href = e.getAttribute("href");
				if(href!=null&&href.contains(id)){
					return e;
				}
			}catch(Exception e1){
				
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
