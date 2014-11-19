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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.peerless.db.PeerlessDbSupport;
import net.peerless.hashtable.FileSignature;
import net.peerless.hashtable.FileSignatureMatch;
import net.peerless.hashtable.SigHashTable;
import net.peerless.message.RequestSignatureMessage;
import net.peerless.message.ResultNotFound;
import net.peerless.message.ResultSignatureMessage;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IoSession;
//import org.apache.mina.example.udp.UdpMessage;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

public class PeerlessUdpServer {
    private static final long serialVersionUID = 1L;

    public static final int PORT = 18567;
    
    private SigHashTable sht;
    
    private PeerlessDbSupport peerlessDbSupport;

    /*protected static final Dimension PANEL_SIZE = new Dimension(300, 200);

    private JFrame frame;

    private JTabbedPane tabbedPane;*/
    
	private final Set<IoSession> sessions = Collections
	.synchronizedSet(new HashSet<IoSession>());

	private final Set<SocketAddress> SockAddrs = Collections
	.synchronizedSet(new HashSet<SocketAddress>());   


    public PeerlessUdpServer(PeerlessDbSupport peerlessDbSupport, SigHashTable sht) throws IOException {
    	
    	this.sht=sht;
    	this.peerlessDbSupport=peerlessDbSupport;

        NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
        acceptor.setHandler(new PeerlessUdpServerHandler(this));

        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        chain.addLast("logger", new LoggingFilter());

        DatagramSessionConfig dcfg = acceptor.getSessionConfig();
        dcfg.setReuseAddress(true);

	    chain.addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));        
        
         acceptor.bind(new InetSocketAddress(PORT));
        System.out.println("UDPServer listening on port " + PORT);
    }


    protected void recvUpdate(SocketAddress clientAddr, String fileSignature, IoSession session) {

        System.out.println("At server: New value for textfield - " + fileSignature + clientAddr);
        
		UdpMessage m = new UdpMessage();
        m.setSequence(0);
        m.setFileSignature(fileSignature);
        session.write(m);
    }
    
    protected void recvUpdate(SocketAddress clientAddr, RequestSignatureMessage udpSignatureMeassage, IoSession session) {
    		
    	//call PeerlessDbSupport readdata function
		//String dbName = "firstdb";
		//PeerlessDbSupport peerlessDbSupport = new PeerlessDbSupport(dbName);
		/*try {
			peerlessDbSupport.connect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		int signNo =0;
		int flag=0;
		
		FileSignature fileSignature= new FileSignature();
		fileSignature = udpSignatureMeassage.getFileSignature();
		System.out.println("[PeerlessUdpServer] signature sent " + fileSignature);
		
		System.out.println("[PeerlessUdpServer]about to call hashtable");
		//SigHashTable signatureListResult= new SigHashTable();
		List<FileSignatureMatch> signatureList=  sht.searchSignature(fileSignature,10,0);
		System.out.println("[PeerlessUdpServer]about to call readata");
		for(FileSignatureMatch listOfSig : signatureList)
		{	
				System.out.println("[PeerlessUdpServer] signature list about to be passed: " + listOfSig.getFSig());
				try {		
					if(udpSignatureMeassage.getMatches()>signNo)
					{	
						List<ResultSignatureMessage> searchResult = peerlessDbSupport.readData(listOfSig.getFSig());
						//List<ResultSignatureMessage> searchResult = peerlessDbSupport.readData(fileSignature);				
						 System.out.println("[PeerlessUdpServer] list of signatures recieved" + listOfSig.getFSig());
							for(ResultSignatureMessage resultMessageObject : searchResult)
							{
								resultMessageObject.setSequence(0);
								resultMessageObject.setSignatureNumber(signNo);
								signNo = signNo + 1;
								  System.out.println("ipAddress and abstract "    + resultMessageObject.getIpAddress() +" and " + resultMessageObject.getFileAbstract());
								  flag=1;
								  session.write(resultMessageObject);
							}
							
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
					
		}
		if(flag==0)
		{
			ResultNotFound resultNotFound = new ResultNotFound();
			resultNotFound.setResultNotFound(0);
			session.write(resultNotFound);
		}
    }

    protected void addClient(SocketAddress clientAddr, IoSession session) {
        if (!containsClient(clientAddr)) {

			sessions.add(session);
			session.setAttribute("socketAddress", clientAddr);
			SockAddrs.add(clientAddr);


        }
    }

    protected boolean containsClient(SocketAddress clientAddr) {
        return SockAddrs.contains(clientAddr);
    }

    protected void removeClient(SocketAddress clientAddr, IoSession session) {

        sessions.remove(session);
        SockAddrs.remove(clientAddr);
    }

//    public static void main(String[] args) throws IOException {
//        new PeerlessUdpServer();
  
//}

}
