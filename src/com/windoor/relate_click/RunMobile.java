package com.windoor.relate_click;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.windoor.count_save.CountSave;
import com.windoor.exec.Result;
import com.windoor.lancher.Lancher;
import com.windoor.parameters.Parameter;
import com.windoor.task.Task;

public class RunMobile {
	
	private static int timeout = 30;
	private static final Dimension POPULAR_DISPLAY_SIZE
    = new Dimension(1366, 768);
	public static Result RunOne(Task task, String ip, int port){
		String word = task.keyword;
		Result t= new Result();
		t.setKey(word);
		t.setHost(ip);
		t.setPort(port);		
		String userAgent = MobileAgent.get(MobileAgent.CHROME);
		t.setAgent(userAgent);
		WebDriver driver = null;
		final String url = "http://www.baidu.com";
		long startTime = System.currentTimeMillis();
		try{
			
			DesiredCapabilities dc = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--user-agent=" + userAgent);
			dc.setCapability(ChromeOptions.CAPABILITY, options);
			dc.setJavascriptEnabled(false);
			
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
//			dc.setCapability(CapabilityType.PROXY, proxy);
			driver = new RemoteWebDriver(new URL("http://localhost:"+Parameter.ChromeDriverPort), dc);
			
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
			Actions act = new Actions(driver);
			
			WebElement e=driver.findElement(By.id("index-kw"));
			e.sendKeys("前程无忧招聘");
			act.sendKeys(Keys.ENTER).perform();
			sleep(3);
			List<WebElement> results = driver.findElements(By.xpath("//div[@class='result']"));
			WebElement r =getResult(results);
			act.sendKeys(Keys.END).perform();
			if(r!=null){
				r.click();
			}
			Thread.sleep(10000);
			t.setUseTime((System.currentTimeMillis()-startTime)/1000+"");
			task.success_state=1;
			t.setIsSuccessed(1);
			System.out.println(word+"成功");
			synchronized (POPULAR_DISPLAY_SIZE) {
				Lancher.count.count++;
				Lancher.count.date = "";
				System.out.println(Lancher.count.count);
				CountSave.save(Lancher.count);
			}
			return t;
		} catch (MalformedURLException e) {
//			e.printStackTrace();
			task.success_state=-1;
			System.out.println(word+"MalformedURLException");
		} catch (NoSuchElementException e) {
			task.success_state=-1;
			System.out.println(word+"NoSuchElementException");
		}  catch (Exception e) {
			task.success_state=-1;
			System.out.println(word+"Exception");
//			e.printStackTrace();
		} finally {
			if (driver != null) {
				driver.quit();
			}
		}
		task.success_state=-1;
		return t;
	}
	
	private static WebElement getResult(List<WebElement> rs){
		List<WebElement> temp = new ArrayList<WebElement>();
		for(WebElement r:rs){
			if(r.getText().contains("51job")){
				temp.add(r);
			}
		}
		Random r=new Random();
//		System.out.println(temp.size());
		if(temp.size()>0){
			
			return temp.get(r.nextInt(temp.size()));
		}
		
		return null;
	}
	private static void sleep(int second){
		try {
			Thread.sleep(second*1000);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
