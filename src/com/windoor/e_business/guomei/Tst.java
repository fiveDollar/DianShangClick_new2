package com.windoor.e_business.guomei;

import com.windoor.task.Task;

public class Tst {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//g-li="9125681379"
		Task task = new Task("拖鞋人字", "1", "A0004823020");
		RunGuomei guomei = new RunGuomei();
		guomei.runOne(task, "222.92.117.87", 31287);
		
	}

}
