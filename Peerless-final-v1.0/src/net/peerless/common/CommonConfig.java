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

package net.peerless.common;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class CommonConfig {

	public static final int systemVersion = CommonConfig.centralizedVersion;
	//public static final int systemVersion = CommonConfig.distributedVersion;
	
	public static String delimeter = ":";
	
    public static String question1String = "Birth Place";
    public static String question2String = "Best Friend's Name";
    public static String question3String = "Mother's Maiden Name";
    
    public static final int fileSignatureSize = 1024;
    public static final int fileAbstractSize = 128;
    public static String tripletLibrary = "conf//triplet_representation";
    
    public static final int udpPort = 18567;
    //public static final String udpPeerlessServerAddress = "localhost";
    public static final String udpPeerlessServerAddress = "152.14.240.193";
    
    public static final int centralizedVersion = 0;
    public static final int distributedVersion = 1;

	private static Charset charset = Charset.forName("US-ASCII");
	public static CharsetEncoder encoder = charset.newEncoder();
    public static CharsetDecoder decoder = charset.newDecoder();
    
    public static final int NumOfDownloadFiles = 20;
    
    public static final int NumOfHashSpaces = 4;

}
