package com.zdx.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/** 
 * @author 作者 E-mail: csliangdu@gmail.com
 * @version 创建时间：2018年1月13日 下午7:23:57 
 * 类说明 
 */
public class test {
	public static void main(String[] args){
		String x = "{ worker_length_online: 11, worker_length: 13, paid: 15.030671793448974, stale_hashes_rejected_last_hour: 73014444032, value: 15.081825266336308, balance: 0.05115347288733307, hashes_last_hour: 3582002724864, workers: [ [ \"\", 0, 12884901888, 0, 403726925824, 4294967296, \"2018-01-13T10:54:44.485123Z\", false, ], [ \"450007\", 0, 0, 0, 7430293422080, 124554051584, \"2018-01-13T09:59:22.059818Z\", false, ], [ \"460001\", 114532461, 416611827712, 8589934592, 8392366096384, 244813135872, \"2018-01-13T11:18:38.273218Z\", false, ], [ \"460002\", 147937762, 377957122048, 0, 7864085118976, 154618822656, \"2018-01-13T11:19:35.854186Z\", false, ], [ \"460003\", 71582788, 335007449088, 8589934592, 618475290624, 21474836480, \"2018-01-13T11:18:22.801658Z\", false, ], [ \"460004\", 52494045, 287762808832, 0, 6910602379264, 133143986176, \"2018-01-13T11:19:38.243626Z\", false, ], [ \"460005\", 100215904, 292057776128, 0, 6347961663488, 115964116992, \"2018-01-13T11:19:15.156111Z\", false, ], [ \"460006\", 57266231, 236223201280, 17179869184, 6682969112576, 171798691840, \"2018-01-13T11:19:47.737508Z\", false, ], [ \"460008\", 119304647, 356482285568, 8589934592, 7713761263616, 163208757248, \"2018-01-13T11:19:42.498238Z\", false, ], [ \"460009\", 109760275, 390842023936, 4294967296, 8315056685056, 176093659136, \"2018-01-13T11:19:53.562512Z\", false, ], [ \"460010\", 42949673, 279172874240, 0, 8143257993216, 188978561024, \"2018-01-13T11:19:00.058334Z\", false, ], [ \"460011\", 119304647, 352187318272, 25769803776, 8031588843520, 214748364800, \"2018-01-13T11:19:13.594838Z\", false, ], [ \"460012\", 81127160, 244813135872, 0, 6085968658432, 176093659136, \"2018-01-13T11:18:47.052886Z\", false, ], ], hashes_last_day: 84146999263232, value_last_day: 0.11285584690099194, stale_hashes_rejected_last_day: 1915555414016}";
		JSONObject j1 = JSON.parseObject(x);
		System.out.println(j1.getIntValue("worker_length_online"));
		System.out.println(j1.getIntValue("worker_length"));
		String y = j1.getString("workers");
		JSONArray z = j1.getJSONArray("workers");
		System.out.println(y);
		for (int i=0; i < z.size(); i++){
			System.out.println(z.get(i));
			String tmp = z.getString(i);
			tmp = tmp.substring(tmp.indexOf("[")+1, tmp.indexOf("]"));
			tmp = tmp.replace("\"", "");
			System.out.println(tmp);
			String[] p = tmp.split(",");
			for (int j = 0; j< p.length ; j++){
				System.out.println(p[j]);	
			}
			
			
		}
	}
}
