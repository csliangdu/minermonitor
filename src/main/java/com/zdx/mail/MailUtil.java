package com.zdx.mail;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Properties;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/** 
 * @author 作者 E-mail: csliangdu@gmail.com
 * @version 创建时间：2018年1月13日 下午8:00:36 
 * 类说明 
 */

@Component
public class MailUtil {
	private static Logger logger = Logger.getLogger(MailUtil.class);
	@SuppressWarnings("restriction")
	public static void sendMail(MailConfig mailConf, String content){
		if (mailConf.emailTo.contains(",")){
			String[] emailToUsers = mailConf.emailTo.split(",");
			for (String x: emailToUsers){
				try {
					// 1.创建一个程序与邮件服务器会话对象 Session
					Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());  
					final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";  
					Properties props = System.getProperties();
					props.setProperty("mail.smtp.host", mailConf.host);
					props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
					props.setProperty("mail.smtp.socketFactory.fallback", "false");
					props.setProperty("mail.smtp.port", "" + mailConf.port);
					props.setProperty("mail.smtp.socketFactory.port", "" + mailConf.port);
					props.setProperty("mail.smtp.auth", "true");

					//props.setProperty("mail.debug", "true");
					props.setProperty("mail.smtp.timeout","" + mailConf.timeout);
					// 验证账号及密码，密码需要是第三方授权码
					Authenticator auth = new Authenticator() {
						public PasswordAuthentication getPasswordAuthentication(){
							return new PasswordAuthentication("im.duliang@qq.com", "byxnkrczzlbsbifd");
						}
					};
					Session session = Session.getInstance(props, auth);
					// 2.创建一个Message，它相当于是邮件内容
					Message message = new MimeMessage(session);
					// 设置发送者
					message.setFrom(new InternetAddress(mailConf.emailFrom, mailConf.personal));
					// 设置主题
					message.setSubject(mailConf.subject);
					// 设置内容
					message.setContent(content, "text/html;charset=utf-8");


					// 设置发送方式与接收者
					message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(x));

					// 3.创建 Transport用于将邮件发送
					Transport.send(message);
				} catch (AddressException e) {
					logger.info("-----AddressException = " + e.getMessage());
				} catch (MessagingException e) {
					logger.info("-----MessagingException = " + e.getMessage());
				} catch (UnsupportedEncodingException e) {
					logger.info("-----UnsupportedEncodingException = " + e.getMessage());
				}
				try {
					Thread.sleep(1000 * 30);
				} catch (InterruptedException e) {
					logger.info("-----InterruptedException = " + e.getMessage());
					
				}
			}
		}
	}
}
