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

public interface ServerInterface {

	Boolean login(String id, String password);

	Boolean longout(String id, String password);

	Boolean register(String id, String password, int questionNum, String answer);

	Boolean checkAlive(int ipaddress, int port);

	Boolean resetPassword(String id, int Question, String Answer);

	Boolean updatePassword(String id, String currPassword, String newPassword);

	FileIpaddressInfo searchFileDigest(String fileDigest); // return value: files & ip addresses
	
	String relayFileDigest(String fileDigest);
	
	Boolean digestAbstract(String filename);		// one person takes this part, paper 1
	
	String[] searchFileWithRanking(String filename); // retrun value: IP lists --> This is research part
	
}
