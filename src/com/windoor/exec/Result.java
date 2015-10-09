package com.windoor.exec;

import java.util.ArrayList;

public class Result {
	String key;
	String host;
	int port;
	int isSuccessed=0;
	String Agent;
	String useTime = "-1";
	String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getIsSuccessed() {
		return isSuccessed;
	}
	public void setIsSuccessed(int isSuccessed) {
		this.isSuccessed = isSuccessed;
	}
	public String getUseTime() {
		return useTime;
	}
	public void setUseTime(String useTime) {
		this.useTime = useTime;
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
	
	public static String getsql(ArrayList<Result> tlist){
		String sql = "insert into tmallresult (`key`,`ip`,`agent`,`useTime`,`isSuccessed`,`type`,`insertDate`) values ";
		for(int i = 0;i<tlist.size();i++){
			sql+="('"+tlist.get(i).getKey()+"','"+tlist.get(i).getHost()+":"+tlist.get(i).getPort()+"','"+tlist.get(i).getAgent()+"','"+tlist.get(i).getUseTime()+"','"+tlist.get(i).getIsSuccessed()+"','"+tlist.get(i).getType()+"',NOW()),";
		}
		return sql.substring(0, sql.length()-1);
	}
}
