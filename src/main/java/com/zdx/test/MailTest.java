package com.zdx.test;


import com.zdx.mail.MailConfig;
import com.zdx.mail.MailUtil;
import com.zdx.mm.MinerConfig;

/** 
 * @author 作者 E-mail: csliangdu@gmail.com
 * @version 创建时间：2018年1月13日 下午9:22:37 
 * 类说明 
 */
public class MailTest {
	public static void main(String[] args){
		test1(args);
	}
	
	public static void test1(String[] args){
		String confPath = args[0];
		MinerConfig.loadConfigFromFile(confPath);
		MailConfig mailConf = new MailConfig();
		mailConf.init(MinerConfig.mailConf);

		String mess = "{\"poolName\":\"f2pool\",\"worker_length_online\":\"10\",\"worker_length\":\"13\",\"workerStatus\":\"{\"\"=false, \"450007\"=false, \"460002\"=true, \"460001\"=true, \"460012\"=true, \"460011\"=true, \"460010\"=true, \"460006\"=true, \"460005\"=true, \"460004\"=true, \"460003\"=true, \"460009\"=true, \"460008\"=true}\"}";
		System.out.println(mailConf.toString());
		MailUtil.sendMail(mailConf,  mess);
	}
}
