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

package net.peerless.server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.HashMap;


import net.peerless.common.CommonConfig;
import net.peerless.message.FileInfoMessage;
import net.peerless.message.FileInfoSuperPeer;
import net.peerless.message.ReqSuperInv;
import net.peerless.message.RequestMessage;
import net.peerless.message.broadcast;
import net.peerless.message.register;
import net.peerless.message.quit;
import net.peerless.message.Servermessage;
import net.peerless.message.AcceptSuperInv;
import net.peerless.ssl.BogusSslContextFactory;
import net.peerless.db.*;
import net.peerless.dist.SuperPeerManager;
import net.peerless.hashtable.FileAbstract;
import net.peerless.hashtable.FileSignature;
import net.peerless.hashtable.SigHashTable;
import net.peerless.server.PeerleeServerCommSupport;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.net.ssl.SSLContext;

/**
 * {@link IoHandler} implementation of a simple chat server protocol.
 * 
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version $Rev$, $Date$
 */
public class PeerlessProtocolHandler extends IoHandlerAdapter {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(PeerlessProtocolHandler.class);

	private final Set<IoSession> sessions = Collections
			.synchronizedSet(new HashSet<IoSession>());

	private final Set<String> users = Collections
			.synchronizedSet(new HashSet<String>());
	
	private final Set<String> superusers = Collections
			.synchronizedSet(new HashSet<String>());
	
	
	private List superusersIP = Collections.synchronizedList(new ArrayList());
	
	private final Set<String> ToBesuper = Collections
		.synchronizedSet(new HashSet<String>());
	
    private HashMap<String, IoSession> normalU = new HashMap<String, IoSession>();
		
	private String theCommand;
	public  String Type;
	private SigHashTable sht;
	private PeerlessDbSupport peerlessDbSupport; 
	private SuperPeerManager superPeerManager;
	
	public PeerlessProtocolHandler(SigHashTable sht, 	PeerlessDbSupport peerlessDbSupport, SuperPeerManager 	superPeerManager) {
		super();
		this.sht = sht;
		this.peerlessDbSupport = peerlessDbSupport;
		this.superPeerManager = superPeerManager;
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		LOGGER.warn("Unexpected exception.", cause);
		// Close connection when unexpected exception is caught.
		session.close(true);
	}

