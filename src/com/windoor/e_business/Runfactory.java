package com.windoor.e_business;

import com.windoor.e_business.Rtmall.RunTmall;
import com.windoor.e_business.RtmallMoblie.RunTmallMoblie;
import com.windoor.e_business.RtmallTestb.RunTmallb;
import com.windoor.e_business.RtmallTestc.RunTmallc;
import com.windoor.e_business.alibaba.RunAlibaba;
import com.windoor.e_business.aliexpress.RunAliExpress;
import com.windoor.e_business.guomei.RunGuomei;
import com.windoor.e_business.jingdong.RunJingDong;
import com.windoor.e_business.suning.RunSuning;
import com.windoor.e_business.taobao.RunTaobao;
import com.windoor.e_business.yhd.RunYHD;
import com.windoor.e_business.yixun.RunYixun;

public class Runfactory {
	public static RunAll getRun(String type){
		if(("0").equals(type)){
			return new RunTmall();
		}else if(("1").equals(type)){
			return new RunSuning();
		}else if(("2").equals(type)){
			return new RunYixun();
		}else if(("3").equals(type)){
			return new RunJingDong();
		}else if(("4").equals(type)){
			return new RunYHD();
		}else if(("5").equals(type)){
			return new RunAliExpress();
		}else if(("6").equals(type)){
			return new RunAlibaba();
		}else if(("7").equals(type)){
			return new RunTaobao();
		}else if(("8").equals(type)){
			return new RunGuomei();
		}else if(("33").equals(type)){
			return new RunTmallb();
		}else if(("401").equals(type)){
			return new RunTmallMoblie();
		}else if(("1992").equals(type)){
			return new RunTmallb();
		}else if(("1993").equals(type)){
			return new RunTmallc();
		}
		return null;
	}
}
