import java.net.*;
import java.io.*;
import java.util.*;


class Sock extends Thread {
	
	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	String name;
	
	public Sock(Socket socket, DataInputStream input, DataOutputStream output){
		this.socket = socket;
		this.input = input;
		this.output = output;
	}
	
	@Override
	public void run(){
		
		try {
			output.writeUTF("Enter your name: ");
			this.name = input.readUTF();
			MessageHandler.add(socket, name);
		
			while(true){
				MessageHandler.message(input.readUTF(), socket, name);
			}
		} 
		catch(IOException ioe){
			System.err.println(ioe);
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
			
			while(on) {
				sock = servSock.accept();
			
				//create data input and data output streams
				DataInputStream inp = new DataInputStream(sock.getInputStream());
				DataOutputStream outp = new DataOutputStream(sock.getOutputStream());
				Thread t = new Sock(sock, inp, outp);
				t.start();
			}
			sock.close();
			servSock.close();
			
		 } catch(IOException ioe){
				System.err.println(ioe);
			}

	}//End-of-main
}//End-of-class

class MessageHandler {
	static Socket[] sockets;
	static DataOutputStream output;
	
	public static void run(){
		sockets = new Socket[0];
	}
	
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

