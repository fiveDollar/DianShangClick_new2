package com.windoor.count_save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CountSave implements Serializable{
	public static int count = 0;
	public static String date;
	public static void main(String[] args) {
//		System.out.println(GetNowDate());
		Count cs = new Count();
		cs.count++;
		save(cs);
		System.out.println(get().count);
	}
	
	public static void save(Count cs){
		System.out.println("");
		cs.date=GetNowDate();
		try {
			File f = new File("count/CountSave.out");
			if(!f.exists()){
				f.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream("count/CountSave.out");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(cs);
			oos.close();                       	
		} catch (Exception ex) {  
			ex.printStackTrace();   
		}
	}
	
	public static void count_add(){
		count++;
	}
	public static Count get(){
		Count cs = null ;
		
		File f = new File("count/CountSave.out");
		try {
			if(f.exists()){
				FileInputStream fis = new FileInputStream("count/CountSave.out");
				ObjectInputStream ois = new ObjectInputStream(fis);
				cs = (Count) ois.readObject();
				ois.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
		
		}
		return cs;
	}
	
	public static String GetNowDate(){
	    String temp_str="";   
	    Date dt = new Date();   
	    //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制   
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
	    temp_str=sdf.format(dt);   
	    return temp_str;   
	} 
}
