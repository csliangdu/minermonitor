package com.zdx.mail;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/** 
 * @author 作者 E-mail: csliangdu@gmail.com
 * @version 创建时间：2018年1月13日 下午8:01:04 
 * 类说明 
 */
public class MailConfig {
	public String host = "";
	public Integer port = 0;
	public String userName = "";
	public String passWord = "";
	public String emailFrom = "";
	public String timeout = "";
	public String personal = "";
	public String emailTo = "";
	public String subject = "";
	public MailConfig(){

	}

	/**
	 * 初始化
	 * @return 
	 */
	public void init(String conf) {
		JSONObject j1 = JSON.parseObject(conf);
		host = j1.getString("mailHost");
		port = j1.getInteger("mailPort");
		userName = String.valueOf(j1.get("mailUsername"));
		passWord = j1.getString("mailPassword");
		emailFrom = String.valueOf(j1.get("mailFrom"));
		emailTo = String.valueOf(j1.get("mailTo"));
		subject = String.valueOf(j1.get("mailSubject"));
		timeout = String.valueOf(j1.get("mailTimeout"));
		personal = String.valueOf(j1.get("personal"));
	}

	public String toString(){
		return  "host = " + host + "\n" +
				"port = " + port + "\n" +
				"userName = " + userName + "\n" +
				"passWord = " + passWord + "\n" +
				"emailFrom = " + emailFrom + "\n" +
				"timeout = " + timeout + "\n" +
				"personal = " + personal + "\n" +
				"emailTo = " + emailTo + "\n" +
				"subject = " + subject + "\n";
	}
}
