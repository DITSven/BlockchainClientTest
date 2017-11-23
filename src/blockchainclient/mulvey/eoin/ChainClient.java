package blockchainclient.mulvey.eoin;
import java.io.*;
import java.net.*;

import javax.swing.*;

@SuppressWarnings("serial") //Not going to serialize this class
public class ChainClient extends JFrame {
	JTextArea responseWindow;
	Socket clientSocket;
	String host;
	int port;
	int blockNumber;
	String blockHash;
	BufferedReader input;
	PrintWriter output;
	
	public ChainClient() {
		responseWindow = new JTextArea();
		add(responseWindow);
		setSize(600,160);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		host = "127.0.0.1";
		port = 62626;
		blockNumber = 5;
		blockHash = "0d74eb07e902c8a3aac7d3082799e7caa622464914c4a068e21b4c0bea55d1e5";
		openClientSocket();
		talkToServer();
		closeClientSocket();
	}
	
	public void openClientSocket() {
		try {
			clientSocket = new Socket(host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			output = new PrintWriter(clientSocket.getOutputStream(), true);

		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
		
	public void talkToServer() {
		String responseLine;
		showMessage("Listening for server \n");
		try {
			while (true) {
				responseLine = input.readLine();
				showMessage("Response: " + responseLine + "\n");
				if (responseLine.equals("SERVER-END")) {
					showMessage("SERVER-END: " + responseLine + "\n");
					break;
				}
				showMessage("Sending block number to server \n");
				output.println(Integer.toString(blockNumber));
				responseLine = input.readLine();
				
				showMessage("Sending block hash to server \n");
				output.println(blockHash);
				responseLine = input.readLine();
				showMessage("Previous Hash: " + responseLine + "\n");
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeClientSocket() {
		try {
			input.close();
			output.close();
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
	}
	
	
	public void showMessage(final String text){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					responseWindow.append(text);
				}
			}
		);
	}
}
