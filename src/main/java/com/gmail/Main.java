package com.gmail;

import java.io.IOException;

import javax.mail.MessagingException;

public class Main {
	private static com.gmail.tls.Sender tlsSender = new com.gmail.tls.Sender("markandgrahamtest@gmail.com","123456789q");
	private static com.gmail.ssl.Sender sslSender = new com.gmail.ssl.Sender("markandgrahamtest@gmail.com","123456789q");
	private static com.gmail.ssl.ReciverImap imapReciver = new com.gmail.ssl.ReciverImap("markandgrahamtest@gmail.com","123456789q");
	private static com.gmail.ssl.ReciverPop3 popReciver = new com.gmail.ssl.ReciverPop3("markandgrahamtest@gmail.com","123456789q");

	public static void main(String[] args) throws MessagingException, IOException {
		tlsSender.send("This is Subject", "TLS: This is text!", "markandgrahamtest@gmail.com");
		sslSender.send("This is Subject", "SSL: This is text!", "markandgrahamtest@gmail.com");
		imapReciver.recive("INBOX");
		popReciver.recive("INBOX");
	}
}