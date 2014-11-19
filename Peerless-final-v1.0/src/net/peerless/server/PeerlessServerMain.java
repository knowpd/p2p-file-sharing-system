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

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import net.peerless.db.PeerlessDbSupport;
import net.peerless.dist.SuperPeerManager;
import net.peerless.filetransfer.PeerlessUdpServer;
import net.peerless.hashtable.FileSignature;
import net.peerless.hashtable.SigHashTable;
import net.peerless.hashtable.SigManager;
import net.peerless.ssl.BogusSslContextFactory;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * (<b>Entry point</b>) Chat server
 * 
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version $Rev$, $Date$
 */
public class PeerlessServerMain {

	/** Set this to true if you want to make the server SSL */
	private static final boolean USE_SSL = false;

	public static void main(String[] args) throws Exception {
		NioSocketAcceptor acceptor = new NioSocketAcceptor();
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();

		MdcInjectionFilter mdcInjectionFilter = new MdcInjectionFilter();
		chain.addLast("mdc", mdcInjectionFilter);

		SigHashTable sht = new SigHashTable();

		/*
		 * FileSignature d0=new FileSignature("test data");
		 * SigManager.printByteArray(d0);
		 * 
		 * FileSignature d1=new FileSignature(new File("ftphome//test.txt"));
		 * SigManager.printByteArray(d1);
		 * 
		 * FileSignature d2=newFileSignature(
		 * "Design and Implementation of Virtualized Network Simulator");
		 * SigManager.printByteArray(d2);
		 * 
		 * sht.addSignature(d0); sht.addSignature(d1); sht.addSignature(d2);
		 */

		PeerlessDbSupport peerlessDbSupport = new PeerlessDbSupport(ServerConfig.ServerDb);
		peerlessDbSupport.connect();
		peerlessDbSupport.createSchema();

		SuperPeerManager superPeerManager = new SuperPeerManager();
		
		// Add SSL filter if SSL is enabled.
		if (USE_SSL) {
			addSSLSupport(chain);
		}

		// chain.addLast("codec", new ProtocolCodecFilter(new
		// TextLineCodecFactory()));
		chain.addLast("codec", new ProtocolCodecFilter(
				new ObjectSerializationCodecFactory()));

		addLogger(chain);

		// Bind
		acceptor.setHandler(new PeerlessProtocolHandler(sht, peerlessDbSupport, superPeerManager));
		acceptor.bind(new InetSocketAddress(ServerConfig.PORT));

		System.out.println("Listening on port " + ServerConfig.PORT);
		
		try {
			new PeerlessUdpServer(peerlessDbSupport, sht);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private static void addSSLSupport(DefaultIoFilterChainBuilder chain)
			throws Exception {
		SslFilter sslFilter = new SslFilter(BogusSslContextFactory
				.getInstance(true));
		chain.addLast("sslFilter", sslFilter);
		System.out.println("SSL ON");
	}

	private static void addLogger(DefaultIoFilterChainBuilder chain)
			throws Exception {
		chain.addLast("logger", new LoggingFilter());
		System.out.println("Logging ON");
	}
}
