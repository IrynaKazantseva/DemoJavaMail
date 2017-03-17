package com.gmail;

import java.io.IOException;

import javax.mail.MessagingException;

public class Main {
	private static com.gmail.tls.Sender tlsSender = new com.gmail.tls.Sender("markandgrahamtest@gmail.com", "123456789q");
	//private static com.gmail.ssl.Sender sslSender = new com.gmail.ssl.Sender("markandgrahamtest@gmail.com", "123456789q");
	private static com.gmail.tls.Reciver tlsReciver = new com.gmail.tls.Reciver("markandgrahamtest@gmail.com", "123456789q");
	
    public static void main(String[] args) throws MessagingException, IOException{
        tlsSender.send("This is Subject", "TLS: This is text!", "markandgrahamtest@gmail.com");
        //sslSender.send("This is Subject", "SSL: This is text!", "markandgrahamtest@gmail.com");
        tlsReciver.recive("INBOX");
    }
}