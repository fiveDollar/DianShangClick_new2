package com.windoor.proxy;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;


public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
            URL url = new URL("http://vv.56.com/vv/?id=MTMzOTMwNjE0&pct=1&user_id=&cpm=-4&opera_id=-4&rela_opera=-4&v_userid=qq-zyqdglcprl&cid=11&totaltime=18&from=pc&callback=jsonp_flv");
            // 创建代理服务器
            //222.92.117.87 1080
            InetSocketAddress addr = new InetSocketAddress("222.92.117.87",
            		1080);
//            InetSocketAddress addr = new InetSocketAddress("42.57.211.62",
//            		35143);
             Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr); // Socket 代理
//            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr); // http 代理
            // 如果我们知道代理server的名字, 可以直接使用
            // 结束
            URLConnection conn = url.openConnection(proxy);
            InputStream in = conn.getInputStream();
            // InputStream in = url.openStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
