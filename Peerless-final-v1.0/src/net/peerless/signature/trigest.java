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

package net.peerless.signature;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class trigest {
	byte[] signature=new byte[1024];

	public byte[] getSignature() {
		return signature;
	}

	public trigest(File FileIn) throws Exception {
		BufferedReader input =  new BufferedReader(new FileReader(FileIn));
		CreateSignature(input);
	}

	public trigest(String StringIn) throws Exception{
		BufferedReader input =  new BufferedReader(new StringReader(StringIn));
		CreateSignature(input);
	}

	private void CreateSignature(BufferedReader bReader) throws Exception {
		Map tripletHash = new LinkedHashMap();
		String line = null; 
		byte[] dArraytemp=new byte[1024]; 

		//Open the Input file and read all the bit positions and the triplets

		BufferedReader triRead =  new BufferedReader(new FileReader("triplet_frequency_2"));
		while (( line = triRead.readLine()) != null){
			//Construct the hash
			tripletHash.put(line.substring(0, 3) , new Integer( 0 ) );
		}
		//System.out.println("Finished Creating Hash");


		while (( line = bReader.readLine()) != null){
			for(int i=0; i<line.length()-2;i++){
				if (line.substring(i,i+3).indexOf(" ") == -1){
					if (tripletHash.containsKey(line.substring(i, i + 3)))
						tripletHash.put( line.substring(i, i + 3) , new Integer( 1 ) );
					else
						tripletHash.put( "..." , new Integer( 1 ) );
				}
			}
		}
		bReader.close();

		//System.out.println( "Now Hash entries:" );
		int shiftby=8;
		byte bytevalue=0;
		int dArrayIndex=0;
		for ( Iterator it = tripletHash.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry e = (Map.Entry) it.next();
			shiftby--;
			byte[] value=e.getValue().toString().getBytes();
			value[0]-=48;
			bytevalue^=(value[0]<<shiftby);
			if (shiftby==0){
				dArraytemp[dArrayIndex]=bytevalue;
				dArrayIndex++;
				shiftby=8;
				bytevalue=0;
			}
		}
		//printbary(dArraytemp);
		System.arraycopy( dArraytemp , 0, signature, 0, 1024 );
		//printbary(dArray);

	}

	public static void main(String[] args) throws Exception {
//		trigest d1=new trigest("hi search for me this sring...");
//		printbary(d1.getSignature());
//
//		trigest d2=new trigest(new File("1.txt"));
//		printbary(d2.getSignature());
//		

	}

//	private static final void printbary(byte[] bary) {
//		for (int i = 0; i < bary.length; i++) {
//			int t= bary[i];
//			String temp = "0000";
//			temp=temp.concat(Integer.toHexString(t));
//			temp=temp.toUpperCase();
//			System.out.printf("%c%c",temp.charAt(temp.length()-2),temp.charAt(temp.length()-1));
//		}
//		System.out.println("");
//	}

}
