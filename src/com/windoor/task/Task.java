package com.windoor.task;

public class Task {
	
	public String keyword;
	public String id;
	public int success_state=0;
	public String type;
	public int execTimes = 0;
	public int task_id = -1;
	
	public Task(String keyword,String type,String id){
		this.type = type;
		this.id = id;
		this.keyword = keyword;
	}
	public Task() {
		
	}
	
}
