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

package net.peerless.db;

import java.sql.SQLException;

import net.peerless.db.PeerlessDbSupport;
import net.peerless.message.SendSuperIP;
import net.peerless.message.SendSuperPvtIP;
import net.peerless.message.Servermessage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import java.util.List;

import org.apache.mina.core.session.IoSession;

public class Dbruntest {
	
	private String UserID;
	private String IP;
	private String Type;
	private PeerlessDbSupport peerlessdb;
	private String PvtIP;
	
	public Dbruntest(PeerlessDbSupport peerlessdb) {
		super();
		this.peerlessdb = peerlessdb;}
	
	public void SetandStore(String UserID, String IP, String Type, String PvtIP) {
		this.UserID = UserID;
		this.IP = IP;
		this.Type = Type;
		try {
			
		
		int k = peerlessdb.insertActivepeers(UserID, IP, Type, PvtIP);
		System.out.println(k + " IP Address inserted");
		
		}
		
	catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}
	
	public void RemoveUser(String UserID) {
		
		String User = UserID;
		
		try {
			
			peerlessdb.deleteUsers(User);
			
		}
		
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		}
	
public void SuperPeer(String UserID) {
		
		String User = UserID;
		
		try {
			
			peerlessdb.UpdateSuper(User);
		}
		
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		}
		
		

	
	public List getSuper(IoSession session, Set<IoSession> sessions) throws SQLException{
		
	
		ResultSet k = peerlessdb.getSuperPeer();
		String ab = "NULL";
		String cd = "NULL";
		
		List ipAddrList = Collections.synchronizedList(new ArrayList());
		List PvtipAddrList = Collections.synchronizedList(new ArrayList());
		
		while (k.next()) {	
			ab = k.getString("PvtIP");
			System.out.println("Listing Super Peer Pvt IPs Here:" + ab);
			PvtipAddrList.add(ab);
			
			cd = k.getString("ipAddress");
			System.out.println("Listing Super Peers Public IP Address Here:" + ab);
			ipAddrList.add(cd);
			}
		String IPadd = PvtipAddrList.get(0).toString();
		System.out.println("This is Pvt IP of Superpeer from List:" + IPadd);
		
		String IPadd1 = ipAddrList.get(0).toString();
		System.out.println("This is Public IP of Superpeer from List:" + IPadd1);
		SendSuperIP IPList = new SendSuperIP();
		IPList.setSequence(0);
		IPList.setIPList(ipAddrList);
		IPList.setPvtIPList(PvtipAddrList);
		//session.write(IPList);
		
		synchronized (sessions) {
			for (IoSession session1 : sessions) {
				if (session1.isConnected()) {
					session1.write(IPList);
					//session.write("BROADCAST OK " + message);
				}
			}
		}
		
		
		
		return ipAddrList;
	}
	
	public String getnormal() throws SQLException{
		
		
		
		ResultSet j = peerlessdb.normaluser();
		String ab = "NULL";
	
		while (j.next()) {	
		ab = j.getString("userID");
		System.out.println("Listing Normal Peers Here:" + ab);
		}
		return ab;
		
		
	}
	
}
