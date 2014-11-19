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

public class ClientConfig {

	public static String peerlessServerAddress = "152.14.240.193:59999";
	//public static String peerlessServerAddress = "localhost:1234";
	public static String sharedFolder = "ftphome";
	public static String downloadedFolder = "clientftp";
	public static final int ftpPort = 2221;
	public static String ClientDb = "clientdb";
    public static final int udpPort = 18567;
    //public static final String udpPeerlessServerAddress = "152.14.240.193";
	public static int count;
}