	// @Override
	public void  messageReceived(IoSession session, Object message) {
		
		

		
		
		Logger log = LoggerFactory.getLogger(PeerlessProtocolHandler.class);
		log.info("received: " + message);
		

		RequestMessage am = null;
		if (message instanceof RequestMessage) {
			am = (RequestMessage) message;
			theCommand = am.getCommand();
			System.out.println("This is Command " + theCommand);
			
		}

		broadcast point = null;
		if (message instanceof broadcast) {
			point = (broadcast) message;
			theCommand = point.getcommand();

			System.out.println("This is Command " + theCommand);
		}

		register reg = null;
		if (message instanceof register) {
			reg = (register) message;
			theCommand = reg.getCommand();

			System.out.println("This is Command " + theCommand);
		}

		quit q = null;
		if (message instanceof quit) {
			q = (quit) message;
			theCommand = q.getCommand();
		}
		
		AcceptSuperInv accept = null;
		if (message instanceof AcceptSuperInv) {
			accept = (AcceptSuperInv) message;
			theCommand = accept.getCommand();
		}
		
		FileInfoMessage fileinfo = null;
		if (message instanceof FileInfoMessage) {
			System.out.println("Server has received file info message");
			fileinfo = (FileInfoMessage) message;
			theCommand = fileinfo.getCommand();
			System.out.println("This is Command " + theCommand);		
		}

		
		int speer=0;
		int flaglog = 0;
		int flag = 0;
		try {
			
			PeerlessCommand command = PeerlessCommand.valueOf(theCommand);
			String user = (String) session.getAttribute("user");
			
			

			switch (command.toInt()) {

			case PeerlessCommand.QUIT:
				Servermessage response = new Servermessage();
				response.setSequence(0);
				response.setCommand("QUIT");
				response.setStatus("QUIT OK");
				session.write(response);
				session.close(true);
				String userdel = (String) session.getAttribute("user");
				String clientipaddress = session.getRemoteAddress().toString();
				ToBesuper.clear();
				if (users.contains(userdel)){	
					PeerleeServerCommSupport sendsuperdel = new PeerleeServerCommSupport(peerlessDbSupport);
					sendsuperdel.DeleteUser(userdel);
					sendsuperdel.DeleteFiles(clientipaddress);
					if (superusers.contains(userdel)) superusers.remove(userdel);
					if (superusersIP.contains(clientipaddress)) superusersIP.remove(clientipaddress);
					if (normalU.containsKey(userdel)) normalU.remove(userdel);
					if ((superusers.size() < 4) && (normalU.size() > 0)){
							PeerleeServerCommSupport getnormal = new PeerleeServerCommSupport(peerlessDbSupport);
							String normal = getnormal.getnormal();	
							session = normalU.get(normal);
							ReqSuperInv reqsuper = new ReqSuperInv();
							reqsuper.setSequence(0);
							reqsuper.setCommand("REQUEST SUPER");
							if (normal != "NULL") session.write(reqsuper);
					}
					
				}
				break;
				
			case PeerlessCommand.ACCEPT:
				
				String NewSuperUser = (String) session.getAttribute("user");
				if((superusers.size() < 4) && (normalU.size() > 0)){
					String IPAdd1 = session.getRemoteAddress().toString();
					System.out.println("This is the New Super Peer IP: " + IPAdd1);
					String clientip = session.getRemoteAddress().toString();
					superusersIP.add(clientip);
					superusers.add(NewSuperUser);
					if (normalU.containsKey(NewSuperUser)) normalU.remove(NewSuperUser);
					PeerleeServerCommSupport newsuper = new PeerleeServerCommSupport(peerlessDbSupport);
					newsuper.NewSuperUser(NewSuperUser);
					newsuper.GetIPList(session, sessions);
				}
				break;
				
			case PeerlessCommand.LOGIN:

				String pass = am.getpassword();
				System.out.println("This is password " + pass);
				String user1 = am.getUserId();
				System.out.println("This is userid " + user1);


				if (user1.isEmpty() || pass.isEmpty()) {
				
					Servermessage responselogin = new Servermessage();
					responselogin.setSequence(0);
					responselogin.setCommand("LOGIN");
					responselogin.setStatus("NOK");
					responselogin.setMessage("LOGIN ERROR: Invalid Login Command.");
					session.write(responselogin);
					return;
				} else {
					user = user1;
					session.write("This is user" + user);
				}

				
				if (users.contains(user)) {
					
					Servermessage responselogin = new Servermessage();
					responselogin.setSequence(0);
					responselogin.setCommand("LOGIN");
					responselogin.setStatus("NOK");
					responselogin.setMessage("LOGIN ERROR The UserID " + user + " is already used.");
					session.write(responselogin);
					return;
				}

				try {
					FileReader input = new FileReader(ServerConfig.RegisteredUsersFile);

					BufferedReader bufRead = new BufferedReader(input);

					String line; // String that holds current file line
					int countline = 0; // Line number of count

					// Read first line
					line = bufRead.readLine();
					countline++;

					// Read through file one line at time. Print line # and line
					while (line != null) {
						String[] arr = line.split(" ", 2);

						if (user.equals(arr[0]) && pass.equals(arr[1])) {
							flaglog = 1;
							break;
						} else
							flaglog = 0;
						line = bufRead.readLine();
						countline++;
					}

					bufRead.close();

				} catch (ArrayIndexOutOfBoundsException e) {
					
					System.out.println("Usage: java ReadFile filename\n");

				} catch (IOException e) {
					// If another exception is generated, print a stack trace
					e.printStackTrace();
				}
				
				
				// check if the username and password match
				if (flaglog == 0) {
					Servermessage responselogin = new Servermessage();
					responselogin.setSequence(0);
					responselogin.setCommand("LOGIN");
					responselogin.setStatus("NOK");
					responselogin.setMessage("Login ERROR: Username & Password don't match");
					session.write(responselogin);
					
					return;
				}

				//Add the user if username and password match
				sessions.add(session);
				session.setAttribute("user", user);
						
				String IPAdd = session.getRemoteAddress().toString();
				System.out.println("This is the Client NAT IP: " + IPAdd);
				String PvtIP = am.getIPAddress();
				PeerleeServerCommSupport sendsuper = new PeerleeServerCommSupport(peerlessDbSupport);
				Type = "NORMAL"; 
				sendsuper.SendSuperpeerIP(user, IPAdd, session, Type, PvtIP);
				normalU.put(user, session);
				
				if ((superusers.size() < 4) && (normalU.size() > 0)){
				ReqSuperInv reqsuper = new ReqSuperInv();
				reqsuper.setSequence(0);
				reqsuper.setCommand("REQUEST SUPER");
				session.write(reqsuper);
				}
				
				MdcInjectionFilter.setProperty(session, "user", user);

				// Send LOGIN OK message to the client.
				users.add(user);
				Servermessage responselogin = new Servermessage();
				responselogin.setSequence(0);
				responselogin.setCommand("LOGIN");
				responselogin.setStatus("LOGIN OK");
				responselogin.setMessage("The User " + user + " has joined the Peerless P2P File Sharing Application.");
				session.write(responselogin);
				//broadcast("The User " + user + " has joined the chat session.");
				break;

			case PeerlessCommand.REGISTER:

				String user3 = reg.getUserId();
				user = user3;

				String data0 = reg.getUserId();
				// String data1 = reg.name;
				String data2 = reg.getpassword();
				String data3 = reg.getQuestion();
				String data4 = reg.getpasswordReminder();

				System.out.println("This is data0 " + data0);
				System.out.println("This is data2 " + data2);
				// log.debug("data.length = " + data.length);
				if (data0.isEmpty() || data2.isEmpty()) {

					//session.write("REGISTER ERROR invalid register command :| .");
					Servermessage responsereg = new Servermessage();
					responsereg.setSequence(0);
					responsereg.setCommand("REGISTER");
					responsereg.setStatus("NOK");
					responsereg.setMessage("REGISTER ERROR: Invalid Register Command");
					session.write(responsereg);
					return;
				} else {

					try {

						FileReader input = new FileReader(ServerConfig.RegisteredUsersFile);

						BufferedReader bufRead = new BufferedReader(input);

						String line; // String that holds current file line
						int count = 0; // Line number of count

						// Read first line
						line = bufRead.readLine();
						count++;

						while (line != null) {
							String[] arr = line.split(" ", 2);
							if (data0.equals(arr[0]))
								flag = 1;
							line = bufRead.readLine();
							count++;
						}

						bufRead.close();

					} catch (ArrayIndexOutOfBoundsException e) {
						
						System.out.println("Usage: java ReadFile filename\n");

					} catch (IOException e) {
						// If another exception is generated, print a stack
						// trace
						e.printStackTrace();
					}

					// check if the username is already used
					if (flag == 1) {
						
						Servermessage responsereg = new Servermessage();
						responsereg.setSequence(0);
						responsereg.setCommand("REGISTER");
						responsereg.setStatus("NOK");
						responsereg.setMessage("REGISTER ERROR: The UserID " + user + " is already registered.");
						session.write(responsereg);
						return;
					}

					try {
						FileWriter outFile = new FileWriter(ServerConfig.RegisteredUsersFile, true);
						PrintWriter out = new PrintWriter(outFile);

						out.println(user + " " + data2);
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

				sessions.add(session);
				session.setAttribute("user", user);

				// Allow all users
				users.add(user);
				Servermessage responsereg = new Servermessage();
				responsereg.setSequence(0);
				responsereg.setCommand("REGISTER");
				responsereg.setStatus("REGISTER OK");
				session.write(responsereg);
				//broadcast("The User " + user + " has Registered.");

				break;
				
			case PeerlessCommand.PUBLISH:
				FileSignature signature = fileinfo.getFileSignature();
				FileAbstract fileabstract = fileinfo.getFileAbstract();
				String Pvtclientip = fileinfo.getPvtIPAdd();
				String Filename = fileinfo.getFilename();
				System.out.println("[PUBLISH] This is the Client Pvt IP: " + Pvtclientip);
				//Add the user if username and password match
			//	sessions.add(session);
			//	session.setAttribute("user", user);		
				String clientip = session.getRemoteAddress().toString();
				System.out.println("[PUBLISH] This is the Client Public IP: " + clientip);
				
				FileInfoSuperPeer msg = new FileInfoSuperPeer();
	    		msg.setSequence(0);
	    		msg.setCommand("PUBLISHSP");
	    		msg.setFileSignature(signature);
	    		msg.setFileAbstract(fileabstract);
	    		String File = fileabstract.getfileName();
	    		msg.setFilename(File);
	    		String ipadd = clientip;
	    		System.out.println("The Private IP Address of the File being Published: "+ ipadd);
	    		msg.setPvtIPAdd(Pvtclientip);
	    		msg.setPublicIPAdd(clientip);
	    		if (CommonConfig.systemVersion == CommonConfig.centralizedVersion)
	    		{	
				sht.addSignature(signature);
				
				PeerleeServerCommSupport filepublish = new PeerleeServerCommSupport(peerlessDbSupport);
				filepublish.addFileInformation(clientip, signature, fileabstract, Pvtclientip, Filename);
	    		}
				int hash = superPeerManager.findHashSpace(signature, superusersIP.size());
				String superpeerip = (String) superusersIP.get(hash);
				
				Iterator<IoSession> iter = sessions.iterator();
				if (CommonConfig.systemVersion == CommonConfig.distributedVersion)
				{	
			    while (iter.hasNext()) {
			    	IoSession spsession = iter.next();
			    	String speerip = spsession.getRemoteAddress().toString();
			    	if(speerip.compareTo(superpeerip)== 0){
			    		System.out.println("Publishing to super peer ip: "+speerip);
			    		spsession.write(msg);
			    		break;
			    	}
			    }
				}
				//MdcInjectionFilter.setProperty(session, "user", user);
				// Send LOGIN OK message to the client.
				//users.add(user);
				Servermessage publishresponse = new Servermessage();
				publishresponse.setSequence(0);
				publishresponse.setCommand("PUBLISH");
				publishresponse.setStatus("PUBLISH OK");
				session.write(publishresponse);
				break;


			case PeerlessCommand.FILETRANSFER:
				/*
				 * File myFile = new File("E:\\hwlee\\testfile-out.txt");
				 * FileOutputStream fos = new FileOutputStream(myFile);
				 * ByteBuffer buf = ByteBuffer.allocate(16);
				 * buf.setAutoExpand(true); buf = (ByteBuffer) message; byte[]
				 * mybytearray = new byte[(int) buf.capacity()];
				 * 
				 * fos.write(mybytearray); // fc.read(buf); buf.flip();
				 */
				break;

			case PeerlessCommand.BROADCAST:
				String user2 = point.getUserId();
				String messagerec = point.getmessage();
				broadcast(user2 + ": " + messagerec);
				break;
			default:
				LOGGER.info("Unhandled command: " + command);
				break;
			}

		} catch (IllegalArgumentException e) {
			LOGGER.debug("Illegal argument" + e);
		}
	}

	public void broadcast(String message) {
		synchronized (sessions) {
			for (IoSession session : sessions) {
				if (session.isConnected()) {
					Servermessage response = new Servermessage();
					response.setSequence(0);
					response.setCommand("BROADCAST");
					response.setMessage(message);
					response.setStatus("BROADCAST OK");
					session.write(response);
					//session.write("BROADCAST OK " + message);
				}
			}
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		
		String user = (String) session.getAttribute("user");
		users.remove(user);
		sessions.remove(session);
		broadcast("The user " + user + " has left the Peerless P2P File Sharing Application.");
	}

	public boolean isChatUser(String name) {
		return users.contains(name);
	}

	public int getNumberOfUsers() {
		return users.size();
	}

	public void kick(String name) {
		synchronized (sessions) {
			for (IoSession session : sessions) {
				if (name.equals(session.getAttribute("user"))) {
					session.close(true);
					break;
				}
			}
		}
	}
}
