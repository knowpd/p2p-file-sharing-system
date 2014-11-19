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

import java.net.SocketAddress;

import javax.net.ssl.SSLContext;

import net.peerless.message.RequestMessage;
import net.peerless.message.broadcast;
import net.peerless.message.register;
import net.peerless.message.quit;
import net.peerless.server.PeerlessProtocolHandler;
import net.peerless.ssl.BogusSslContextFactory;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import net.peerless.common.CommonConfig;

;

/**
 * A simple chat client for a given user.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class PeerlessClientSupport {
	
	private Logger log = Logger.getLogger(PeerlessProtocolHandler.class);
	
	private final IoHandler handler;

	private final String name;

	private String id;

	private String password;

	private String question;

	private String passwordReminder;

	private IoSession session;

	public PeerlessClientSupport(String name, String pass, String id, IoHandler handler) {
		if (name == null) {
			throw new IllegalArgumentException("Name can not be null");
		}
		this.name = name;
		this.password = pass;
		this.handler = handler;
		this.id = id;
	}

	public PeerlessClientSupport(String name, String id, String password,
			String question, String passwordReminder, IoHandler handler) {
		if (id == null) {
			throw new IllegalArgumentException("ID can not be null");
		}
		this.name = name;
		this.id = id;
		this.password = password;
		this.question = question;
		this.passwordReminder = passwordReminder;
		this.handler = handler;

	}

    public boolean connect(NioSocketConnector connector, SocketAddress address,
            boolean useSsl) {
        if (session != null && session.isConnected()) {
            throw new IllegalStateException(
                    "Already connected. Disconnect first.");
        }

        try {
            IoFilter LOGGING_FILTER = new LoggingFilter();

            IoFilter CODEC_FILTER = new ProtocolCodecFilter(
                    new TextLineCodecFactory());
            
            connector.getFilterChain().addLast("mdc", new MdcInjectionFilter());
            //connector.getFilterChain().addLast("codec", CODEC_FILTER);
            connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
            connector.getFilterChain().addLast("logger", LOGGING_FILTER);
            

            if (useSsl) {
                SSLContext sslContext = BogusSslContextFactory
                        .getInstance(false);
                SslFilter sslFilter = new SslFilter(sslContext);
                sslFilter.setUseClientMode(true);
                connector.getFilterChain().addFirst("sslFilter", sslFilter);
            }

            connector.setHandler(handler);
            ConnectFuture future1 = connector.connect(address);
            future1.awaitUninterruptibly();
            if (!future1.isConnected()) {
                return false;
            }
            session = future1.getSession();
            login();

            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public void login() {
        RequestMessage m = new RequestMessage();
        m.setSequence(0);
        m.setCommand("LOGIN");
        m.setUserId(id);
        m.setpassword(password);
        String IP = session.getLocalAddress().toString();
        System.out.println("This is the Local IP: " + IP);
        m.setIPAddress(IP);
        session.write(m);
        //session.write("LOGIN " + name);
    }

	public boolean register(NioSocketConnector connector, SocketAddress address,
			String name, String id, String password, String question,
			String passwordReminder, boolean useSsl) {
		System.out.println("This is address: " + address);
        if (session != null && session.isConnected()) {
            throw new IllegalStateException(
                    "Already connected. Disconnect first.");
          
        }

        try {
            IoFilter LOGGING_FILTER = new LoggingFilter();

            IoFilter CODEC_FILTER = new ProtocolCodecFilter(
                    new TextLineCodecFactory());
            
            connector.getFilterChain().addLast("mdc", new MdcInjectionFilter());
            //connector.getFilterChain().addLast("codec", CODEC_FILTER);

            connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
            connector.getFilterChain().addLast("logger", LOGGING_FILTER);

            if (useSsl) {
                SSLContext sslContext = BogusSslContextFactory
                        .getInstance(false);
                SslFilter sslFilter = new SslFilter(sslContext);
                sslFilter.setUseClientMode(true);
                connector.getFilterChain().addFirst("sslFilter", sslFilter);
            }

            connector.setHandler(handler);
            ConnectFuture future1 = connector.connect(address);
            future1.awaitUninterruptibly();
			if (!future1.isConnected()) {
				return false;
			}
			session = future1.getSession();
			register m = new register();
	        m.setSequence(0);
	        m.setCommand("REGISTER");
	        m.setUserId(id);
	        m.setpassword(password);
	        m.setQuestion(question);
	        m.setpasswordReminder(passwordReminder);
	        session.write(m);
			/*session.write("REGISTER " + id + CommonConfig.delimeter + name
					+ CommonConfig.delimeter + password
					+ CommonConfig.delimeter + question
					+ CommonConfig.delimeter + passwordReminder);*/
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

//	public void sendfile(File myFileName) throws CharacterCodingException {
	public void sendfile() {
		log.debug("in sendfile()");

            
 		
//		File myFileName;
//		myFileName = new File("E:\\hwlee\\testfile.txt");
//		log.debug("myFile.getPath() = " + myFileName.getPath());
/*
		try {
			bytebuf.putString("FILETRANSFER ", CommonConfig.encoder);
		} catch (CharacterCodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			log.debug("FILETRANSFER equals " + bytebuf.getString(CommonConfig.decoder));
		} catch (CharacterCodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		session.write(bytebuf);
		// session.write("FILETRANSFER " + myFileName + " ");

		try {

			FileInputStream fis = new FileInputStream(myFileName);
			FileChannel fc = fis.getChannel();
			// byte[] mybytearray = new byte[(int) myFile.length()];
			java.nio.ByteBuffer buf = java.nio.ByteBuffer.allocateDirect(1024);
			// buf.setAutoExpand(true);
			while (true) {
				int ret = fc.read(buf);
				if (ret != -1) {
					buf.flip();
					ByteBuffer bf = ByteBuffer.wrap(buf);
					session.write(bf);
				} else
					break;
			}

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		*/
	}

    public void broadcast(String message) {
    	broadcast m = new broadcast();
        m.setSequence(0);
        m.setcommand("BROADCAST");
        m.setUserId(name);
        m.setmessage(message);
        session.write(m);
    }

    public void quit() {
        if (session != null) {
            if (session.isConnected()) {
            	quit mess = new quit();
            	mess.setSequence(0);
            	mess.setCommand("QUIT");
                session.write(mess);
                // Wait until the chat ends.
                session.getCloseFuture().awaitUninterruptibly();
            }
            session.close(true);
        }
    }

}
