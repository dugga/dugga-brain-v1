package com.dugga.examples.SWT.socketListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSide {
	Socket requestSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	
	public ClientSide() {
		// TODO Auto-generated constructor stub
	}
	
	public void run() {
		try {
			// creating a socket connection to the server
			requestSocket = new Socket("localhost", 2129);
			System.out.println("Connected to localhost in port 2129");
			
			// get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			
			// Communicating with the server
			do {
				try {
					message = (String)in.readObject();
					System.out.println("server>" + message);
					sendMessage("Hi.  My server");
					message = "bye";
					sendMessage(message);
				} catch (ClassNotFoundException e) {
					System.err.println("data received in unknown format");
				}
			} while(!message.equals("bye"));
		} catch (UnknownHostException e) {
			System.err.println("You are trying to connect to an  unknown host");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// closing connection
			try {
				in.close();
				out.close();
				requestSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendMessage(String message) {
		try {
			out.writeObject(message);
			out.flush();
			System.out.println("client>" + message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientSide client = new ClientSide();
		client.run();
	}

}
