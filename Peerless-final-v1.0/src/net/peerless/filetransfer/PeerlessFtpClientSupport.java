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

import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FileTransferClient;

public class PeerlessFtpClientSupport {

	private String remoteHost;
	private int remotePort;
	private String username;
	private String password;
	
	private FileTransferClient ftp;
	
	public PeerlessFtpClientSupport(String remoteHost, int remotePort,
			String username, String password) {
		super();
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
		this.username = username;
		this.password = password;
	}
	
	public void connect() throws FTPException, IOException{
		ftp = new FileTransferClient();
		ftp.setRemoteHost(remoteHost);
		ftp.setRemotePort(remotePort);
		ftp.setUserName(username);
		ftp.setPassword(password);
		ftp.getAdvancedFTPSettings().setConnectMode(FTPConnectMode.PASV);

		ftp.connect();
	}
	
	public String uploadFile(String localFileName, String remoteFileName) throws FTPException, IOException{
		return ftp.uploadFile(localFileName, remoteFileName);
	}
	
	public void downloadFile(String localFileName, String remoteFileName) throws FTPException, IOException{
		ftp.downloadFile(localFileName, remoteFileName);
	}
	
	public FileTransferClient getFtp() {
		return ftp;
	}
	
	
	
	
}
