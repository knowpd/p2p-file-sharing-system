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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.peerless.common.CommonConfig;

public class SigHashTable {

	private Hashtable<FileSignature, int[]> sigHashTable;

	public SigHashTable() {
		sigHashTable = new Hashtable<FileSignature, int[]>();
	}

	public Object addSignature(FileSignature fSig) {
		
		int[] coordi = new int[2];
		coordi = SigManager.findCoordinate(fSig);
		return sigHashTable.put(fSig, coordi);
	}
	
	
	
	public List<FileSignatureMatch> searchSignature(FileSignature inputSig,
			int maxNumOfSearch, int minNumOfMatch) {
		FileSignature currentSig = new FileSignature();
		int currentNumOfOnes;
		int lastIndexOfRetSigArray;

		List<FileSignatureMatch> retSigArray = new ArrayList<FileSignatureMatch>();

		for (Enumeration<FileSignature> hashSig = sigHashTable.keys(); hashSig
				.hasMoreElements();) {
			currentSig = hashSig.nextElement();
			currentNumOfOnes = SigManager.andByteArrays(inputSig, currentSig);
			
			FileSignatureMatch fSigMatch = new FileSignatureMatch(currentSig,
					currentNumOfOnes);
			
			fSigMatch.setFSig(currentSig);
			fSigMatch.setNumOfMatch(currentNumOfOnes);

			if (currentNumOfOnes > 0 && currentNumOfOnes >= minNumOfMatch) {

				
				if (retSigArray.size() < maxNumOfSearch) {

					retSigArray.add(fSigMatch);
					//for(int i = 0; i < retSigArray.size(); i++){
					//	SigManager.printByteArray(retSigArray.get(i).getFSig());
					//}
					//Collections.sort(retSigArray, new SigComparator());
					Collections.sort(retSigArray);
				} else {
					lastIndexOfRetSigArray = retSigArray.size()-1;
					if( fSigMatch.getNumOfMatch() > retSigArray.get(lastIndexOfRetSigArray).getNumOfMatch() ){
						retSigArray.remove(lastIndexOfRetSigArray);
						retSigArray.add(fSigMatch);
						Collections.sort(retSigArray);
					}
				}
			}

		}
		
		

		return retSigArray;

	}
}
