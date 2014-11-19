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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.SaltedPasswordEncryptor;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

public class PeerlessFtpServerSupport {

	private int port;
	private FtpServerFactory serverFactory;
	private ListenerFactory factory;
	private PropertiesUserManagerFactory userManagerFactory;
	private UserManager um;
	private BaseUser user;
	private FtpServer server;

	public PeerlessFtpServerSupport(int port) {
		super();
		this.port = port;
		user = new BaseUser();

		userManagerFactory = new PropertiesUserManagerFactory();
		userManagerFactory.setFile(new File("users.properties"));

		userManagerFactory.setPasswordEncryptor(new SaltedPasswordEncryptor());
		um = userManagerFactory.createUserManager();
	}

	private void setFtpEnv() {
		serverFactory = new FtpServerFactory();
		factory = new ListenerFactory();

		// set the port of the listener
		factory.setPort(port);

		// define SSL configuration
		// SslConfigurationFactory ssl = new SslConfigurationFactory();
		// ssl.setKeystoreFile(new File("ftpserver.jks"));
		// ssl.setKeystorePassword("password");

		// set the SSL configuration for the listener
		// factory.setSslConfiguration(ssl.createSslConfiguration());
		// factory.setImplicitSsl(true);

		// replace the default listener
		serverFactory.addListener("default", factory.createListener());

		List<Authority> auths = new ArrayList<Authority>();
		Authority auth = new WritePermission();
		auths.add(auth);

		try {
			appendUser("peerless", "peerless", "ftphome", true, auths);
			appendUser("hwlee", "hwlee", "ftphome", true, auths);
		} catch (FtpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * user.setName("hwlee"); user.setPassword("hwlee");
		 * user.setHomeDirectory("ftphome"); user.setEnabled(true);
		 * user.setAuthorities(auths);
		 */

		serverFactory.setUserManager(userManagerFactory.createUserManager());
		server = serverFactory.createServer();
	}

	private void appendUser(String name, String passwd, String homedir,
			Boolean enabled, List<Authority> auths) throws FtpException {
		user.setName(name);
		user.setPassword(passwd);
		user.setHomeDirectory(homedir);
		user.setEnabled(enabled);
		user.setAuthorities(auths);
		um.save(user);
	}

	public void startServer() throws FtpException {
		// start the server
		setFtpEnv();
		server.start();

	}
	   public static void main(String[] args) throws IOException {
		   PeerlessFtpServerSupport ser = new PeerlessFtpServerSupport(2221);
	        
			try {
				ser.startServer();
			} catch (FtpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        
	}

}
