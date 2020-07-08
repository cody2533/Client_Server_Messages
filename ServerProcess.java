//Course: CS4345
//Semester/Year: Summer 2020
//Assignment Identifier: Assignment 2
//Cody Phillips and Jeremy Craven

import java.net.*;
import java.io.*;
import java.util.*;


class Sock extends Thread {
	
	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	String name;
	
	//Sock constructor
	public Sock(Socket socket, DataInputStream input, DataOutputStream output){
		this.socket = socket;
		this.input = input;
		this.output = output;
	}
	
	@Override
	public void run(){
		String message;
		boolean open = true;
		
		try {
			output.writeUTF("Enter your name: ");
			this.name = input.readUTF();
			MessageHandler.add(socket, name);
			output.writeUTF("Welcome " + name + ". Type 'exit' to leave.");
		
			while(open){
				message = input.readUTF();
				if(message.equals("exit")){
					open = false;
					MessageHandler.remove(socket, name);
				}
				else
					MessageHandler.message(message, socket, name);
			}
		} 
		catch(IOException ioe){
			System.err.println(ioe);
			MessageHandler.remove(socket, name);
		}
	}
	
}

public class ServerProcess{
	public static void main(String[] args){
		MessageHandler.run();
		try{
			ServerSocket servSock = new ServerSocket(7000);
			System.out.println("Server started at "+ new Date() + '\n');
			
			Socket sock = null;
			boolean on = true;
			
			//Keeps the server running
			while(on) {
				sock = servSock.accept();
				//create data input and data output streams
				DataInputStream input = new DataInputStream(sock.getInputStream());
				DataOutputStream output = new DataOutputStream(sock.getOutputStream());
				Thread t = new Sock(sock, input, output);
				t.start();
			}
			sock.close();
			servSock.close();
			
		 } 
		catch(IOException ioe){
				System.err.println(ioe);
		}
	}
}

class MessageHandler {
	static Socket[] sockets;
	static DataOutputStream output;
	
	public static void run(){
		sockets = new Socket[0];
	}
	
	//Adds a socket to the sockets array to keep track of connected sockets
	public static void add(Socket socket, String name){
		sockets = Arrays.copyOf(sockets, sockets.length + 1);
		sockets[sockets.length - 1] = socket;
		for(Socket s : sockets){
			try {	
				output = new DataOutputStream(s.getOutputStream());
				output.writeUTF(name + " has joined the server.");
			} 
			catch(IOException ioe){
				System.err.println(ioe);
			}
		}
	}
	
	//removes a socket from the sockets array
	public static void remove(Socket socket, String name){
		Socket[] temp = Arrays.copyOf(sockets, sockets.length);
		sockets = new Socket[sockets.length - 1];
		int i = 0;
		
		for(Socket s : temp){
			if(s != socket){
				sockets[i] = s;
				i++;
				try {
					output = new DataOutputStream(s.getOutputStream());
					output.writeUTF(name + " has left the server.");
				} 
				catch(IOException ioe){
					System.err.println(ioe);
				}
			}
			else{
				try {
					output = new DataOutputStream(s.getOutputStream());
					output.writeUTF("Goodbye.");
				} 
				catch(IOException ioe){
					System.err.println(ioe);
				}
			}
		}
	}
	
	//Sends a message to each of the connected sockets
	public static void message(String str, Socket socket, String name){ 
		for(Socket s : sockets){
			if(s != socket){
				try {
					output = new DataOutputStream(s.getOutputStream());
					output.writeUTF(name + ": " + str);
				} 
				catch(IOException ioe){
					System.err.println(ioe);
				}
			}
		}
	}
}