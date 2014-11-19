/**
 * Copyright (c) 2009 Hee Won Lee <knowpd@gmail.com>
 *
 * This file is part of Peerless's P2P Application.
 *
 * Peerless's P2P Application is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Peerless's P2P Application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.peerless.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.sql.SQLException;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.log4j.Logger;

import net.peerless.client.PeerlessClientHandler.Callback;
import net.peerless.common.CommonConfig;
import net.peerless.filetransfer.PeerlessFtpServerSupport;
import net.peerless.filetransfer.PeerlessSendUdpMessageSupport;
import net.peerless.filetransfer.PeerlessUdpServer;
import net.peerless.hashtable.SigHashTable;
import net.peerless.server.PeerlessProtocolHandler;
import net.peerless.server.ServerConfig;
import net.peerless.client.SearchDialog;
import net.peerless.db.PeerlessDbSupport;
import net.peerless.dist.SuperPeerManager;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * Simple chat client based on Swing & MINA that implements the chat protocol.
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class PeerlessClientMain extends JFrame implements Callback,
		ActionListener, ItemListener {
	private static final long serialVersionUID = 1538675161745436968L;

	private Logger log = Logger.getLogger(PeerlessProtocolHandler.class);

	JTextArea output;
	JScrollPane scrollPane;

	public JMenuBar menuBar;
	public JMenu menu, submenu;
	public JMenuItem menuItem;
	public JRadioButtonMenuItem rbMenuItem;
	public JCheckBoxMenuItem cbMenuItem;
	public int check = 0;
	private NioSocketConnector connector;
	private PeerlessClientHandler handler;
	private PeerlessClientSupport client;
	private IoSession session;
	public static JFrame frame;
	public SearchDialog dialog;
	public SuperPeerManager superPeerIP = new SuperPeerManager();
	public static PeerlessDbSupport clientDbSupport;
	public static SigHashTable sht;

	public JMenuBar createMenuBar(final JFrame frame) {

		connector = new NioSocketConnector();

		// Create the menu bar.
		menuBar = new JMenuBar();

		// Build the first menu.
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
				"The only menu in this program that has menu items");
		menuBar.add(menu);

		// a group of JMenuItems
		menuItem = new JMenuItem("Login", KeyEvent.VK_T);
		// menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"This doesn't really do anything");
		menu.add(menuItem);

		menuItem.addActionListener(this);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ConnectDialog dialog = new ConnectDialog(
						PeerlessClientMain.this);
				dialog.pack();
				dialog.setVisible(true);

				if (dialog.isCancelled()) {
					return;
				}

				SocketAddress address = parseSocketAddress(dialog
						.getServerAddress());
				String name = dialog.getUsername();
				String pass = dialog.getpassword();
				String id = dialog.getID();

				handler = new PeerlessClientHandler(PeerlessClientMain.this,
						superPeerIP);
				client = new PeerlessClientSupport(name, pass, id, handler);
				// nameField.setText(name);
				// serverField.setText(dialog.getServerAddress());
				// idField.setText(id);
				check = 1;
				if (!client.connect(connector, address, dialog.isUseSsl())) {
					JOptionPane.showMessageDialog(PeerlessClientMain.this,
							"Could not connect to " + dialog.getServerAddress()
									+ ". ");
					check = 0;
				}
				
				

			}

		});

		menuItem = new JMenuItem("Register", KeyEvent.VK_F);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3,
				ActionEvent.ALT_MASK));
		menu.add(menuItem);

		menuItem.addActionListener(this);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				RegisterDialog dialog = new RegisterDialog(frame);
				dialog.pack();
				dialog.setVisible(true);

				if (dialog.isCancelled()) {
					return;
				}

				SocketAddress address = parseSocketAddress(ClientConfig.peerlessServerAddress);

				String name = dialog.getUserName();
				String id = dialog.getUserId();
				String password = dialog.getUserPassword();
				String question = dialog.getUserQuestion();
				String passwordReminder = dialog.getUserPasswordReminder();

				handler = new PeerlessClientHandler(PeerlessClientMain.this,
						superPeerIP);
				client = new PeerlessClientSupport(name, id, password,
						question, passwordReminder, handler);
				// nameField.setText(name);
				// idField.setText(id);

				// serverField.setText(dialog.getServerAddress());
				// serverField.setText(ClientConfig.peerlessServerAddress);

				if (!client.register(connector, address, name, id, password,
						question, passwordReminder, dialog.isUseSsl())) {
					JOptionPane
							.showMessageDialog(
									PeerlessClientMain.this,
									"Could not connect to "
											+ ClientConfig.peerlessServerAddress
											+ ". ");
				}
				

			}

		});

		menuItem = new JMenuItem("Logoff", KeyEvent.VK_B);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
				ActionEvent.ALT_MASK));
		menu.add(menuItem);

		menuItem.addActionListener(this);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (client != null) {
					client.quit();
					check = 0;
				}

				JOptionPane
						.showMessageDialog(
								menuItem,
								"                           YOU HAVE LOGGED OUT!! "
										+ "\nTHANK YOU FOR USING THE PEERLESS P2P APPLICATION!!");
				frame.dispose();
			}

		});

		menuItem = new JMenuItem("Close", KeyEvent.VK_V);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4,
				ActionEvent.ALT_MASK));
		menu.add(menuItem);

		menuItem.addActionListener(this);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				check = 0;
				if (client != null) {
					client.quit();
					check = 0;
				}
				frame.dispose();

			}

		});

		// Build second menu in the menu bar.
		menu = new JMenu("Options");
		menu.setMnemonic(KeyEvent.VK_N);
		menu.getAccessibleContext().setAccessibleDescription(
				"This menu does nothing");
		menuBar.add(menu);

		menuItem = new JMenuItem("Set Shared Folder", KeyEvent.VK_B);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7,
				ActionEvent.ALT_MASK));
		menu.add(menuItem);

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser(".");
				// fileChooser.setMultiSelectionEnabled(true);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int status = fileChooser.showOpenDialog(null);
				/*
				 * if (status == JFileChooser.APPROVE_OPTION) { // File
				 * selectedFiles[] = fileChooser.getSelectedFiles(); // for (int
				 * i = 0, n = selectedFiles.length; i < n; i++) { //
				 * System.out.println("Selected: " // +
				 * selectedFiles[i].getParent() + " --- " // +
				 * selectedFiles[i].getName()); // }
				 * 
				 * File selectedFolder = fileChooser.getSelectedFile();
				 * //ClientConfig.sharedFolder =
				 * selectedFolder.getAbsolutePath();
				 * System.out.println("ClientConfig.sharedFolder = " +
				 * ClientConfig.sharedFolder);
				 * 
				 * }
				 */
				File selectedFolder = fileChooser.getSelectedFile();
				ClientConfig.sharedFolder = selectedFolder.getAbsolutePath();
				System.out.println("ClientConfig.sharedFolder = "
						+ ClientConfig.sharedFolder);
				DirectoryReader.printFilesOfSharedFolder();

			}
		});

		// Build third menu in the menu bar.
		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_N);
		menu.getAccessibleContext().setAccessibleDescription(
				"This menu does nothing");
		menuBar.add(menu);

		menuItem = new JMenuItem("About", KeyEvent.VK_B);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6,
				ActionEvent.ALT_MASK));
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(menuItem,
						"This is a Peer to Peer File Sharing Application developed "
								+ "by the Team Peerless!! ");
			}
		});
		/*
		 * if(ActionEvent.ALT_MASK) { JOptionPane.showMessageDialog(menuItem,
		 * "YOU NEED TO LOGIN BEFORE YOU DOWNLOAD!!! "); }
		 */

		return menuBar;
	}

	private void append(String text) {

		// System.out.println("This is the message Received: " + text);
		dialog.append(text);
	}

	private void notifyError(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	private SocketAddress parseSocketAddress(String s) {
		s = s.trim();
		int colonIndex = s.indexOf(":");
		if (colonIndex > 0) {
			String host = s.substring(0, colonIndex);
			int port = parsePort(s.substring(colonIndex + 1));
			return new InetSocketAddress(host, port);
		} else {
			int port = parsePort(s.substring(colonIndex + 1));
			return new InetSocketAddress(port);
		}
	}

	private int parsePort(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Illegal port number: " + s);
		}
	}

	public void connected() {
	}

	public void disconnected() {
		append("Connection closed.\n");
		// setLoggedOut();
	}

	public void error(String message) {
		notifyError(message + "\n");
		frame.dispose();
	}

	public void loggedIn(String message, IoSession session) {
		// setLoggedIn();
		this.session = session;
		dialog = new SearchDialog(frame, superPeerIP, session);
		dialog.append(message);
		// append("You have joined the chat session.\n");
	}

	public void registered() {
		// setRegistered();
		JOptionPane.showMessageDialog(menuItem,
				"                           YOU HAVE BEEN REGISTERED!! "
						+ "\nLOGIN NOW TO USE THE PEERLESS P2P APPLICATION!!");
	}

	public void loggedOut() {
		append("You have left the left the Peerless P2P File Sharing Application.\n");
		// setLoggedOut();
	}

	public void messageReceived(String message) {
		if (message != "NULL")
			append(message + "\n");
	}

	public Container createContentPane() {
		// Create the content-pane-to-be.
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setOpaque(true);

		// Create a scrolled text area.
		output = new JTextArea(5, 30);
		output.setEditable(false);

		return contentPane;
	}

	private static void createAndShowGUI() {
		
		
		// Create and set up the window.
		frame = new JFrame("Peerless");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		PeerlessClientMain demo = new PeerlessClientMain();
		frame.setJMenuBar(demo.createMenuBar(frame));
		frame.setContentPane(demo.createContentPane());

		// Display the window.
		frame.setSize(800, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {

		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.

		/*
		 * use this coden servr main later SigHashTable sht = new
		 * SigHashTable(); PeerlessDbSupport peerlessDbSupport = new
		 * PeerlessDbSupport("peerlessdb");
		 * 
		 * try { new PeerlessUdpServer(sht, peerlessDbSupport); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		sht = new SigHashTable();
		clientDbSupport = new PeerlessDbSupport(ClientConfig.ClientDb);
		try {
			clientDbSupport.connect();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		clientDbSupport.createSchema();
		


		PeerlessFtpServerSupport ser = new PeerlessFtpServerSupport(ClientConfig.ftpPort);

		try {
			ser.startServer();
		} catch (FtpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
		
		try {
			new PeerlessUdpServer(clientDbSupport, sht);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("This works because of Action Event!!");
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		System.out.println("This works because of Item Event!!");

	}
}
