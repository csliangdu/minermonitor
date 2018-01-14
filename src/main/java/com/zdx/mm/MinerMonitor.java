package com.zdx.mm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.zdx.common.JsonFormatTool;
import com.zdx.mail.MailConfig;

import io.parallec.core.ParallelClient;
import io.parallec.core.ParallelTaskBuilder;
import io.parallec.core.RequestProtocol;


/** 
 * @author 作者 E-mail: csliangdu@gmail.com
 * @version 创建时间：2018年1月13日 下午6:47:06 
 * 类说明 
 */
public class MinerMonitor {
	private static Logger logger = Logger.getLogger(MinerMonitor.class);
	final static HashMap<String, String> FAILED_TICKER_MAP = new HashMap<String, String>();
	final static HashMap<String, Object> RESPONSE_CONTEXT = new HashMap<String, Object>();
	static ArrayList<String> tickerNamesLeft = new ArrayList<String>();
	
	final static MailConfig mailConf = new MailConfig();
	
	public static void main(String[] args) throws InterruptedException {
		if (args.length == 0) {
			System.err.println("Please input configuration file");
			System.exit(-1);
		}
		String confPath = args[0];
		MinerConfig.loadConfigFromFile(confPath);
		mailConf.init(MinerConfig.mailConf);
		execute();


	}

	public static void execute() throws InterruptedException{
		RESPONSE_CONTEXT.put("hostMap", MinerConfig.poolSymbolMap);
		RESPONSE_CONTEXT.put("pathMap", MinerConfig.pathSymbolMap);
		RESPONSE_CONTEXT.put("mailConf", mailConf);

		while (true){
			oneFullBathWithRetry();
			try {
				Thread.sleep(1000 * 60 * MinerConfig.sleepMinitue);
			} catch (InterruptedException e) {
				logger.info("-----InterruptedException = " + e.getMessage());
			}
		}
	}

	public static void oneFullBathWithoutRetry(){
		oneBatch(MinerConfig.targetHosts, MinerConfig.replaceLists);
	}

	public static void oneFullBathWithRetry(){
		int minError = Integer.MAX_VALUE;
		boolean done = false;
		while (!done){
			List<String> targetHostsLeft = new ArrayList<String>();
			List<List<String>> replaceListsLeft = new ArrayList<List<String>>();
			for (int i = 0; i < MinerConfig.targetHosts.size(); i ++){
				String host = MinerConfig.targetHosts.get(i);
				List<String> urls = MinerConfig.replaceLists.get(i);
				List<String> replaceListsLeft2 = new ArrayList<String>();
				for (String url : urls){
					MinerConfig.tickerNames.add(host + "/" + url);
					String key = host + "/" + url;
					String status = FAILED_TICKER_MAP.get(key);
					if (!FAILED_TICKER_MAP.containsKey(key) || !(status.contains("code") || "OtherError".equals(status)) ){
						replaceListsLeft2.add(url);
						tickerNamesLeft.add(host + "/" + url);
					}
				}
				targetHostsLeft.add(host);
				replaceListsLeft.add(replaceListsLeft2);
			}
			if (targetHostsLeft.isEmpty()){
				done = true;
			}
			if (tickerNamesLeft.size() < minError){
				minError = tickerNamesLeft.size();
			} else if (tickerNamesLeft.size() == minError){
				done = true;//两轮结果一样，
			}
			oneBatch(targetHostsLeft, replaceListsLeft);
			logger.info(tickerNamesLeft.toString());
			try {
				Thread.sleep(1000 * 60 * MinerConfig.sleepMinitue);
				//reset
				tickerNamesLeft = new ArrayList<String>();
			} catch (InterruptedException e) {
				logger.info("-----InterruptedException = " + e.getMessage());
			}
		}
	}

	public static void oneBatch(List<String> targetHostsLeft, List<List<String>> replaceListsLeft){
		ParallelClient pc = new ParallelClient();		
		ParallelTaskBuilder ptb = pc.prepareHttpGet("/$JOB_ID")
				.setProtocol(RequestProtocol.HTTP)
				.setReplaceVarMapToMultipleTarget("JOB_ID", replaceListsLeft, targetHostsLeft)
				.setResponseContext(RESPONSE_CONTEXT);		
		RESPONSE_CONTEXT.put("failedTickerMap", FAILED_TICKER_MAP);

		MinerHandler tpHandler = new MinerHandler(); 
		ptb.execute(tpHandler);
	}
}
