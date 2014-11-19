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

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.peerless.hashtable.FileSignature;
import net.peerless.message.RequestSignatureMessage;
import net.peerless.message.ResultSignatureMessage;

public class PeerlessDBHandler {

	
	
	public static void main(String[] args) {
		
		String schemaFile = "conf\\peerlessdbschema.sql";
		
		//System.out.println("Checkpoint0");
		//String schemaFile = "conf\\peerlessDbSchema.sql";
		String dbName = "peerlessdb";
		PeerlessDbSupport peerlessdb = new PeerlessDbSupport(dbName);
		try {
			peerlessdb.connect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			peerlessdb.setUpSchema(schemaFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		//System.out.println("Checkpoint1");
		
		/*
		try {
			peerlessdb.dropTable("USERS");
			peerlessdb.dropTable("ACTIVEPEERS");
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}		*/
	
	//public /*Map<FileSignature, ArrayList>*/void searchFile(FileSignature signature){	//ret: IP Lists and Signatures
		
		RequestSignatureMessage signatureMeassage = new RequestSignatureMessage();
		FileSignature fileSignature= new FileSignature();
		
		fileSignature.setFileSignature("efg");
		signatureMeassage.setFileSignature(fileSignature);
		System.out.println("signature sent " + fileSignature);
		
		try {		
		List<ResultSignatureMessage> searchResult = peerlessdb.readData(fileSignature);
		 // System.out.println(searchResult);
		for(ResultSignatureMessage s : searchResult)
			  System.out.println("ipAddress and abstract "    + s.getIpAddress() +" and " + s.getFileAbstract());
			//System.out.println(s);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//PeerlessHashTable hT= new PeerlessHashTable();
		
		//String abstractValue= hT.accessHashMap(fileSignature);
		//System.out.println("abstract   "+abstractValue);
				
		//System.out.println("Checkpoint1");
		
		// add implementation.
		// server should be implemented at the same time.
		// Use UDP.
		 
		//Map<FileSignature, ArrayList> map= Collections.synchronizedMap(new HashMap<FileSignature, ArrayList>());
		
		//return map;
	//}
	
	}
}
