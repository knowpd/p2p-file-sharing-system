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

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.enterprisedt.net.ftp.FTPException;

import net.peerless.common.CommonConfig;
import net.peerless.dist.PeerIpAddress;
import net.peerless.dist.SuperPeerManager;
import net.peerless.filetransfer.PeerlessFtpClientSupport;
import net.peerless.filetransfer.PeerlessSendUdpMessageSupport;
import net.peerless.hashtable.FileAbstract;
import net.peerless.hashtable.FileSignature;
import net.peerless.hashtable.SigManager;
import net.peerless.message.FileInfoMessage;

public class PeerlessClientCommSupport {

	private static final String String = null;
	private IoSession session;
	SuperPeerManager superPeerManager;

	public PeerlessClientCommSupport(IoSession session,
			SuperPeerManager superPeerManager) {
		this.session = session;
		this.superPeerManager = superPeerManager;
	}

	public PeerlessClientCommSupport(IoSession session) {
		this.session = session;
	}

	public PeerlessClientCommSupport() {
	}

	void publishFileAll() {

		File[] listOfFiles = DirectoryReader.findFiles();
		FileSignature d1;
		try {

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					FileSignature fSig = new FileSignature(new File(
							listOfFiles[i].getAbsolutePath()));
					FileAbstract fa = new FileAbstract(new File(listOfFiles[i]
							.getAbsolutePath()));
					publishFile(fSig, fa);

					System.out.println("Published File: "
							+ listOfFiles[i].getAbsolutePath());
					SigManager.printByteArray(fSig);
				} else if (listOfFiles[i].isDirectory()) {
					// System.out.println("Directory " +
					// listOfFiles[i].getName());
				}
			}
		} catch (Exception e) {
			System.out.println("File Publish Exception");
			e.printStackTrace();
		}
	}

	void publishFile(FileSignature fileSignature, FileAbstract fileAbstract) {
		// ret: Success/Fail
		// The server stores the relationship between IP Address and Signature
		// in FILELOCATION table of Server DB
		// The server stores Signature and Abstract in HashTable.
		// Use TCP.

		FileInfoMessage msg = new FileInfoMessage();
		msg.setSequence(0);
		msg.setCommand("PUBLISH");
		msg.setFileSignature(fileSignature);
		msg.setFileAbstract(fileAbstract);
		String File = fileAbstract.getfileName();
		msg.setFilename(File);
		String PvtIPAdd = session.getLocalAddress().toString();
		System.out
				.println("The Private IP Address of the File being Published: "
						+ PvtIPAdd);
		msg.setPvtIPAdd(PvtIPAdd);
		FileSignature test = msg.getFileSignature();
		String testcmd = msg.getCommand();
		System.out.println(testcmd);
		System.out.println("In publish files");
		// System.out.println(test);
		session.write(msg);

	}

	public void addFileInformation(String clientip, FileSignature signature,
			FileAbstract fileabstract, String PvtClientIP, String Filename)
			throws SQLException {

		byte[] bsig = signature.getFileSignature();
		char[] babs = fileabstract.getFileAbstract();
		String sig = new String(bsig);
		String abs = new String(babs);
		System.out.println("Add File Information");
		System.out.println(sig);
		System.out.println(abs);
		PeerlessClientMain.clientDbSupport.insertFiles(clientip, sig, abs,
				PvtClientIP, Filename);
	}

	ArrayList requestSuperPeerIp() { // ip list, for Distributed case
		// add implementaion.
		// Use TCP.

		ArrayList ipAddrList = new ArrayList();
		return ipAddrList;
	}

	void searchFile(String searchString, SearchDialog search) throws Exception {
		FileSignature fSig = new FileSignature(searchString);

		PeerIpAddress[] targetIpAddress = superPeerManager
				.findHashSpaceAddress(fSig);
		if (CommonConfig.systemVersion == CommonConfig.centralizedVersion) {
			searchFile(searchString, search,
					CommonConfig.udpPeerlessServerAddress,
					CommonConfig.NumOfDownloadFiles);
		} else if (CommonConfig.systemVersion == CommonConfig.distributedVersion) {

			double resultMatches = Math.ceil(CommonConfig.NumOfDownloadFiles
					/ targetIpAddress.length);
			int resultMatchesInt = (int) (resultMatches);
			int lastMatch = CommonConfig.NumOfDownloadFiles
					- (targetIpAddress.length - 1) * resultMatchesInt;
			
			System.out.println("targetIpAddress.length = "
					+ targetIpAddress.length);
			/*for (int i = 0; i < targetIpAddress.length; i++) {
				System.out.print("PUBLIC IP ADDRESS = "
						+ targetIpAddress[i].getPublicIpAddress());
				System.out.println(", PRIVATE IP ADDRESS = "
						+ targetIpAddress[i].getPrivateIpAddress());
			}*/

			for (int i = targetIpAddress.length-1; i >=0; i--) {
				if (targetIpAddress[i] != null) 
				{
					if (i == targetIpAddress.length - 1) {
						String ipAddr = targetIpAddress[i].getPublicIpAddress();
						String temp1 = ipAddr.substring(1);
						String temp2[] = temp1.split(":");

						//searchFile(searchString, search, temp2[0], lastMatch);

						ipAddr = targetIpAddress[i].getPrivateIpAddress();
						temp1 = ipAddr.substring(1);
						String PriTemp2[] = temp1.split(":");
						System.out
								.println("[PeerlessClientCommSupport] super peer ip"
										+ PriTemp2[0]);

						searchFile(searchString, search, PriTemp2[0], lastMatch);

						}

					else {

						String ipAddr = targetIpAddress[i].getPublicIpAddress();
						String temp1 = ipAddr.substring(1);
						String temp2[] = temp1.split(":");

						//searchFile(searchString, search, temp2[0],resultMatchesInt);

						ipAddr = targetIpAddress[i].getPrivateIpAddress();
						temp1 = ipAddr.substring(1);
						String PriTemp2[] = temp1.split(":");
						System.out
								.println("[PeerlessClientCommSupport] super peer ip"
										+ PriTemp2[0]);

						searchFile(searchString, search, PriTemp2[0],
								resultMatchesInt);
						}
				}
			}

		}
	}

	void searchFile(String searchString, SearchDialog Search,
			String targetIpAddress, int resultMatchesInt) { // ret: IP Lists and
															// Signatures
		int remotePort = CommonConfig.udpPort;
		// String querySent = "efg";
		// String password = "peerless";
		System.out.println(targetIpAddress + remotePort + searchString);

		PeerlessSendUdpMessageSupport udpClientSupport = new PeerlessSendUdpMessageSupport(
				targetIpAddress, remotePort, searchString, Search,
				resultMatchesInt);

		udpClientSupport.connectAndSend();
	}

	boolean requestFile(String pvtIpAddress, String ipAddress, String fileName) {

		requestFile(pvtIpAddress, fileName);
		/*if (ipAddress.equalsIgnoreCase(pvtIpAddress))
			requestFile(ipAddress, fileName);
		else {
			//requestFile(ipAddress, fileName);
			requestFile(pvtIpAddress, fileName);
		}*/
		return false;
	}

	boolean requestFile(String ipAddress, String fileName) { // download a file

		// String remoteHost = "localhost";
		int remotePort = 2221;
		String username = "peerless";
		String password = "peerless";

		// String localFilePath = "./clientftp";
		String localFilePath = ClientConfig.downloadedFolder;
		String remoteFileName = fileName;

		String temp1 = ipAddress.substring(1);
		String temp2[] = temp1.split(":");
		System.out.println("spli pvt ip address: " + temp2[0]);
		

		PeerlessFtpClientSupport ftpClientSupport = new PeerlessFtpClientSupport(
				temp2[0], remotePort, username, password);

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
			System.out.println(remoteFileName);
			ftpClientSupport.downloadFile(localFilePath, remoteFileName);
			// System.out.println(ftpClientSupport.uploadFile(localFilePath,
			// remoteFileName));
			// System.out.println("File sent by hwlee.");
			ftpClientSupport.getFtp().disconnect();
		} catch (FTPException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return false;
	}

	/*
	 * boolean requestFile(String signature){ // add implementation. // server
	 * should be implemented at the same time. // use requestFile(String
	 * ipAddress, Sgring signature). return false; }
	 */

}
