package com.windoor.taskProtect;

import java.util.Iterator;
import java.util.Map;

import com.windoor.exec.ExecClick;
import com.windoor.exec.ExecClickFactory;
import com.windoor.parameters.Parameter;

/**
 * 监控线程
 * @author Administrator
 * 如果执行的线程中由于某种原因挂起状态，则恢复运行，或重开线程
 */
public class TaskMonitorThread extends Thread {
	public TaskMonitorThread(){
		this.setName("TaskMonitor");
	}
	
	public void run(){
		while (true){
			try{
				Map<Thread, StackTraceElement[]> maps = Thread.getAllStackTraces();
				Iterator it = maps.entrySet().iterator();
				int t = 0;
				int iMaxThread = 0;
				//检查还有多少线程成功运行
	
				while (it.hasNext()){
					Map.Entry<Thread, StackTraceElement[]> entry = (Map.Entry<Thread, StackTraceElement[]>)it.next();
					Thread thread = entry.getKey();
					//如果未运行则重启线程
//					System.out.println(thread.getName());
					if (thread.getName().contains(Parameter.TaskNameHead)||thread.getName().contains("Forward")){
//						ExecClick te = (ExecClick)thread;
//						Log.LogMessage("TaskMonitorThread", "Status: "+te.getName()+" > " + te.getState().name());
						if (!thread.isAlive() 
								|| thread.getState() == Thread.State.TERMINATED){
								//|| System.currentTimeMillis()-te.livetime>10*60*1000){
							System.out.println("come there");
							try{
//								te.stop = true;
								thread.interrupt();
								thread = null;
							}catch(Exception e){}
						} else t ++;
					}
				}
				System.gc();
				//补充线程池
//				int iSuccess = 0;
				for(int i = 0; i < Parameter.BATCH_SIZE - t; i ++){
					try{
						iMaxThread ++;
						ExecClick te =new ExecClick();
			        	te.start();
//			        	iSuccess ++;
					}catch(Exception e){
						e.printStackTrace();
					}
				}
//				if (iSuccess > 0)
//					Log.LogMessage("TaskMonitorThread", "Create new thread [" + iSuccess + "] success!");
//				Log.LogMessage("TaskMonitorThread", "Total thread [" + (t + iSuccess) + "] running!");
			} catch(Exception e){
				e.printStackTrace();
//				Log.LogException("TaskMonitorThread", e, "run thread faild!");
			}
			try{
				Thread.sleep(1000 * 10); 
			}catch(Exception e){}
		}
	}
}
