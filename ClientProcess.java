//Course: CS4345
//Semester/Year: Summer 2020
//Assignment Identifier: Assignment 2
//Cody Phillips and Jeremy Craven

import java.net.*;
import java.io.*;
import java.util.*;

class Client extends Thread{
	Socket socket;
	DataInputStream input;
	boolean open = true;
	String message;
	
	//Client Constructor
	public Client(Socket socket, DataInputStream input){
		this.socket = socket;
		this.input = input;
	}
	
	@Override
	public void run(){
		while(open){
			try{
				message = input.readUTF();
				System.out.println(message);
				if(message.equals("Goodbye.")){
					open = false;
					socket.close();
				}

			}
			catch(IOException ioe){
				System.err.println(ioe);
			}
		}
	}
	
}

public class ClientProcess{
	public static void main(String[] args){
		try{
			//create a socket to make connection to server socket
			Socket sock = new Socket("127.0.0.1", 7000);
			boolean open = true;
			
			//create an output stream to send data to the server
			DataOutputStream data2server = new DataOutputStream(sock.getOutputStream());
			//create an input stream to receive data from server
			DataInputStream result4mserver = new DataInputStream(sock.getInputStream());
			
			Thread t = new Client(sock, result4mserver);
			t.start();
			
			Scanner inp = new Scanner(System.in);
			
			//Open until user types 'exit'
			while(open){
				String message = inp.nextLine();
				
				data2server.writeUTF(message);
				data2server.flush();
				
				//Allows the user to exit chat
				if(message.equals("exit")){
					open = false;
				}
			}
			

		 } 
		catch(IOException ioe){
				System.err.println(ioe);
		}
	}
}