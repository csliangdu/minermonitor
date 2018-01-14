package com.zdx.mm;

import java.util.HashMap;

import java.util.Map;

import org.apache.log4j.Logger;



import com.zdx.common.CommonConst;
import com.zdx.mail.MailConfig;
import com.zdx.mail.MailUtil;
import com.zdx.pool.PoolFormat;
import com.zdx.pool.PoolStandardFormat;

import io.parallec.core.ParallecResponseHandler;
import io.parallec.core.ResponseOnSingleTask;

/** 
 * @author 作者 E-mail: csliangdu@gmail.com
 * @version 创建时间：2018年1月13日 下午6:49:06 
 * 类说明 
 */
public class MinerHandler implements ParallecResponseHandler{
	private static Logger logger = Logger.getLogger(MinerHandler.class);

	@SuppressWarnings("unchecked")
	public void onCompleted(ResponseOnSingleTask res, Map<String, Object> responseContext) {

		HashMap<String, String> failedTickerMap = (HashMap<String, String>) responseContext.get("failedTickerMap");
		Map<String, String> hostMap = (Map<String, String>) responseContext.get("hostMap");
		MailConfig mailConf = (MailConfig) responseContext.get("mailConf");


		String hostName = res.getRequest().getHostUniform();
		String pathName = res.getRequest().getResourcePath();
		String url = hostName + pathName;
		logger.debug("====================Fetch info Begin====================");
		logger.info("Fetch URL= " + url);
		logger.info("response = " + res.getResponseContent());
		logger.debug("====================Fetch info End====================");
		if (res.getError()){
			logger.warn("Parallec Fetch error. API Response = "+res);
			failedTickerMap.put(url,"ParallecError");
			return;
		} else if (res.getStatusCodeInt() != CommonConst.HTTP_OK){
			logger.warn("Parallec Fetch StatusNot200. API Response = "+res);
			failedTickerMap.put(url,"StatusNot200");
			return;
		} else if (res.getStatusCodeInt() == CommonConst.HTTP_OK){
			logger.debug("====================Begin Handle Message====================");
			String host = res.getRequest().getHostUniform();
			String poolName = hostMap.get(host);

			PoolStandardFormat psf = new PoolStandardFormat();			
			psf.poolName = poolName;

			PoolFormat.format(res.getResponseContent(), poolName, psf);

			String mailStr = psf.toMailString();
			if (!mailStr.isEmpty()){
				MailUtil.sendMail(mailConf, mailStr);
			}			
			logger.debug("====================End Handle Message====================");
		} else {
			logger.warn("Parallec Fetch Unkonw error. API Response = "+res);
			failedTickerMap.put(url,"OtherError");
			return;
		}
	}
}
