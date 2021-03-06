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

package net.peerless.hashtable;

public class FileSignatureMatch implements Comparable<FileSignatureMatch>{
	private FileSignature fSig;
	private int numOfMatch;
	
	public FileSignatureMatch(FileSignature sig, int numOfMatch) {
		super();
		fSig = sig;
		this.numOfMatch = numOfMatch;
	}

	public FileSignature getFSig() {
		return fSig;
	}

	public void setFSig(FileSignature sig) {
		fSig = sig;
	}

	public int getNumOfMatch() {
		return numOfMatch;
	}

	public void setNumOfMatch(int numOfMatch) {
		this.numOfMatch = numOfMatch;
	}

	@Override
	public int compareTo(FileSignatureMatch fSigMatch) {
        if(numOfMatch > fSigMatch.getNumOfMatch()){
        	return -1;
        } else if(numOfMatch < fSigMatch.getNumOfMatch()){
        	return 1;
        } else{
        	return 0;
        }
	}
	
	
}
