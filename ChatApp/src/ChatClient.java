/*
 * ChatClient.java
 * 
 * This is the file that holds the client code for the chat application
 * It holds the GUI and also 1 inner classes that contains the thread
 * which will listen for messages from the server, while the main thread handles output.
 * When a message is sent, it will by default send to all clients connected to the server.
 * The user can also specify 1 person to message privately by typing /w [username] [message].
 * 
 * ChatApp Project 4
 * Created for CS342 at UIC
 * By Lawrence Chu, Grieldo Lulaj, Daia Elsalaymeh
 * 
 */

import java.net.*;
import java.util.ArrayList;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class ChatClient extends JFrame implements ActionListener {
	// GUI items
	private JButton sendButton;
	private JButton connectButton;
	
	private JTextField machineInfo;
	private JTextField portInfo;
	private JTextField message;
	private JTextField username;
	
	private JScrollPane histPane;
	private JScrollPane usePane;
	private JTextArea history;
	private JList<String> userlist;
	
	private JPanel upperPanel;
	private JPanel lowerPanel;
	
	// Network Items
	private boolean connected;
	private Socket echoSocket;
	private PrintWriter out;

	private ArrayList<String> userarr;	//stores all current users in chatroom

	// constructor, set up GUI
	public ChatClient() {
		super("Chat Client");

		// get content pane and set its layout
		Container container = getContentPane();
		container.setLayout(new GridLayout(2, 1));

		// set up the login/message panel
		upperPanel = new JPanel();
		upperPanel.setLayout(new GridLayout(5, 2));
		container.add(upperPanel);

		// create buttons
		connected = false;

		upperPanel.add(new JLabel("Message: ", JLabel.RIGHT));
		message = new JTextField("");
		message.addActionListener(this);
		upperPanel.add(message);

		sendButton = new JButton("Send Message");
		sendButton.addActionListener(this);
		sendButton.setEnabled(false);
		upperPanel.add(sendButton);

		connectButton = new JButton("Connect to Server");
		connectButton.addActionListener(this);
		upperPanel.add(connectButton);

		upperPanel.add(new JLabel("Server Address: ", JLabel.RIGHT));
		machineInfo = new JTextField("192.168.0.7");
		upperPanel.add(machineInfo);

		upperPanel.add(new JLabel("Server Port: ", JLabel.RIGHT));
		portInfo = new JTextField("5000");
		upperPanel.add(portInfo);

		upperPanel.add(new JLabel("Username: ", JLabel.RIGHT));
		username = new JTextField("");
		upperPanel.add(username);

		//set up the message list/user list panel
		lowerPanel = new JPanel();
		lowerPanel.setLayout(new GridLayout(1, 2));

		history = new JTextArea(10, 40);
		history.setEditable(false);
		history.setLineWrap(true);
		history.setWrapStyleWord(true);
		DefaultCaret caret = (DefaultCaret)history.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		histPane = new JScrollPane(history);
		lowerPanel.add(histPane);

		userlist = new JList<String>();
		usePane = new JScrollPane();
		usePane.getViewport().add(userlist);
		lowerPanel.add(usePane);

		container.add(lowerPanel);

		setSize(500, 500);
		setVisible(true);

		userarr = new ArrayList<String>();
	}

	public static void main(String args[]) {
		ChatClient application = new ChatClient();
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// handle connection/send message button events
	public void actionPerformed(ActionEvent event) {
		if (connected && (event.getSource() == sendButton || event.getSource() == message)) {
			doSendMessage();
		} else if (event.getSource() == connectButton) {
			doManageConnection();

		}
	}
	
	//handle message sending
	public void doSendMessage() {
		out.println(message.getText());
		message.setText("");
	}
	
	//handle connecting/disconnecting to server
	//checks for valid usernames (no spaces and can't start with '/') then spawns a new input threa that listens for input, parent thread handles output. 
	//on connection, the client will immediately send the client's user name, which the server is expecting
	public void doManageConnection() {
		if (connected == false) {
			String machineName = null;
			int portNum = -1;
			try {
				String name = username.getText();
				if(name.length()==0||name.indexOf(' ')>=0||name.charAt(0)=='/'){
					history.append("<invalid username>\n");
					return;
				}
				machineName = machineInfo.getText();
				portNum = Integer.parseInt(portInfo.getText());
				echoSocket = new Socket(machineName, portNum);
				out = new PrintWriter(echoSocket.getOutputStream(), true);
				out.println(name);
				sendButton.setEnabled(true);
				machineInfo.setEnabled(false);
				username.setEnabled(false);
				portInfo.setEnabled(false);
				connected = true;
				history.append("<Connected>\n");
				connectButton.setText("Disconnect from Server");
				new InputThread(echoSocket);
			} catch (NumberFormatException e) {
				history.append("<Server Port must be an integer>\n");
			} catch (UnknownHostException e) {
				history.append("<Don't know about host: " + machineName + ">\n");
			} catch (IOException e) {
				history.append("<Couldn't get I/O for " + "the connection to: " + machineName + ">\n");
			}

		} else {
			try {
				out.close();
				echoSocket.close();
				resetGUI();
			} catch (IOException e) {
				history.append("<Error in closing down Socket>\n");
			}
		}
	}
	
	//resets GUI on disconnect
	public void resetGUI() {
		history.append("<Disconnected>\n");
		sendButton.setEnabled(false);
		machineInfo.setEnabled(true);
		username.setEnabled(true);
		portInfo.setEnabled(true);
		connected = false;
		connectButton.setText("Connect to Server");
		userarr = new ArrayList<String>();
		updateUserList();
	}

	//updates list of logged in users
	public void updateUserList() {
		usePane.getViewport().remove(userlist);
		String[] str = new String[userarr.size()];
		userlist = new JList<String>(userarr.toArray(str));
		usePane.getViewport().add(userlist);
	}

	
	
	//thread class to listen for messages coming from server
	private class InputThread extends Thread {
		private Socket clientSocket;
		private ChatClient gui;
		private BufferedReader in;

		public InputThread(Socket clientSoc) {
			clientSocket = clientSoc;
			gui = ChatClient.this;
			start();
		}
		
		//input thread first listens for the server's client list, in order to create the list on the client side, terminated by "\0clientlistend"
		//once initial client list is set, starts listening for messages from server
		//2 special messages, "\0newuseradd" and "\0userremove" will make client update its user list
		//otherwise, it will print the message received to the message text area
		public void run() {
			try {
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				String inputLine;
				while ((inputLine = in.readLine()) != null && !inputLine.equals("\0clientlistend")) {
					gui.userarr.add(inputLine);
					gui.updateUserList();
				}

				while ((inputLine = in.readLine()) != null) {
					if (inputLine.equals("\0newuseradd")) {
						gui.userarr.add(in.readLine());
						gui.updateUserList();
					} else if (inputLine.equals("\0userremove")) {
						gui.userarr.remove(in.readLine());
						gui.updateUserList();
					} else {
						gui.history.append(inputLine + "\n");
					}
				}
				in.close();
				clientSocket.close();
				gui.resetGUI();
			} catch (IOException e) {
				System.err.println("Problem with Communication Server");
			}
		}
	}
}
