package com.windoor.proxy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import com.windoor.proxy.MyProxy;

public class GetWebcon {
	private int connetTimeout = 1000 *60*10;
	private int readTimeout = 1000*60*10;
	public String getWebcon(String url, MyProxy proxy) {
		HttpURLConnection connection = null;
		ByteArrayOutputStream bos = null;
		InputStream in = null;
		try {
			if (proxy != null && proxy.check()) {
				connection = (HttpURLConnection) new URL(url)
						.openConnection(new Proxy(Proxy.Type.HTTP,
								new InetSocketAddress(proxy.host, proxy.port)));
			} else {
				connection = (HttpURLConnection) new URL(url).openConnection();
			}
			connection.setReadTimeout(readTimeout);
			connection.setConnectTimeout(connetTimeout);
			in = connection.getInputStream();
			byte[] temp = new byte[1024 * 1024];
			bos = new ByteArrayOutputStream();
			int size = in.read(temp);
			while (size > 0) {
				bos.write(temp, 0, size);
				try {
					size = in.read(temp);
				} catch (Exception e) {
					size = 0;
				}
			}
			String webcon = bos.toString("utf-8");
			return webcon;
		} catch (MalformedURLException e1) {
			return "chaoshi";
		} catch (IOException e) {
			return "chaoshi";
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public String getWebcon(String url) {
		HttpURLConnection connection = null;
		ByteArrayOutputStream bos = null;
		InputStream in = null;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setReadTimeout(readTimeout);
			connection.setConnectTimeout(connetTimeout);
			in = connection.getInputStream();
			byte[] temp = new byte[1024 * 1024];
			bos = new ByteArrayOutputStream();
			int size = in.read(temp);
			while (size > 0) {
				bos.write(temp, 0, size);
				try {
					size = in.read(temp);
				} catch (Exception e) {
					size = 0;
				}
			}
			String webcon = bos.toString("utf-8");
			return webcon;
		} catch (MalformedURLException e1) {
			return "chaoshi";
		} catch (IOException e) {
			return "chaoshi";
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (connection != null) {
				connection.disconnect();
			}
		}

	}

}
