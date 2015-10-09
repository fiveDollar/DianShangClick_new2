package com.wd.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DriverConf {
	private static final Logger logger = Logger.getLogger(DriverConf.class); 
	private static Properties properties = new Properties();
	
	private static final String FILE_PATH = "etc/driver.properties";
	private static final String CHROME_KEY = "chrome";
	private static final String FF_KEY = "ff";
	private static final String CHROME_DRIVER_KEY = "chromedriver";

	public static String chrome;
	public static String ff;
	public static String chromeDriver;

	static {
		try {
			properties.load(new FileInputStream(FILE_PATH));
		} catch (IOException e) {
			e.printStackTrace();
//			logger.error("cannot find the driver config file(" + FILE_PATH + ")");
		}
		chrome = properties.getProperty(CHROME_KEY);
		ff = properties.getProperty(FF_KEY);
		chromeDriver = properties.getProperty(CHROME_DRIVER_KEY);
	}
}
