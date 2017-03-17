package com.gmail.tls;

import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.search.FlagTerm;


public class Reciver {
	
	//String FOLDER_INDOX = "INBOX"; // имя папки "Входящие"
	String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private Properties pop3Props;
	private URLName url;

	public Reciver(String username, String password) {
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
			String subject = messages[i].getSubject().toString().toLowerCase().trim(); //Получение темы письма

			String letterConfirmationSubject = "this is subject"; //Тема письма которое содержит активационную ссылку

                        //Выбор нужного письма
			if (!subject.equals(letterConfirmationSubject)) {
				System.err.println("Сообщение пропущено, т.к. не является активационным письмом.......");
				continue;
			}

			System.out.println("Сообщение принято к обработке");
			
			Object contentMessage = messages[i].getContent();
			String content = contentMessage.toString();
                        
                        //Проверка наличия активационной ссылки в теле письма
			CharSequence searchPhrase = "This is text";
			if (content.contains(searchPhrase)) {
				System.out.println("ОК.Письмо содержит активационную ссылку.");
			} else {
				System.err.println("!!!===SHIT!Письмо не содержит активационную ссылку===!!!");
				
		}
		folder.close(false);

		store.close();}}}
	
		