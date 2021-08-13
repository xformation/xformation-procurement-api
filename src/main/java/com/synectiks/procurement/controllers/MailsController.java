package com.synectiks.procurement.controllers;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sun.mail.pop3.POP3Store;
@RestController
@RequestMapping("/api")
public class MailsController {
	
	
     
	@PostMapping("/test")
	 public static void receiveEmail(String pop3Host, String storeType,
			  String user, String password) {  
		 try {    
			   Properties properties = new Properties();  
			   properties.setProperty("mail.store.protocol","pop3s");
			   properties.setProperty("mail.pop3s.host", pop3Host);
			   properties.setProperty("mail.pop3s.port", "995");

			   properties.put("mail.pop3.starttles.enable","true");
			   Session emailSession = Session.getDefaultInstance(properties);  
			   Store store = emailSession.getStore("pop3s");
			   store.connect(pop3Host,user,password);
			   Folder emailFolder = store.getFolder("INBOX");
			   emailFolder.open(Folder.READ_ONLY);
			   Message messages[] = emailFolder.getMessages();  
			   int i = ((messages.length)-1);
			     Message message = messages[i];
			    System.out.println("---------------------------------");  
			    System.out.println("Email Number " + (i + 1));  
			    System.out.println("Subject: " + message.getSubject());  
			    System.out.println("From: " + message.getFrom()[0]);  
			    System.out.println("Text: " + message.getContent().toString());  
			   
//			   emailFolder.close(false);  
//			   emailStore.close();  
			  emailFolder.close(true);  
			  store.close();
		 
		 } catch (NoSuchProviderException e) {e.printStackTrace();
		 }catch (MessagingException e) {e.printStackTrace();
		  } catch (Exception e) {e.printStackTrace();}  
			  
	}
}
