package com.he.email;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.sun.mail.util.MailSSLSocketFactory;

public class Email {
	private static String	user		= "1366162208@qq.com";	// 用户名
	private static String	pwd			= "tejktcttbmatjhec";	// 密码
	private static String	sender		= "1366162208@qq.com";	// 发送人
	private static String	receiver	= "1366162208@qq.com";	// 接收人

	public static void main(String[] args) throws Exception {
		MimeMessage msg = createMessage(createDefaultSession(true), "这是主题哟", "这是文本内容哟<img src = \"cid:logo_jpg\">", sender, receiver);
		Transport.send(msg);
	}

	/**
	 *获取默认session
	 */
	public static Session createDefaultSession(boolean debug) throws Exception {
		Properties prop = new Properties();
		prop.setProperty("mail.transport.protocol", "smtp");
		prop.setProperty("mail.smtp.host", "smtp.qq.com");
		prop.setProperty("mail.smtp.port", "465");
		prop.setProperty("mail.smtp.auth", "true");
		 //开启ssl加密（并不是所有的邮箱服务器都需要，但是qq邮箱服务器是必须的）
		MailSSLSocketFactory sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.ssl.socketFactory", sf);
		Session session = Session.getDefaultInstance(prop, new UserAuthenticatorBean(user, pwd));
		session.setDebug(debug);
		return session;
	}

	/**
	 *创建附件
	 */
	public static MimeBodyPart createAttachment(String fileName) throws Exception {
		MimeBodyPart attachmentPart = new MimeBodyPart();
		FileDataSource fds = new FileDataSource(fileName);
		attachmentPart.setDataHandler(new DataHandler(fds));
		attachmentPart.setFileName(MimeUtility.encodeText(fds.getName()));
		return attachmentPart;
	}
	/**
	 *创建富文本内容
	 */
	public static MimeBodyPart createContent(String body, String fileName) throws Exception {
		MimeBodyPart contentBody = new MimeBodyPart();
		MimeMultipart contentMulti = new MimeMultipart("related");
		MimeBodyPart textBody = new MimeBodyPart();
		textBody.setContent(body, "text/html;charset=utf-8");
		contentMulti.addBodyPart(textBody);
		if (fileName != null) {
			MimeBodyPart jpgBody = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(fileName);
			jpgBody.setDataHandler(new DataHandler(fds));
			jpgBody.setContentID("logo_jpg");// 关联图片中的id <img src = \"cid:logo_jpg\">
			contentMulti.addBodyPart(jpgBody);
		}
		contentBody.setContent(contentMulti);
		return contentBody;
	}
	/**
	 *创建消息
	 */
	public static MimeMessage createMessage(Session session, String subject, String content, String from, String to) throws Exception {
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from));
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		msg.setSubject(subject);
		MimeBodyPart attachment01 = createAttachment("E:/img/年终总结.docx");
		MimeBodyPart attachment02 = createAttachment("E:/img/jce_policy-8.zip");
		MimeBodyPart realContent = createContent(content, "E:/img/1.jpg");
		MimeMultipart allPart = new MimeMultipart("mixed");
		allPart.addBodyPart(attachment01);
		allPart.addBodyPart(attachment02);
		allPart.addBodyPart(realContent);
		msg.setContent(allPart);
		msg.saveChanges();
		return msg;
	}

}
