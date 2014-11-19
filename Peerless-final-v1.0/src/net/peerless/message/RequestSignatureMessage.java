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

package net.peerless.message;

import net.peerless.hashtable.FileSignature;

public class RequestSignatureMessage extends AbstractMessage {
	
    private static final long serialVersionUID = 7371211248110239946L;
	FileSignature fileSignature;
	int matches;

	public FileSignature getFileSignature() {
		return fileSignature;
	}

	public void setFileSignature(FileSignature fileSignature) {
		this.fileSignature = fileSignature;
	}
	
	public int getMatches() {
		return matches;
	}

	public void setMatches(int matches) {
		this.matches = matches;
	}
	
	
	public RequestSignatureMessage(){
		
	}
	
	@Override
    public String toString() {
        // it is a good practice to create toString() method on message classes.
		return "0";
    }	
	
}
