/**
 * Main class implements GUI
 * Lawrence & Ana
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Main {
    static boolean isKeyGenerated = false;
    static boolean isPrivate = false;
    static boolean isPublic = false;
    static RSA.RSAKeys keys;

    /**
     * Nested classes - main/auxiliary windows
     */

    public static class RSAMainWindow extends JPanel implements ActionListener {
        JLabel message;
        public RSAMainWindow() {
            setBackground(Color.GRAY);
            setBackground(Color.GRAY);
            setLayout( new GridLayout(7,1,3,3) );
            message = new JLabel("Please choose the option", JLabel.CENTER);
            message.setForeground(new Color(180,0,0));
            message.setBackground(Color.WHITE);
            message.setOpaque(true);
            add(message);
            JPanel buttonBar;
            JButton button;

            buttonBar = new JPanel();
            buttonBar.setLayout(new GridLayout(1,2,3,3));
            buttonBar.setBackground(Color.GRAY);
            button = new JButton("Enter a key");
            button.addActionListener(this);
            buttonBar.add(button);
            button = new JButton("Generate a key");
            button.addActionListener(this);
            buttonBar.add(button);
            add(buttonBar);

            buttonBar = new JPanel();
            buttonBar.setLayout(new GridLayout(1,2,3,3));
            buttonBar.setBackground(Color.GRAY);
            button = new JButton("Save a private key");
            button.addActionListener(this);
            buttonBar.add(button);
            button = new JButton("Load a private key");
            button.addActionListener(this);
            buttonBar.add(button);
            add(buttonBar);

            buttonBar = new JPanel();
            buttonBar.setLayout(new GridLayout(1,2,3,3));
            buttonBar.setBackground(Color.GRAY);
            button = new JButton("Save a public key");
            button.addActionListener(this);
            buttonBar.add(button);
            button = new JButton("Load a public key");
            button.addActionListener(this);
            buttonBar.add(button);
            add(buttonBar);

            buttonBar = new JPanel();
            buttonBar.setLayout(new GridLayout(1,2,3,3));
            buttonBar.setBackground(Color.GRAY);
            button = new JButton("Block");
            button.addActionListener(this);
            buttonBar.add(button);
            button = new JButton("Unblock");
            button.addActionListener(this);
            buttonBar.add(button);
            add(buttonBar);

            buttonBar = new JPanel();
            buttonBar.setLayout(new GridLayout(1,2,3,3));
            buttonBar.setBackground(Color.GRAY);
            button = new JButton("Encrypt Blocked");
            button.addActionListener(this);
            buttonBar.add(button);
            button = new JButton("Decrypt Blocked");
            button.addActionListener(this);
            buttonBar.add(button);
            add(buttonBar);

            buttonBar = new JPanel();
            buttonBar.setLayout(new GridLayout(1,2,3,3));
            buttonBar.setBackground(Color.GRAY);
            button = new JButton("Encrypt");
            button.addActionListener(this);
            buttonBar.add(button);
            button = new JButton("Decrypt");
            button.addActionListener(this);
            buttonBar.add(button);
            add(buttonBar);
            setBorder(BorderFactory.createLineBorder(Color.GRAY,3));
        }

        /**
         * Implementation of the ActionListener
         * @param evt
         */
        public void actionPerformed(ActionEvent evt) {
            String command = evt.getActionCommand();
            if (command.equals("Enter a key")) {
                boolean proceedCalculatingKey = true;
                int p = 0;
                int q = 0;
                String responseP = JOptionPane.showInputDialog(this,"Please enter a prime number p");
                String responseQ = JOptionPane.showInputDialog(this,"Please enter a prime number q");
                if (responseP == null || responseQ == null) {
                    JOptionPane.showMessageDialog(this, "You have not entered one or two numbers");
                    proceedCalculatingKey = false;
                }
                else {
                    try {
                        p = Integer.parseInt(responseP);
                        q = Integer.parseInt(responseQ);
                        if (!RSA.isPrime(new HugeUInt(p))) {
                            JOptionPane.showMessageDialog(this, "p is not a prime number");
                            proceedCalculatingKey = false;
                        }
                        if (!RSA.isPrime(new HugeUInt(q))) {
                            JOptionPane.showMessageDialog(this, "q is not a prime number");
                            proceedCalculatingKey = false;
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this,"Please only enter integers");
                        proceedCalculatingKey = false;
                    }
                }
                if (proceedCalculatingKey) {
                    keys = new RSA.RSAKeys(p, q);
                    isPrivate = true;
                    isPublic = true;
                    isKeyGenerated = true;
                }
            }
            else if (command.equals("Generate a key")) {
            	int ps;
            	String primeSize = JOptionPane.showInputDialog(this,"How many decimals would you like in your prime numbers?");
            	 try {
                     ps = Integer.parseInt(primeSize);
                     if(ps>0){
                    	 keys = new RSA.RSAKeys(ps);
     	                isKeyGenerated = true;
     	                isPublic = true;
     	                isPrivate = true;
     	                JOptionPane.showMessageDialog(this,"The key has been auto-generated");
                     }
                     else{
                    	 JOptionPane.showMessageDialog(this,"A positive number is required.");
                     }
            	 }
            	 catch (NumberFormatException e) {
                     JOptionPane.showMessageDialog(this,"Please only enter integers.");
                 }
            }
            else if (command.equals("Load a public key")) {
                JFileChooser fileChooser = new JFileChooser();
                int ret = fileChooser.showDialog(null, "Open an xml key file");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File xmlFile = fileChooser.getSelectedFile();
                    keys = RSA.readPublicKeyXML(xmlFile);
                    if (keys != null) JOptionPane.showMessageDialog(this,"A public key has been loaded");
                    isPrivate = false;
                    isPublic = true;
                }
            }
            else if (command.equals("Load a private key")) {
                JFileChooser fileChooser = new JFileChooser();
                int ret = fileChooser.showDialog(null, "Open an xml key file");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File xmlFile = fileChooser.getSelectedFile();
                    keys = RSA.readPrivateKeyXML(xmlFile);
                    if (keys != null) JOptionPane.showMessageDialog(this, "A private key has been loaded");
                    isPublic = false;
                    isPrivate = true;
                }
            }
            else if (command.equals("Save a public key")) {
                if (isPublic) {
                    JFileChooser fileChooser = new JFileChooser();
                    int ret = fileChooser.showDialog(null, "Save an xml key file");
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        File xmlFile = fileChooser.getSelectedFile();
                        RSA.savePublicKeyXML(xmlFile, keys);
                        JOptionPane.showMessageDialog(this, "A public key has been saved");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(this, "No public key has been loaded");
                }
            }
            else if (command.equals("Save a private key")) {
                if (isPrivate) {
                    JFileChooser fileChooser = new JFileChooser();
                    int ret = fileChooser.showDialog(null, "Save an xml key file");
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        File xmlFile = fileChooser.getSelectedFile();
                        RSA.savePrivateKeyXML(xmlFile, keys);
                        JOptionPane.showMessageDialog(this, "A private key has been saved");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(this, "No private key has been loaded");
                }
            }
            else if (command.equals("Encrypt")) {
                if (isPublic) {
                	boolean bsInput = false;
                	while(!bsInput){
                    	String blockSize = JOptionPane.showInputDialog(this,"Enter a blocking size");
                    	 try {
                             int bs = Integer.parseInt(blockSize);
                             if(bs>0){
                            	 RSA.setBlockSize(bs);
                            	 bsInput = true;
                             }
                    	 }
                    	 catch (NumberFormatException e) {
                             JOptionPane.showMessageDialog(this,"Please only enter integers.");
                         }
                	}
                    JFileChooser fileChooser = new JFileChooser();
                    int ret = fileChooser.showDialog(null, "Choose a file");
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        File blockedFile = fileChooser.getSelectedFile();
                        ret = fileChooser.showDialog(null, "Save an encrypted file");
                        if (ret == JFileChooser.APPROVE_OPTION) {
                            File txtFile = fileChooser.getSelectedFile();
                            if(!RSA.encryptFile(blockedFile, txtFile, keys)){
                            	JOptionPane.showMessageDialog(this, "Public key size too small for file");
                            }
                        }
                    }
                } else JOptionPane.showMessageDialog(this, "No public key has been loaded");
            }
            else if (command.equals("Decrypt")) {
                if (isPrivate) {
                	boolean bsInput = false;
                	while(!bsInput){
                    	String blockSize = JOptionPane.showInputDialog(this,"Enter a blocking size");
                    	 try {
                             int bs = Integer.parseInt(blockSize);
                             if(bs>0){
                            	 RSA.setBlockSize(bs);
                            	 bsInput = true;
                             }
                    	 }
                    	 catch (NumberFormatException e) {
                             JOptionPane.showMessageDialog(this,"Please only enter integers.");
                         }
                	}
                    JFileChooser fileChooser = new JFileChooser();
                    int ret = fileChooser.showDialog(null, "Choose an encrypted file");
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        File blockedFile = fileChooser.getSelectedFile();
                        ret = fileChooser.showDialog(null, "Save a file");
                        if (ret == JFileChooser.APPROVE_OPTION) {
                            File decFile = fileChooser.getSelectedFile();
                            if(!RSA.decryptFile(blockedFile, decFile, keys)){
                            	JOptionPane.showMessageDialog(this, "Private key size too small for file");
                            }
                        }
                    }
                } else JOptionPane.showMessageDialog(this, "No private key has been loaded");
            }
            else if (command.equals("Block")) {
            	boolean bsInput = false;
            	while(!bsInput){
                	String blockSize = JOptionPane.showInputDialog(this,"Enter a blocking size");
                	 try {
                         int bs = Integer.parseInt(blockSize);
                         if(bs>0){
                        	 RSA.setBlockSize(bs);
                        	 bsInput = true;
                         }
                	 }
                	 catch (NumberFormatException e) {
                         JOptionPane.showMessageDialog(this,"Please only enter integers.");
                     }
            	}
                JFileChooser fileChooser = new JFileChooser();
                int ret = fileChooser.showDialog(null, "Choose a file");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File txtFile = fileChooser.getSelectedFile();
                    ret = fileChooser.showDialog(null, "Save a blocked file");
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        File blockedFile = fileChooser.getSelectedFile();
                        RSA.encryptFile(txtFile, blockedFile, null);
                    }
                }
            }
            else if (command.equals("Unblock")) {
            	boolean bsInput = false;
            	while(!bsInput){
                	String blockSize = JOptionPane.showInputDialog(this,"Enter a blocking size");
                	 try {
                         int bs = Integer.parseInt(blockSize);
                         if(bs>0){
                        	 RSA.setBlockSize(bs);
                        	 bsInput = true;
                         }
                	 }
                	 catch (NumberFormatException e) {
                         JOptionPane.showMessageDialog(this,"Please only enter integers.");
                     }
            	}
                JFileChooser fileChooser = new JFileChooser();
                int ret = fileChooser.showDialog(null, "Choose a blocked file");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File blockedFile = fileChooser.getSelectedFile();
                    ret = fileChooser.showDialog(null, "Save a file");
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        File txtFile = fileChooser.getSelectedFile();
                        RSA.decryptFile(blockedFile, txtFile, null);
                    }
                }
            }
            else if (command.equals("Encrypt Blocked")) {
                if (isPublic) {	 
                    JFileChooser fileChooser = new JFileChooser();
                    int ret = fileChooser.showDialog(null, "Choose a blocked file");
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        File blockedFile = fileChooser.getSelectedFile();
                        ret = fileChooser.showDialog(null, "Save an encrypted file");
                        if (ret == JFileChooser.APPROVE_OPTION) {
                            File txtFile = fileChooser.getSelectedFile();
                            if(!RSA.encryptFile(blockedFile, txtFile, keys)){
                            	JOptionPane.showMessageDialog(this, "Public key size too small for file");
                            }
                        }
                    }
                } else JOptionPane.showMessageDialog(this, "No public key has been loaded or generated");
            }
            else if (command.equals("Decrypt Blocked")) {
                if (isPrivate) {
                	boolean bsInput = false;
                	while(!bsInput){
                    	String blockSize = JOptionPane.showInputDialog(this,"Enter a blocking size");
                    	 try {
                             int bs = Integer.parseInt(blockSize);
                             if(bs>0){
                            	 RSA.setBlockSize(bs);
                            	 bsInput = true;
                             }
                    	 }
                    	 catch (NumberFormatException e) {
                             JOptionPane.showMessageDialog(this,"Please only enter integers.");
                         }
                	}
                    JFileChooser fileChooser = new JFileChooser();
                    int ret = fileChooser.showDialog(null, "Choose an encrypted file");
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        File blockedFile = fileChooser.getSelectedFile();
                        ret = fileChooser.showDialog(null, "Save a blocked file");
                        if (ret == JFileChooser.APPROVE_OPTION) {
                            File decFile = fileChooser.getSelectedFile();
                            if(!RSA.decryptFile(blockedFile, decFile, keys)){
                            	JOptionPane.showMessageDialog(this, "Private key size too small for file");
                            }
                        }
                    }
                } else JOptionPane.showMessageDialog(this, "No private key has been loaded or generated");
            }
        }
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("RSA encryption");
        RSAMainWindow content = new RSAMainWindow();
        window.setContentPane(content);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocation(250,200);
        window.pack();
        window.setResizable(false);
        window.setVisible(true);
    }
}
