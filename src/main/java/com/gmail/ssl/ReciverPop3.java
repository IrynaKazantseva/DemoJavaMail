package com.gmail.ssl;

import java.io.IOException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;



public class ReciverPop3 {

	final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private Properties pop3Props;
	private URLName url;

	public ReciverPop3(String username, String password) {
		pop3Props = new Properties();

		pop3Props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
		pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
		pop3Props.setProperty("mail.pop3.port", "995");
		pop3Props.setProperty("mail.pop3.socketFactory.port", "995");
		url = new URLName("pop3", "pop.gmail.com", 955, "", username, password);
	}

	public void recive(String folderInbox) throws MessagingException, IOException {

		Session session = Session.getInstance(pop3Props, null);
		Store store = session.getStore(url);
		store.connect();

		Folder folder = store.getFolder(folderInbox);
		
		try {

			folder.open(Folder.READ_WRITE);

		} catch (MessagingException ex) {

			folder.open(Folder.READ_ONLY);

		}

		Message[] messages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

		for (int i = 0; i < messages.length; i++) {
			String subject = messages[i].getSubject().toString().toLowerCase().trim(); // Получение темы письма
																						
			String letterConfirmationSubject = "your order has been received"; // Тема письма которое содержит сonfirmation number
																				

			// Выбор нужного письма
			if (!subject.equals(letterConfirmationSubject)) {
				System.err.println("Сообщение пропущеноooo, т.к. не является активационным письмом.......");

				System.out.println(subject);
				System.out.println("1" + messages.length);
				continue;
			}

			System.out.println("Сообщение принято к обработке");


			String result = "";
			MimeMultipart mimeMultipart = (MimeMultipart) messages[i].getContent();
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			String html = (String) bodyPart.getContent();
			result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
			CharSequence searchPhrase = "070756246742";
			if (html.contains(searchPhrase)) {
				System.out.println("ОК.Mail boby contains confirmation number.");
			} else {
				System.err.println("!!!===SHIT!Mail boddy do not contains confirmation number===!!!");

			}
			//System.out.println(result);

			folder.close(false);

			store.close();
		}
	}
}
