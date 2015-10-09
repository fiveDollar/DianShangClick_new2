package com.wd.selenium.driver;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.chrome.ChromeDriverService;

import com.google.common.collect.ImmutableMap;
import com.wd.config.DriverConf;

public class CDriverService {
	private static ScheduledExecutorService executor;
	private static ChromeDriverService chromeDriverService;

	public static void start() {
		executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "start chrome driver");
			}
		});
		executor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				ChromeDriverService tmp = new ChromeDriverService.Builder()
				.usingDriverExecutable(new File(DriverConf.chromeDriver))
				.usingPort(12345)
				.withEnvironment(ImmutableMap.of("DISPLAY",":1"))
				.build();
				try {
					tmp.start();
					chromeDriverService = tmp;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 0, 20, TimeUnit.SECONDS);	
	}

	public static void stop() {
		if(chromeDriverService != null) {
			chromeDriverService.stop();
		}
		if (executor != null) {
			executor.shutdown();
		}
	}
}
