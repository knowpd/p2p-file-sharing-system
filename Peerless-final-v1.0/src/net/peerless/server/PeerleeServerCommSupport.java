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

import java.sql.PreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.mina.core.session.IoSession;

import net.peerless.db.*;
import net.peerless.hashtable.FileAbstract;
import net.peerless.hashtable.FileSignature;
import net.peerless.message.SendSuperIP;

public class PeerleeServerCommSupport {

	private Connection conn1;
	PeerlessDbSupport peerlessDbSupport;
	Dbruntest dbtest;
	PublishDB db;
	private Set<IoSession> sessions;

	public PeerleeServerCommSupport(PeerlessDbSupport peerlessDbSupport) {
		super();
		this.peerlessDbSupport = peerlessDbSupport;
		dbtest = new Dbruntest(peerlessDbSupport);
		db = new PublishDB(peerlessDbSupport);
	}
	
	public void publishToSuperPeers(){
		
		
		for(Iterator<IoSession> iter = sessions.iterator(); iter.hasNext(); iter.next()){
			iter.next().getRemoteAddress();
			
		}
	
	}

	public void addFileInformation(String clientip, FileSignature signature,
			FileAbstract fileabstract, String PvtClientIP, String Filename) {

		byte[] bsig = signature.getFileSignature();
		char[] babs = fileabstract.getFileAbstract();
		String sig = new String(bsig);
		String abs = new String(babs);
		System.out.println("Add File Information");
		System.out.println(sig);
		System.out.println(abs);
		db.Storefile(sig, abs, clientip, PvtClientIP, Filename);
	}

	public void DeleteFiles(String clientip) {
		db.Removefiles(clientip);

	}

	public void SendSuperpeerIP(String user, String IPAdd, IoSession session,
			String Type, String PvtIP) {

		// DbTestMain dbrun = new DbTestMain();
		// dbrun.main();

		dbtest.SetandStore(user, IPAdd, Type, PvtIP);
	}

	public void GetIPList(IoSession session, Set<IoSession> sessions) {

		try {

			List IP = dbtest.getSuper(session, sessions);
			/*
			 * String IPadd = IP.get(0).toString();
			 * System.out.println("This is Public IP of Superpeer from List:" +
			 * IPadd); SendSuperIP IPList = new SendSuperIP();
			 * IPList.setIPList(IP); session.write(IPList);
			 */
		}

		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void DeleteUser(String UserId) {
		String UserId1 = UserId;

		dbtest.RemoveUser(UserId1);

	}

	public void NewSuperUser(String UserID) {
		String UserId2 = UserID;

		dbtest.SuperPeer(UserId2);

	}

	public String getnormal() {
		String normal = "NULL";
		try {

			normal = dbtest.getnormal();
		}

		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			return normal;
		}

	}
}
