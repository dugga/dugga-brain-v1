package com.dugga.examples.SWT.socketListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSide {
	ServerSocket providerSocket;
	Socket connection = null;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	
	public ServerSide() {
	}
	
	public void run() {
		try {
			// creating a server socket
			providerSocket = new ServerSocket(2129, 10);
			
			// wait for connection
			System.out.println("Waiting for connection");
			connection = providerSocket.accept();
			System.out.println("Connection received from " + connection.getInetAddress().getHostName());
			
			// get input and output streams
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			sendMessage("Connection successful");
			
			// the two parts communicate via the input and output streams
			do {
				try {
					message = (String)in.readObject();
					System.out.println("client>" + message);
					if (message.equals("bye"))
						sendMessage("bye");
				} catch (ClassNotFoundException e) {
					System.out.println("Data received in unknown format");
				}
			} while(!message.equals("bye"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// closing connection
			try {
				in.close();
				out.close();
				providerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendMessage(String message) {
		try {
			out.writeObject(message);
			out.flush();
			System.out.println("server>" + message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSide server = new ServerSide();
		while(true) {
			server.run();
		}
	}

}
