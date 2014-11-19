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

public class DbTestMain {
	
	public DbTestMain() {
    }

	public static void main() {
		System.out.println("Checkpoint0");
		String schemaFile = "conf\\peerlessdbschema.sql";
		String dbName = "peerlessdb";
		PeerlessDbSupport peerlessdb = new PeerlessDbSupport(dbName);
		try {
			peerlessdb.connect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		System.out.println("Checkpoint1");
		
		try {
			//peerlessdb.dropTable("USERS");
			peerlessdb.dropTable("ACTIVEPEERS");
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			
		}
		
		try {
			
			peerlessdb.setUpSchema(schemaFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}
		
		String userName = "hwlee";
		String userId = "hlee17@ncsu.edu";
		String passWord = "1234";
		int reminderQuestion = 3;
		String reminderAnswer = "Daejeon";
		
		
		/*try {
			int i = peerlessdb.insertUser(userName, userId, passWord, reminderQuestion, reminderAnswer);
			System.out.println(i + " line(s) inserted");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		

	}

}
