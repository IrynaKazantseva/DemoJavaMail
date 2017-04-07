package com.gmail.ssl;

import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

public class ReciverImap {

	final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private Properties imapProps;
	private URLName url;

	public ReciverImap(String username, String password) {
		try {
			imapProps = new Properties();

			imapProps.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
			imapProps.setProperty("mail.imap.socketFactory.fallback", "false");
			imapProps.setProperty("mail.imap.port", "993");
			imapProps.setProperty("mail.imap.socketFactory.port", "993");
			url = new URLName("imap", "imap.gmail.com", 953, "", username, password);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	public void recive(String folderInbox) throws MessagingException, IOException {

		Session session = Session.getInstance(imapProps, null);
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
			String subject = messages[i].getSubject().toString().toLowerCase().trim(); // Получение
																						// темы
																						// письма

			String letterConfirmationSubject = "your order has been received"; // Тема
																				// письма
																				// которое
																				// содержит
																				// сonfirmation
																				// number

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
