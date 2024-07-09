package com.example.erp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Requester {
	Socket requestSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;

	Requester() {
	}

	void run() throws IOException {
		try {
			requestSocket = new Socket("192.168.1.1", 4370);
			System.out.println("Connected to given host in port 4370");
			in = new ObjectInputStream(requestSocket.getInputStream());
			String line;
			while (true) {
				line = in.readLine();
				if (line != null) {
					System.out.println(line);
				}
			}
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");

		} catch (IOException ioException) {
			ioException.printStackTrace();

		} catch (Exception Exception) {
			Exception.printStackTrace();

		} finally {
			in.close();
			requestSocket.close();
		}
	}

	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("client: " + msg);

		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public static void main(String args[]) throws IOException {
		Requester client = new Requester();
		client.run();
	}
}
