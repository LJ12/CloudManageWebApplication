/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CloudManage;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Administrator
 */
public class SendEmil {
    
    public static void send(String emil,String name,String password) throws MessagingException {
     
      String host = "smtp.qq.com"; 
      final String smtpPort = "465";
      
      Properties prop =new Properties();
      prop.setProperty("mail.smtp.host",host );
      prop.setProperty("mail.transport.protocol", "smtp");
     
      
      prop.setProperty("mail.smtp.port", smtpPort);
      prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
      prop.setProperty("mail.smtp.socketFactory.fallback", "false");
      prop.setProperty("mail.smtp.socketFactory.port", smtpPort);
      prop.setProperty("mail.smtp.auth", "true");
    Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication("2258546101@qq.com", "nxtwvpofenbtdicd");
			}
		};
      Session session=Session.getInstance(prop,auth);
      session.setDebug(true);
      Transport transport= session.getTransport("smtp");
      transport.connect(host,"2258546101@qq.com", "nxtwvpofenbtdicd");
      Message message=createMail(session,emil,name,password);
      transport.sendMessage(message, message.getAllRecipients());
      transport.close();
    }
    public static MimeMessage createMail(Session session,String emil,String name,String password) throws AddressException,MessagingException{
		//创建邮件对象
		MimeMessage mm=new MimeMessage(session);
		//设置发件人
		mm.setFrom(new InternetAddress("2258546101@qq.com"));
		//设置收件人
		mm.setRecipient(Message.RecipientType.TO, new InternetAddress(emil));
		
		mm.setSubject(name+",您好！");
		mm.setContent("您在考勤管理平台中的密码为："+password, "text/html;charset=utf-8");
		return mm;
    }
}