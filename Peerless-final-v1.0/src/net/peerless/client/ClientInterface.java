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

import net.peerless.server.FileIpaddressInfo;

public interface ClientInterface {

	Boolean login(String ID, String Password);

	Boolean longout();

	Boolean registration(String ID, String encPassword, int Question,
			String Answer);

	String encryptPassword(String rawPassword);

	Boolean resetPassword(int Question, String Answer);

	Boolean changePassword(String currPassword, String newPassword);
	
	Boolean advertiseFiles(String[] digestedfile);
	
	String digestFile(String filename);	// return value: digestedfile
	
	String[] searchFile(String ipaddress, String filename); // return value: List of ip addresses
	

}
