package com.windoor.e_business.taobao;

import com.windoor.task.Task;

public class Tst {

	public static void main(String[] args) {
		RunTaobao taobao = new RunTaobao();
		//nid="41054850422"
		//油印章
		Task t = new Task("橡皮擦 4B 绘图", "1", "8012610785");
		taobao.runOne(t,"222.92.117.87", 31287);
	}

}
