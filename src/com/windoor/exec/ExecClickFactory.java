package com.windoor.exec;

import com.windoor.parameters.Parameter;

public class ExecClickFactory {
	static int id =0;
	public static Thread newExecClick(){
		return new Thread(new ExecClick(),Parameter.TaskNameHead+(++id));
	}

}
