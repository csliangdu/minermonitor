package com.zdx.pool;

import java.util.HashMap;
import java.util.Map.Entry;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class PoolStandardFormat {
	public String poolName = "";
	public int worker_length_online = 0;
	public int worker_length = 0;
	public HashMap<String, Boolean> workerStatus = new HashMap<String, Boolean>();

	public PoolStandardFormat(){
	}

	public String toJsonString(){
		return "{\"poolName\":\"" + poolName + 
				"\",\"worker_length_online\":\"" + worker_length_online +
				"\",\"worker_length\":\"" + worker_length + 
				"\",\"workerStatus\":\"" + workerStatus.toString() +
				"\"}";

	}

	public String toMailString(){
		String res = "";
		int i = 0;
		StringBuffer sb = new StringBuffer();
		for (Entry<String, Boolean> x: workerStatus.entrySet()){
			String workerName = x.getKey();
			boolean workerStatus = x.getValue();
			if (!workerName.isEmpty() && !workerStatus){
				sb.append(workerName + "    已死，请重启! \n");
				i = i + 1;
			}
		}

		String tmp = sb.toString();
		if (!tmp.isEmpty()){
			res = tmp + "\n" +
					i + " 台矿机掉线\n" +
					worker_length_online + " 台矿机在线\n" + 
					"抓紧修复，5分钟后继续报警!!!";					
		}
		return res;
	}

	public JSONObject toJson(){
		return JSON.parseObject(this.toJsonString());
	}


}
