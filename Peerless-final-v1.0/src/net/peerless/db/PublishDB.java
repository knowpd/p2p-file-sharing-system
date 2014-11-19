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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

public class PublishDB {
	private String fileSignature;
	private String fileAbstract;
	private String ipAddress;
	private String PvtClientIP;
	private PeerlessDbSupport peerlessdb;
	
	public PublishDB(PeerlessDbSupport peerlessdb) {
		super();
		this.peerlessdb = peerlessdb;
    }
	
	public void Storefile(String filesignature, String fileabstract, String clientip, String PvtClientIP, String Filename) {
		this.fileSignature = filesignature;
		this.fileAbstract = fileabstract;
		this.ipAddress = clientip;
		this.PvtClientIP = PvtClientIP;
		try {
			System.out.println("Store File: Insterting in to DB");
			System.out.println(filesignature);
			int k = peerlessdb.insertFiles(clientip, filesignature, fileabstract,PvtClientIP, Filename);
			System.out.println("FILES FOR IP Address" + clientip + "inserted");
		}catch (SQLException e) {
		System.out.println("Exception inserting into Data base");
		e.printStackTrace();
		}
	}
	
	public void Removefiles(String clientip){
		try {
			System.out.println("Remove File: Removing files from DB");
			System.out.println(clientip);
			peerlessdb.deleteFiles(clientip);
		}catch (SQLException e) {
		System.out.println("Exception deleting files from Data base");
		e.printStackTrace();
		}
		
	}
}

