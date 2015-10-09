package com.windoor.goods;

import java.util.ArrayList;

public class Goods {
	public String key;
	public String host;
	public int port;
	public int isSuccessed=0;
	public String Agent;
	public String type;
	public int useTime;
	
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getUseTime() {
		return useTime;
	}
	public void setUseTime(int useTime) {
		this.useTime = useTime;
	}
	public int getIsSuccessed() {
		return isSuccessed;
	}
	public void setIsSuccessed(int isSuccessed) {
		this.isSuccessed = isSuccessed;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getAgent() {
		return Agent;
	}
	public void setAgent(String agent) {
		Agent = agent;
	}
	
	public static String getsql(ArrayList<Goods> tlist){
		String sql = "insert into tmallresult (`key`,`ip`,`agent`,`useTime`,`isSuccessed`,`insertDate`,`type`) values ";
		for(int i = 0;i<tlist.size();i++){
			sql+="('"+tlist.get(i).getKey()+"','"+tlist.get(i).getHost()+":"+tlist.get(i).getPort()+"','"+tlist.get(i).getAgent()+"','"+tlist.get(i).getUseTime()+"',"+tlist.get(i).getIsSuccessed()+",now(),'"+tlist.get(i).getType()+"'),";
		}
		return sql.substring(0, sql.length()-1);
	}
	
}
