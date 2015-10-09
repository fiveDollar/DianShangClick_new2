package com.windoor.database;

public class Test {

	public static void main(String[] args) {
		Databaselinux db = new Databaselinux();
		db.selectall("select count(*),`key` from tmallresult where useTime!='null' and `insertDate`>='2014-10-16' group by `key`");
	}

}
