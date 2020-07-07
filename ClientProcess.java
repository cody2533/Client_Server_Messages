import java.net.*;
import java.io.*;
import java.util.*;

class Client extends Thread{
	Socket socket;
	DataInputStream input;
	
	public Client(Socket socket, DataInputStream input){
		this.socket = socket;
		this.input = input;
	}
	
	@Override
	public void run(){
		while(true){
			try{
				System.out.println(input.readUTF());
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
			
			while(open){
				String message = inp.nextLine();
				data2server.writeUTF(message);
				data2server.flush();
			}
			
			
			sock.close();
		 } catch(IOException ioe){
				System.err.println(ioe);
			}
	}//End-of-main
}//End-ofclass