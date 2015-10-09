package com.windoor.e_business.RtmallMoblie;

import java.util.Random;


public class TestTmall {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		RunTmall.runOne("铅笔", "36637525505", "", 1);
		int sum =0;
		for(int i=0;i<100;i++){
		sum+=new Random().nextInt(100);
		}
		System.out.println(sum/100);
	}

}
