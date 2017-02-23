package com.project.utils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * 
 * 创建日期:2009-3-26
 * 
 * @author liuding
 */
public class JavaMail {
	/**
	 * 邮件服务器
	 */
	private String receivehost;
	/**
	 * 发送帐户(发件人)
	 */
	private String from;
	/**
	 * 显示名称(发件人别名)
	 */
	private String displayName;
	/**
	 * 发送密码
	 */
	public String passwd;
	/**
	 * 收件人地址
	 */
	public String to;

	/**
	 * 
	 * 发送email
	 * 
	 * @param from
	 *            发件人
	 * 
	 * @param to
	 *            　收件人
	 * 
	 * @param title
	 *            标题
	 * 
	 * @param mailcontent
	 *            内容
	 * 
	 * @param　host 发送邮件服务器(SMTP)
	 * @trun 发送成功返回true否则返回false
	 */

	public boolean sendMail(String title, String mailcontent) {
		boolean flg = false;
		Properties props = System.getProperties();// 得到系统属性

		props.put("mail.smtp.host", receivehost);// 压入接收邮件服务器(SMTP)

		props.put("mail.smtp.auth", "true"); // 这样才能通过验证

		MyAuthenticator myauth = new MyAuthenticator(from, passwd);

		Session session = Session.getDefaultInstance(props, myauth);// 获取默认会话

		try {

			MimeMessage message = new MimeMessage(session); // 建立邮件

			try {
				// 设置发件人及显示名称
				message.setFrom(new InternetAddress(from, displayName));
			} catch (UnsupportedEncodingException e) {
				flg = false;
				e.printStackTrace();
			}
			// 多收件人用";"分隔
			String mailList[] = to.split(";");
			for (int i = 0; i < mailList.length; i++) {
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(mailList[i]));
			}

			message.setSubject(title);// 加入邮件标题

			message.setContent(mailcontent, "text/html;charset=utf-8");// 加入邮件内容

			message.setSentDate(new Date());

			Transport.send(message);// 发送邮件

			flg = true;

		} catch (AuthenticationFailedException e) {
			flg = false;
			e.printStackTrace();
			System.out.println("邮件发送失败！错误原因：\n" + "身份验证错误!");
		} catch (MessagingException mex) {
			flg = false;
			mex.printStackTrace();
			System.out.println("邮件发送失败！错误原因：\n" + mex.getMessage());
		}
		return flg;
	}

	/**
	 * 
	 * 发送附件
	 * 
	 * @param title
	 *            标题
	 * 
	 * @param mailconent
	 *            文体内容
	 * 
	 * @param filenames
	 *            本地文件
	 */

	public void sendAppendix(String title, String mailconent, String filenames) {

		Properties props = System.getProperties();// 得到系统属性

		props.put("mail.smtp.host", this.receivehost);// 压入接收邮件服务器(SMTP)

		props.put("mail.smtp.auth", "true"); // 这样才能通过验证

		MyAuthenticator myauth = new MyAuthenticator(from, passwd);

		Session session = Session.getDefaultInstance(props, myauth);// 获取默认会话

		try {

			MimeMessage msg = new MimeMessage(session);// 建立邮件

			try {
				msg.setFrom(new InternetAddress(from, displayName));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			InternetAddress[] address = { new InternetAddress(to) };

			msg.setRecipients(Message.RecipientType.TO, address);

			msg.setSubject(title);

			MimeBodyPart mbp1 = new MimeBodyPart();// 用于存放文本内容

			mbp1.setContent(mailconent, "text/html;charset=utf-8");

			MimeMultipart mimemultipart = new MimeMultipart();

			mimemultipart.addBodyPart(mbp1);// 加入文字内容

			if (this.isFine(filenames))
				return;
			String[] filelist = filenames.split(",");

			MimeBodyPart mbp2;// 用于存放附件内容

			FileDataSource filedatasource;

			for (int i = 0; i < filelist.length; i++) {
				// 逐个加入附件内容
				mbp2 = new MimeBodyPart();

				filedatasource = new FileDataSource(filelist[i]);

				mbp2.setDataHandler(new DataHandler(filedatasource));

				mbp2.setFileName(filedatasource.getName());

				mimemultipart.addBodyPart(mbp2);

			}
			// 压入附件内容
			msg.setContent(mimemultipart);

			// 压入当前时间
			msg.setSentDate(new Date());

			// 发送邮件
			Transport.send(msg);
		} catch (AuthenticationFailedException e) {
			e.printStackTrace();
			System.out.println("邮件发送失败！错误原因：\n" + "身份验证错误!");
		} catch (MessagingException mex) {
			mex.printStackTrace();
			System.out.println("邮件发送失败！错误原因：\n" + mex.getMessage());
		}
	}

	/**
	 * 
	 * 接收邮件
	 * 
	 * @param popServer
	 * 
	 * @param popUser
	 * 
	 * @param popPwd
	 */

	// public void receiveMail() {
	//
	// Store store = null;
	//
	// Folder folder = null;
	//
	// try {
	// Properties props = System.getProperties();// 得到系统属性
	//
	// Session session = Session.getDefaultInstance(props, null);// 获取默认会话
	//
	// store = session.getStore("pop3");// 使用POP3会话机制，连接服务器
	// store.connect(receivehost, from, passwd);// 压入接收邮件服务器，用户，密码
	// folder = store.getDefaultFolder();// 获取默认文件夹
	//
	// if (folder == null)
	// throw new Exception("No default folder");
	//
	// folder = folder.getFolder("INBOX");// 链接收件箱
	//
	// if (folder == null)
	// throw new Exception("No POP3 INBOX");
	//
	// folder.open(Folder.READ_ONLY);// 使用只读方式打开收件箱
	//
	// Message[] msgs = folder.getMessages();// 得到文件夹信息，获取邮件列表
	// for (int msgNum = 0; msgNum < msgs.length; msgNum++) {
	//
	// printMessage(msgs[msgNum]);
	//
	// }
	//
	// }
	//
	// catch (Exception ex) {
	//
	// ex.printStackTrace();
	// } finally {// 释放资源
	//
	// try {
	//
	// if (folder != null)
	// folder.close(false);
	//
	// if (store != null)
	// store.close();
	//
	// }
	//
	// catch (Exception ex2) {
	//
	// ex2.printStackTrace();
	//
	// }
	//
	// }
	//
	// }

	/**
	 * 
	 * 打印邮件内容
	 * 
	 * @param message
	 */

	// public void printMessage(Message message) {
	//
	// try {
	//
	// String from = ((InternetAddress) message.getFrom()[0])
	// .getPersonal();// 获得发送邮件地址
	//
	// if (from == null)
	// from = ((InternetAddress) message.getFrom()[0]).getAddress();
	//
	// System.out.println("FROM: " + from);
	//
	// String subject = message.getSubject();// 获取标题
	//
	// System.out.println("SUBJECT: " + subject);
	//
	// Part messagePart = message;// 获取信息对象
	//
	// Object content = messagePart.getContent();// 附件
	//
	// if (content instanceof Multipart) {
	//
	// messagePart = ((Multipart) content).getBodyPart(0);
	//
	// System.out.println("[ Multipart Message ]");
	//
	// }
	// String contentType = messagePart.getContentType();// 获取content类型
	// System.out.println("CONTENT:" + contentType);
	//
	// // 如果邮件内容是纯文本或者是HTML，那么打印出信息
	// if (contentType.startsWith("text/plain")
	// || contentType.startsWith("text/html")) {
	//
	// InputStream is = messagePart.getInputStream();
	//
	// BufferedReader br = new BufferedReader(
	// new InputStreamReader(is));
	//
	// String thisLine = "";
	// while ((thisLine = br.readLine()) != null) {
	// thisLine = br.readLine();
	// }
	//
	//
	// }
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// }

	/**
	 * 
	 * 说明:查看字符串是否合法
	 * 
	 * @param str
	 * @return
	 */
	public boolean isFine(String str) {

		return str != null && str.length() > 0;

	}

	/**
	 * 
	 * 创建日期:2009-3-26
	 * 
	 * @author 蒲刚敏 说明: 发送邮件验证发送人的用户名和密码
	 */
	static class MyAuthenticator extends javax.mail.Authenticator {
		private String username, password;

		public MyAuthenticator(String username, String password) {
			this.username = username;
			this.password = password;
		}

		protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
			return new javax.mail.PasswordAuthentication(username, password);
		}
	}

	public String getReceivehost() {
		return receivehost;
	}

	/**
	 * 
	 * 说明: 设置邮件服务器
	 * 
	 * @param receivehost
	 */
	public void setReceivehost(String receivehost) {
		this.receivehost = receivehost;
	}

	public String getFrom() {
		return from;
	}

	/**
	 * 
	 * 说明: 设置发件人
	 * 
	 * @param from
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	public String getPasswd() {
		return passwd;
	}

	/**
	 * 
	 * 说明: 发件人账户密码
	 * 
	 * @param passwd
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getTo() {
		return to;
	}

	/**
	 * 
	 * 说明:设置接受邮件人(多个地址用";"分隔)
	 * 
	 * @param to
	 */
	public void setTo(String to) {
		this.to = to;
	}

	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 
	 * 说明: 设置发件人显示别名
	 * 
	 * @param displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	// 测试main函数
	public static void main(String[] args) {

	}
}