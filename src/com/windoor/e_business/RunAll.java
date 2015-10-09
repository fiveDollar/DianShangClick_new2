package com.windoor.e_business;

import com.windoor.exec.Result;
import com.windoor.task.Task;

public abstract class RunAll {
	public abstract Result runOne(Task task, String ip, int port) ;
}
