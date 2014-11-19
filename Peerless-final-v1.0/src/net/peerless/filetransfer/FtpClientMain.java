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

import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FileTransferClient;

public class FtpClientMain {
	public static void main(String[] args) {
		
		
		String remoteHost = "localhost";
		int remotePort = 2221;
		String username = "peerless";
		String password = "peerless";
		
		String localFilePath="./clientftp";
		String remoteFileName="test.txt";
		
		PeerlessFtpClientSupport ftpClientSupport = new PeerlessFtpClientSupport(remoteHost, remotePort, username, password);

		try {
			ftpClientSupport.connect();
		} catch (FTPException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			System.out.println(ftpClientSupport.uploadFile(localFilePath, remoteFileName));
			System.out.println("File sent by hwlee.");
			ftpClientSupport.getFtp().disconnect();
		} catch (FTPException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		
		

		
/*		
		FileTransferClient ftp = new FileTransferClient();
		
		try {
			ftp.setRemoteHost(remoteHost);
			ftp.setRemotePort(remotePort);
			ftp.setUserName(username);
			ftp.setPassword(password);

			ftp.connect();
			
			ftp.uploadFile(localFilePath, remoteFileName);
			System.out.println("File sent by hwlee.");
			
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
		
	}
}
