/*
 * ChatServer.java
 * 
 * This is the file that holds the server code for the chat application
 * It holds the GUI and also 2 inner classes that contain the threads required
 * for multiple client connections. ConnectionThread will listen for incoming
 * client requests, which will spawn a CommunicationThread that handles input/output
 * received from that client.
 * 
 * ChatApp Project 4
 * Created for CS342 at UIC
 * By Lawrence Chu, Grieldo Lulaj, Daia Elsalaymeh
 * 
 */

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ChatServer extends JFrame implements ActionListener {
	
	//GUI items
	private JButton ssButton;
	private JLabel machineInfo;
	private JLabel portInfo;
	private JTextArea history;

	//network related items
	private boolean running;
	private ConnectionThread listener;
	private ArrayList<CommunicationThread> clients;
	
	//constructor, set up GUI
	public ChatServer() {
	    setTitle( "Chat Server" );
	    
		running = false;
		
		Container c = getContentPane();
		c.setLayout( new FlowLayout() );
		
		ssButton = new JButton("Start Listening");
		ssButton.addActionListener(this);
		c.add(ssButton);
		
		//get IP address
		String machineAddress = null;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			machineAddress = addr.getHostAddress();
		} catch (UnknownHostException e) {
			machineAddress = "192.168.0.13";
		}
		machineInfo = new JLabel(machineAddress);
		c.add(machineInfo);
		portInfo = new JLabel(" Not Listening ");
		c.add(portInfo);

		history = new JTextArea(10, 40);
		history.setEditable(false);
		c.add(new JScrollPane(history));

		setSize(500, 250);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		clients = new ArrayList<CommunicationThread>();
	}
	
	//handle server listening on/off button event
	public void actionPerformed(ActionEvent event) {
		if (running == false) {
			listener = new ConnectionThread();
			listener.start();
			running = true;
		} else {
			ssButton.setText("Start Listening");
			portInfo.setText(" Not Listening ");
			running = false;
		}
	}

	public static void main(String args[]) {
		ChatServer application = new ChatServer();
	}

	
	//connection thread listens for new socket connections
	//once a connection request has been found, a new communication thread is spawned to
	//deal with input/output communication with that client
	private class ConnectionThread extends Thread {
		private ServerSocket s;
		private ChatServer gui = ChatServer.this;
		
		public void run() {

			try {
				s = new ServerSocket(0);
				gui.portInfo.setText("Listening on Port: " + s.getLocalPort());
				try {
					while (gui.running) {
						gui.ssButton.setText("Stop Listening");
						new CommunicationThread(s.accept());	//spawn off new thread
					}
				} catch (IOException e) {
					System.err.println("Accept failed.");
					System.exit(1);
				}
			} catch (IOException e) {
				System.err.println("Could not listen on port: 10008.");
				System.exit(1);
			} finally {
				try {
					s.close();
				} catch (IOException e) {
					System.err.println("Could not close port: 10008.");
					System.exit(1);
				}
			}
		}
	}

	
	
	//communication thread handles input/output on the network for 1 client
	//it will also update the server's list of clients/usernames
	//the thread expects the client to send the client's username first
	//it will then send the list of connected users back to the client so the userlist 
	//can be set
	//it will close the connections if the user name is not unique
	//finally, the thread will start listening for user input from the client, and send
	//the message out to the appropriate clients.
	private class CommunicationThread extends Thread {
		private String userName;
		private Socket clientSocket;
		private PrintWriter out;
		private BufferedReader in;
		private ChatServer gui = ChatServer.this;

		public CommunicationThread(Socket clientSoc) {
			clientSocket = clientSoc;
			start();
		}

		public void run() {
			try {
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String inputLine;
				userName = in.readLine();
				for (CommunicationThread c : gui.clients) {
					if(equals(c)){							//check user name is unique
						out.println("\0clientlistend");
						out.println("<Username already in use>");
						return;
					}
					out.println(c.userName);				//send connected users to new client
				}
				out.println("\0clientlistend");
				gui.clients.add(this);
				addNewUsertoList(userName);
				while ((inputLine = in.readLine()) != null) {
					gui.history.append(inputLine + "\n");
					if(inputLine.length()>0&&inputLine.charAt(0)=='/'&&inputLine.charAt(1)=='w'){
						tryPrivateMessage(inputLine);
					}
					else{
						for (CommunicationThread c : gui.clients) {
							c.out.println(userName + ": " + inputLine);
						}
					}
				}
				removeUserfromList(userName);
			} catch (IOException e) {
				System.err.println("Problem with Communication Server");
				removeUserfromList(userName);
			}
			finally{
				out.close();
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				gui.clients.remove(this);
				try {
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//tell clients to add new user to their user lists
		private void addNewUsertoList(String u){
			for (CommunicationThread c : gui.clients) {
				c.out.println("\0newuseradd");
				c.out.println(u);
			}
		}
		
		//tell clients to remove user from user lists
		private void removeUserfromList(String u){
			for (CommunicationThread c : gui.clients) {
				c.out.println("\0userremove");
				c.out.println(u);
			}
		}
		
		//attempts to send a private message to a specified user.
		//syntax: /w username message...
		private void tryPrivateMessage(String s){
			String[] parsed = s.split("\\s+",3);
			if(parsed.length<3){
				return;
			}
			for(CommunicationThread c : gui.clients){
				if(parsed[1].equals(c.userName)){
					out.println("<private>" + userName + ": " + parsed[2]);
					c.out.println("<private>" + userName + ": " + parsed[2]);
					return;
				}
			}
			out.println("<User not found>");
		}
		
		//used to compare to check for duplicate usernames and add/remove arraylist options
		public boolean equals(CommunicationThread c) {
			return userName.equals(c.userName);
		}
	}
}
