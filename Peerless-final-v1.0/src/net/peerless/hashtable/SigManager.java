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

import net.peerless.common.CommonConfig;

public class SigManager {


	public static int findHashSpace(FileSignature fSig, int numOfHashSpaces) {
		int[] coordi;

		coordi = findCoordinate(fSig);
		
		int xLoc = coordi[0];
		int yLoc = coordi[1];
		
		int sum = numOfHashSpaces * (numOfHashSpaces + 1)/2;
		double startLoc;
		double endLoc;

		//System.out.printf("Input ( %d, %d )\n", xLoc, yLoc);
		/*
		for (double i = 0; i < numOfHashSpaces; i++) {
			startLoc = i*(i+1)/2/sum * CommonConfig.fileSignatureSize * 8 / 2;
			endLoc = (i + 1)*(i+2)/2/sum * CommonConfig.fileSignatureSize * 8 / 2;

			System.out.printf("( %d ~ %d )\n", (int)startLoc, (int)endLoc);
		}
		*/
		
		for (double i = 0; i < numOfHashSpaces; i++) {
			startLoc = i*(i+1)/2/sum * CommonConfig.fileSignatureSize * 8 / 2;
			endLoc = (i + 1)*(i+2)/2/sum * CommonConfig.fileSignatureSize * 8 / 2;
			//System.out.printf("( %d ~ %d )\n", (int)startLoc, (int)endLoc);

			if(xLoc >= startLoc && xLoc < endLoc){
				if(yLoc >= 0 && yLoc < endLoc){
					//System.out.printf("output -> ( %d ~ %d )\n", (int)startLoc, (int)endLoc);
					return (int)i;
				}
			}else if(xLoc >= 0 && xLoc < endLoc ){
				if(yLoc >= startLoc && yLoc < endLoc){
					//System.out.printf("output -> ( %d ~ %d )\n", (int)startLoc, (int)endLoc);
					return (int)i;
				}
			}
			
		}

		return 0;
	}
	
	

	public static int[] findCoordinate(FileSignature fSig) {
		return findCoordinate(fSig.getFileSignature());
	}

	public static int[] findCoordinate(byte[] x) {
		int count;
		int[] result = new int[2];
		for (int i = 0; i < 2; i++) {
			count = 0;
			for (int j = i * x.length / 2; j < (i + 1) * x.length / 2; j++) {
				count += countBitsOfByte(x[j]);
			}
			result[i] = count;
		}
		return result;
	}

	public static int countBitsOfByteArray(byte[] x) {
		int count = 0;
		for (int i = 0; i < x.length; i++) {
			count += countBitsOfByte(x[i]);
		}
		return count;
	}

	public static int countBitsOfByte(byte x) {
		int count = 0;
		while (x != 0) {
			// The result of this operation is to subtract off
			// the least significant non-zero bit. This can be seen
			// from noting that subtracting 1 from any number causes
			// all bits up to and including the least significant
			// non-zero bit to be complemented.
			//
			// For example:
			// 10101100 x
			// 10101011 x-1
			// 10101000 (x - 1) & x
			x &= x - 1;
			count++;
		}
		return count;
	}
	
	public static int andByteArrays(FileSignature inputSig, FileSignature hashSig){
		return andByteArrays(inputSig.getFileSignature(), hashSig.getFileSignature());
	}	
	
	public static int andByteArrays(byte[] firstByteArray, byte[] secondByteArray){
		int count = 0;
		byte[] resultArray = new byte[firstByteArray.length];
		
		for(int i=0;i<firstByteArray.length;i++){
			resultArray[i] = (byte) (firstByteArray[i] & secondByteArray[i]);
			count += SigManager.countBitsOfByte(resultArray[i]);
		}

		return count;
	}
	
	public static void printByteArray(FileSignature fSig) {
		printByteArray(fSig.getFileSignature());
	}
	
	public static void printByteArray(byte[] barray) {
		for (int i = 0; i < barray.length; i++) {
			int t= barray[i];
			String temp = "0000";
			temp=temp.concat(Integer.toHexString(t));
			temp=temp.toUpperCase();
			System.out.printf("%c%c",temp.charAt(temp.length()-2),temp.charAt(temp.length()-1));
		}
		System.out.println("");
	}
	
	
	public static void main(String[] args){
		byte[] br1 = new byte[]{ 3, 15 };
		byte[] br2 = new byte[]{ 3, 14 };
		
		System.out.println("addByteArrays = "+ andByteArrays(br1, br2));
		
	}


}
