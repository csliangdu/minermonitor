package com.zdx.mm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zdx.common.FileIO;

/** 
 * @author 作者 E-mail: csliangdu@gmail.com
 * @version 创建时间：2018年1月13日 下午6:49:15 
 * 类说明 
 */
public class MinerConfig {
	private static Logger logger = Logger.getLogger(MinerConfig.class);
	public static HashMap<String, ArrayList<String>>  poolTickerListMap = new HashMap<String, ArrayList<String>>();

	public static HashMap<String, String> poolSymbolMap = new HashMap<String, String>();

	public static HashMap<String, String> pathSymbolMap = new HashMap<String, String>();

	public static List<String> targetHosts = new ArrayList<String>();

	public static List<List<String>> replaceLists = new ArrayList<List<String>>();

	public static ArrayList<String> tickerNames = new ArrayList<String>();

	public static String producerGroup = "";

	public static String mailConf;
	public static int sleepMinitue = 0;
	public MinerConfig(){

	}

	public static void loadConfigFromFile(String confFilePath){
		String fileContent = FileIO.readFile(confFilePath);
		if (fileContent.isEmpty()){
			logger.error("Producer Config File Is Empty, Please check it. confFilePath=" +confFilePath );
		}
		JSONObject j1 = JSON.parseObject(fileContent);
		sleepMinitue = j1.getIntValue("SleepMinitue");
		producerGroup = String.valueOf(j1.get("ProducerGroup"));
		JSONArray tmp = j1.getJSONArray("MailConf");
		if (tmp.size() == 1){
			mailConf = tmp.getString(0);
		}

		String tickerConf = String.valueOf(j1.get("TickerConf"));
		ArrayList<String> poolTickerList = JSON.parseObject(tickerConf, new TypeReference<ArrayList<String>>(){});
		for (String e : poolTickerList){
			ArrayList<String> epList = new ArrayList<String>();
			JSONObject j2 = JSON.parseObject(e);
			String poolURL = String.valueOf(j2.get("poolURL"));
			String poolName = String.valueOf(j2.get("poolName"));
			targetHosts.add(poolURL);
			poolSymbolMap.put(poolURL, poolName);
			String tickerData = String.valueOf(j2.get("endpoints"));
			ArrayList<String> poolTickerList2 = JSON.parseObject(tickerData, new TypeReference<ArrayList<String>>(){});
			for (String e2 : poolTickerList2){
				JSONObject j3 = JSON.parseObject(e2);
				String workerGroup = String.valueOf(j3.get("workerGroup"));
				String workerAddress = String.valueOf(j3.get("workerAddress"));
				pathSymbolMap.put(workerGroup, workerAddress);
				epList.add(workerAddress);
			}
			poolTickerListMap.put(poolURL, epList);
			replaceLists.add(epList);			
		}
		for (int i = 0; i < targetHosts.size(); i ++){
			String host = targetHosts.get(i);
			List<String> urls = replaceLists.get(i);
			for (String url : urls){
				tickerNames.add(host + "/" + url);
			}
		}
		logger.info(toLogString());
	}

	public static String toLogString(){

		String s1 = poolSymbolMap.toString();

		String s2 = pathSymbolMap.toString();

		String s3 = poolTickerListMap.toString();

		String s4 = targetHosts.toString();

		String s5 = replaceLists.toString();

		String x = "producerGroup = " + producerGroup + "\n" +
				"poolSymbolMap = " + s1 + "\n" +
				"pathSymbolMap = " + s2 + "\n" +
				"poolTickerListMap = " + s3 + "\n" +
				"targetHosts = " + s4 + "\n" +
				"replaceLists = " + s5 + "\n" ;
		return x;
	}
}
