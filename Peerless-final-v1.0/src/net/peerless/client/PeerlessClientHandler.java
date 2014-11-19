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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.peerless.server.PeerleeServerCommSupport;
import net.peerless.server.PeerlessCommand;


import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import net.peerless.db.PeerlessDbSupport;
import net.peerless.dist.PeerIpAddress;
import net.peerless.dist.SuperPeerManager;
import net.peerless.hashtable.FileAbstract;
import net.peerless.hashtable.FileSignature;
import net.peerless.hashtable.SigHashTable;
import net.peerless.message.*;

/**
 * {@link IoHandler} implementation of the client side of the simple chat protocol.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class PeerlessClientHandler extends IoHandlerAdapter {


	
    public interface Callback {
        void connected();

        void loggedIn(String message, IoSession session);
        
        void registered();

        void loggedOut();

        void disconnected();

        void messageReceived(String message);

        void error(String message);
    }

    private final Callback callback;
    
    private SuperPeerManager SuperPeerIP;

    public PeerlessClientHandler(Callback callback, SuperPeerManager SuperPeerIP) {
        this.callback = callback;
        this.SuperPeerIP = SuperPeerIP;
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        callback.connected();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
    	System.out.println("This is the debug point 0: ");
    	
    	if (message instanceof SendSuperIP) { 
    		
    	SendSuperIP theMessage = (SendSuperIP) message;
    	List ipAddrList = Collections.synchronizedList(new ArrayList());
    	ipAddrList = theMessage.getIPList();
    	String IPadd = ipAddrList.get(0).toString();
		System.out.println("Public IP of Superpeer Received at Client:" + IPadd);
		
		List pvtipAddrList = Collections.synchronizedList(new ArrayList());
    	pvtipAddrList = theMessage.getPvtIPList();
    	String IPadd1 = pvtipAddrList.get(0).toString();
		System.out.println("Private IP of Superpeer Received at Client:" + IPadd1);
   
		SuperPeerIP.EmptyIPList();
		
		for(int i = 0; i<ipAddrList.size(); i++){
			String PublicIP = ipAddrList.get(i).toString();
			String PvtIP = pvtipAddrList.get(i).toString();
			PeerIpAddress PeerIP = new PeerIpAddress(PublicIP,PvtIP);
			SuperPeerIP.AddIPtoList(PeerIP);
		}
    	}
    	
    	/*else if (message instanceof SendSuperPvtIP) {
    		
    		SendSuperPvtIP theMessage = (SendSuperPvtIP) message;
        	List ipAddrList = Collections.synchronizedList(new ArrayList());
        	ipAddrList = theMessage.getIPList();
        	String IPadd = ipAddrList.get(0).toString();
    		System.out.println("Private IP of Superpeer Received at Client:" + IPadd);
    		
    	}*/
    	
    	else if (message instanceof ReqSuperInv) {
    		ReqSuperInv theMessage = (ReqSuperInv) message;
    		String Commandrecv = theMessage.getCommand();
    		if (Commandrecv.equals("REQUEST SUPER")) {
        		AcceptSuperInv accept = new AcceptSuperInv();
        		accept.setSequence(0);
        		accept.setCommand("ACCEPT");
        		session.write(accept);	
        		}
    	}
    	
    	else if (message instanceof FileInfoSuperPeer){
    		FileInfoSuperPeer msg = (FileInfoSuperPeer)message;
    		FileSignature signature = msg.getFileSignature();
			FileAbstract fileabstract = msg.getFileAbstract();
			String Pvtclientip = msg.getPvtIPAdd();
			String Filename = msg.getFilename();
			String ip = msg.getPublicIPAdd();
			
			PeerlessClientMain.sht.addSignature(signature);
			PeerlessClientCommSupport filepublish = new PeerlessClientCommSupport();
			filepublish.addFileInformation(ip, signature, fileabstract, Pvtclientip, Filename);
    	}
    	
    	else {
        Servermessage theMessage = (Servermessage) message;
      
        String status = theMessage.getStatus();
        String theCommand = theMessage.getCommand();
        String messagerecv = theMessage.getMessage();
        
        System.out.println("This is the debug point 1: " + messagerecv);

        if (("BROADCAST OK".equals(status)) && ("BROADCAST".equals(theCommand))) {
        		callback.messageReceived(messagerecv);
        		}
       
        else if (("LOGIN OK".equals(status)) && ("LOGIN".equals(theCommand))) {
        	callback.loggedIn(messagerecv, session);
        	PeerlessClientCommSupport peerlessClientCommSupport = new PeerlessClientCommSupport(session);
        	peerlessClientCommSupport.publishFileAll();
            }
            
        else if (("REGISTER OK".equals(status)) && ("REGISTER".equals(theCommand))) {
        	callback.registered();
            }
         
        else if (("QUIT OK".equals(status)) && ("QUIT".equals(theCommand))) {
        	callback.loggedOut();
            }
        
        else if (("PUBLISH OK".equals(status)) && ("PUBLISH".equals(theCommand))) {
        	System.out.println("Your Files have been published on the SERVER!!");
            }
           
        else if ("NOK".equals(status)) callback.error(messagerecv);
            
        }}
    

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        callback.disconnected();
    }

}
