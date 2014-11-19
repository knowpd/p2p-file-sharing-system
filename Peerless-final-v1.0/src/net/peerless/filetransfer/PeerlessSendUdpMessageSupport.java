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

package net.peerless.filetransfer;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import javax.net.ssl.SSLContext;

import net.peerless.client.SearchDialog;
import net.peerless.client.PeerlessClientHandler.Callback;
import net.peerless.common.CommonConfig;
import net.peerless.hashtable.FileSignature;
import net.peerless.message.RequestSignatureMessage;
import net.peerless.message.ResultNotFound;
import net.peerless.message.ResultSignatureMessage;
import net.peerless.message.register;
import net.peerless.ssl.BogusSslContextFactory;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
//import org.apache.mina.example.udp.MemoryMonitor;
//import org.apache.mina.example.udp.client.MemMonClient;
//import org.apache.mina.example.udp.UdpMessage;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeerlessSendUdpMessageSupport extends IoHandlerAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(PeerlessSendUdpMessageSupport.class);

    private IoSession session;

    private IoConnector connector;
   
    private String ipAddress;
    
    private int portNo;
    
    private String query;
    
    private FileSent fileSent;
    
    private SearchDialog Search;
    
    int matches;
    
    public interface FileSent {
    	 void FileList(ResultSignatureMessage signatureResultList);
    }
    
    //private final FileSent fileSent;
    
    public PeerlessSendUdpMessageSupport(String ipAddress, int portNo, String query, SearchDialog Search, int matches){
    	super();
    	this.ipAddress = ipAddress;
    	this.portNo = portNo;
    	this.query = query;
    	this.Search= Search;
    	this.matches=matches;
    	
    }
    
    //added
	private int parsePort(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Illegal port number: " + s);
		}
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
	

	
	//added ends

    /**
     * Default constructor.
     */
    public void connectAndSend() {
    	
    	LOGGER.debug(ipAddress +":"+ portNo);
    	SocketAddress address = parseSocketAddress(ipAddress +":"+ portNo);
   	

        LOGGER.debug("UDPClient::UDPClient");
        LOGGER.debug("Created a datagram connector");
        connector = new NioDatagramConnector();

        LOGGER.debug("Setting the handler");
        connector.setHandler(this);


        IoFilter LOGGING_FILTER = new LoggingFilter();
        IoFilter CODEC_FILTER = new ProtocolCodecFilter(
                new TextLineCodecFactory());                
        connector.getFilterChain().addLast("mdc", new MdcInjectionFilter());
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        connector.getFilterChain().addLast("logger", LOGGING_FILTER);
        
        
        LOGGER.debug("About to connect to the server...");
        ConnectFuture future1 = connector.connect(address);
        future1.awaitUninterruptibly();
        if (!future1.isConnected()) {
            return;
        }
        session = future1.getSession();
 //       try {
			//sendData(fileSignature);
        /*
			UdpMessage m = new UdpMessage();
	        m.setSequence(0);
	        m.setFileSignature(fileSignature);
            session.write(m);*/
        
		RequestSignatureMessage signatureMessage = new RequestSignatureMessage();
		FileSignature fileSignature;
		try {
			fileSignature = new FileSignature(query);
			signatureMessage.setSequence(0);
			signatureMessage.setFileSignature(fileSignature);
			signatureMessage.setMatches(matches);
			System.out.println("Sending the query: " + query);
			System.out.println("[PeerlessSendUdpMessageSupport] file signature" + fileSignature);
	        session.write(signatureMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		//fileSignature.setFileSignature(query);
		

		
		
    }
    


    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
		System.out.println("[PeerlessSendUdpMessSupp] Recieving the damn result");
        LOGGER.debug("Session recv...");
    	UdpMessage am = null;
		if(message instanceof UdpMessage){
		am = (UdpMessage) message;
		String fileSignatureBack = am.getFileSignature();
		System.out.println("This is signature sent back "+ fileSignatureBack);
		}      
		
		ResultSignatureMessage resultSignatureMessageObject = null;
		if(message instanceof ResultSignatureMessage){
			resultSignatureMessageObject = (ResultSignatureMessage) message;
	        String fileSignatureString = new String((resultSignatureMessageObject.getFileSignature()).getFileSignature()); 
			System.out.println("[PeerlessSendUdpMessSupp] Signature, ipAddress & abstract :  "   + fileSignatureString + "  "+ resultSignatureMessageObject.getIpAddress() +" and " + resultSignatureMessageObject.getFileAbstract());
			//fileSent.FileList(resultSignatureMessageObject);
			Search.FileList(resultSignatureMessageObject);
			}   
		
		ResultNotFound resultNotFound = null;
		if(message instanceof ResultNotFound){
			if (CommonConfig.systemVersion == CommonConfig.centralizedVersion) {
				Search.NoResult();
			}
			}  
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        LOGGER.debug("Message sent...");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        LOGGER.debug("Session closed...");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        LOGGER.debug("Session created...");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        LOGGER.debug("Session idle...");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        LOGGER.debug("Session opened...");
    }

//    public static void main(String[] args) {
//        new PeerlessSendUdpMessageSupport();
//    }
	

}
