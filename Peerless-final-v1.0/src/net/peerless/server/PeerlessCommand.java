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

public class PeerlessCommand {
    public static final int LOGIN = 0;

    public static final int REGISTER = 1;
    
    public static final int QUIT = 2;

    public static final int BROADCAST = 3;
    
    public static final int FILETRANSFER = 4;
    
    public static final int ACCEPT = 5;
    
	public static final int PUBLISH = 6;
	
	public static final int PUBLISHSP = 7;
    

    private final int num;

    private PeerlessCommand(int num) {
        this.num = num;
    }

    public int toInt() {
        return num;
    }

    public synchronized static PeerlessCommand valueOf(String s) {
    	System.out.println("This is Command DEBUG3"+ s);
        s = s.toUpperCase();
        System.out.println("This is Command DEBUG4"+ s);
        if ("LOGIN".equals(s)) {
            return new PeerlessCommand(LOGIN);
        }
        if ("REGISTER".equals(s)) {
            return new PeerlessCommand(REGISTER);
        }
        if ("QUIT".equals(s)) {
            return new PeerlessCommand(QUIT);
        }
        if ("BROADCAST".equals(s)) {
            return new PeerlessCommand(BROADCAST);
        }
        if ("FILETRANSFER".equals(s)) {
            return new PeerlessCommand(FILETRANSFER);
        }
        
        if ("ACCEPT".equals(s)) {
        	return new PeerlessCommand(ACCEPT);
        }
        
        if ("PUBLISH".equals(s)) {
            return new PeerlessCommand(PUBLISH);
        }
        
        if("PUBLISHSP".equals(s)) {
            return new PeerlessCommand(PUBLISHSP);
        }

        
        throw new IllegalArgumentException("Unrecognized command: " + s);
    }
}
