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

import java.net.SocketAddress;

import net.peerless.message.RequestSignatureMessage;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
//import org.apache.mina.example.udp.MemoryMonitor;
//import org.apache.mina.example.udp.UdpMessage;

public class PeerlessUdpServerHandler extends IoHandlerAdapter {
	
    private PeerlessUdpServer server;
    


    public PeerlessUdpServerHandler(PeerlessUdpServer server) {
        this.server = server;
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        session.close(true);
    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
    	System.out.println("[PeerlessUdpServerHandler] just came to server");
    	UdpMessage am = null;
        SocketAddress remoteAddress = session.getRemoteAddress();
        
		if(message instanceof UdpMessage){
		am = (UdpMessage) message;
		String fileSignature = am.getFileSignature();
		System.out.println("This is signature "+ fileSignature);
        server.recvUpdate(remoteAddress, fileSignature, session);
		}
		
		RequestSignatureMessage signatureMessage = null;
		
		if(message instanceof RequestSignatureMessage){	
			signatureMessage=(RequestSignatureMessage)message;
	        server.recvUpdate(remoteAddress, signatureMessage, session);
		}
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("Session closed...");
        SocketAddress remoteAddress = session.getRemoteAddress();
        server.removeClient(remoteAddress, session);

    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {

        System.out.println("Session created...");
        SocketAddress remoteAddress = session.getRemoteAddress();
        server.addClient(remoteAddress, session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        System.out.println("Session idle...");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("Session Opened...");
    }

}
