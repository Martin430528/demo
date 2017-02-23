package com.project.utils.mail;

public class TestSendMail {
	public static void main(String[] args) {
		TestSendMail.send_163();
	}

	// 163邮箱
	public static void send_163() {
		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.163.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("18565710426@163.com"); // 实际发送者
		mailInfo.setPassword("LiMin8236");// 您的邮箱密码
		mailInfo.setFromAddress("18565710426@163.com"); // 设置发送人邮箱地址
		mailInfo.setToAddress("294903280@qq.com"); // 设置接受者邮箱地址
		mailInfo.setSubject("设置邮箱标题");
		mailInfo.setContent("设置邮箱内容<b>h6</b>");
		mailInfo.setAttachFileNames(new String[]{"D:/test.epf", "D:/禅道zzt120.txt"});
		// 这个类主要来发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		boolean res1 = sms.sendTextMail(mailInfo); // 发送文体格式
		boolean res2 = sms.sendHtmlMail(mailInfo); // 发送html格式
		System.out.println(res1);
		System.out.println(res2);
	}
}
