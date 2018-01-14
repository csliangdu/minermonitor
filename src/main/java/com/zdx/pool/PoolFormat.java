package com.zdx.pool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zdx.common.CommonConst;


public class PoolFormat {

	public static void format(String tickerJsonString, String poolName, PoolStandardFormat x) {
		JSONObject jsonObject = JSON.parseObject(tickerJsonString);
		if (CommonConst.F2POOLNAME.equals(poolName)) {
			f2poolFormat(jsonObject, x);
		}
	}

	private static void f2poolFormat(JSONObject jsonObject, PoolStandardFormat x) {
		//http://api.f2pool.com/eth/0xf3f975ddd64d70d9eab8869e883360e2a71c2413
		x.worker_length_online = jsonObject.getInteger("worker_length_online");
		x.worker_length = jsonObject.getInteger("worker_length");		
		JSONArray tmp = jsonObject.getJSONArray("workers");
		
		for (int i=0; i < tmp.size(); i++){
			String tmp2 = tmp.getString(i);
			tmp2 = tmp2.substring(tmp2.indexOf("[")+1, tmp2.indexOf("]"));
			tmp2 = tmp2.replace("\"", "");
			String[] p = tmp2.split(",");
			if (p.length == 8) {
				String workerName = p[0];
				int workerHash = Integer.valueOf(p[1]);
				if(workerName.length() > 1){
					if (workerHash > 0 ){
						x.workerStatus.put(workerName, true);	
					} else {
						x.workerStatus.put(workerName, false);
					}
				}
			}
		}
	}
}
