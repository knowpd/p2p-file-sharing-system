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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.peerless.client.DirectoryReader;
import net.peerless.common.CommonConfig;
import net.peerless.message.AbstractMessage;

public class FileSignature extends AbstractMessage implements Comparable<FileSignature>{
	
	private static final long serialVersionUID = -940833727668119141L;
	private byte[] fileSignature = new byte[CommonConfig.fileSignatureSize];;

	public FileSignature(){
		super();
	}
	
	public FileSignature(byte[] fileSignature) {
		super();
		this.fileSignature = fileSignature;
	}

	public FileSignature(File FileIn) throws Exception {
		BufferedReader input =  new BufferedReader(new FileReader(FileIn));
		CreateSignature(input);
	}

	public FileSignature(String StringIn) throws Exception{
		BufferedReader input =  new BufferedReader(new StringReader(StringIn));
		CreateSignature(input);
	}
	
	public void setFileSignature(String fileName){//@deepak: remove public after testing
		//for(int i=0; i < signatureSize; i++ ){
			
			fileSignature = fileName.getBytes();
		//}
	}

	public byte[] getFileSignature() {
		return fileSignature;
	}

	private void CreateSignature(BufferedReader bReader) throws Exception {
		Map tripletHash = new LinkedHashMap();
		String line = null; 
		byte[] dArraytemp=new byte[1024]; 

		//Open the Input file and read all the bit positions and the triplets

		BufferedReader triRead =  new BufferedReader(new FileReader(CommonConfig.tripletLibrary));
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
		System.arraycopy( dArraytemp , 0, fileSignature, 0, 1024 );
		//printbary(dArray);

	}

	public static void main(String[] args) throws Exception {

		File[] listOfFiles = DirectoryReader.findFiles();

		
	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	    	  
	    	  System.out.print("Hash Space: "+SigManager.findHashSpace(new FileSignature(new File(listOfFiles[i].getAbsolutePath())),4));
	    	  int[] coordi;
	    	  coordi = SigManager.findCoordinate(new FileSignature(new File(listOfFiles[i].getAbsolutePath())));
	    	  System.out.print(", ("+coordi[0]+", "+coordi[1]+") :");
	    	  System.out.println(listOfFiles[i].getName());
	    	  
	    	  //System.out.println("File " + listOfFiles[i].getAbsolutePath());
	      } else if (listOfFiles[i].isDirectory()) {
	        //System.out.println("Directory " + listOfFiles[i].getName());
	      }
	    }
		
		
		/*
		SigHashTable sht = new SigHashTable();
		
		FileSignature d0=new FileSignature("sig");
		SigManager.printByteArray(d0);
		
		FileSignature d1=new FileSignature(new File("ftphome//test.txt"));
		SigManager.printByteArray(d1);
		
		FileSignature d10=new FileSignature(new File("ftphome//test.txt"));
		SigManager.printByteArray(d10);
		
		System.out.println("compare two FileSignature = "+ d1.compareTo(d0));
		System.out.println("equals Test for FileSignature = "+ d1.equals(d10));
		
		sht.addSignature(d1);
		sht.addSignature(d10);
		

		
		List<FileSignatureMatch> retList = sht.searchSignature(d0, 6, 0);
		System.out.println("***** Search Result *****");
		
		for(Iterator<FileSignatureMatch> iter = retList.iterator(); iter.hasNext();){
			FileSignatureMatch fsm = iter.next();
			SigManager.printByteArray(fsm.getFSig());
			System.out.println("fsm.getNumOfMatch() = "+fsm.getNumOfMatch());
		}
		*/
/*				
		FileSignature d2=new FileSignature(new File("ftphome//Hash_table.htm"));
		SigManager.printByteArray(d2);

		FileSignature d3=new FileSignature("aaa aab aac");
		SigManager.printByteArray(d3);
		
		FileSignature d4=new FileSignature("signature");
		SigManager.printByteArray(d4);
		
		FileSignature d5=new FileSignature("I am a boy.");
		SigManager.printByteArray(d5);
		
		FileSignature d6=new FileSignature("aaa aab disk aaf mbc");
		SigManager.printByteArray(d6);
		
		FileSignature d7=new FileSignature("aaa aab aaf disboy");
		SigManager.printByteArray(d7);
		
		FileSignature d8=new FileSignature("aaa aae aab");
		SigManager.printByteArray(d8);
		
		FileSignature d9=new FileSignature("Design and Implementation of Virtualized Network Simulator");
		SigManager.printByteArray(d9);
		


		sht.addSignature(d1);
		sht.addSignature(d2);
		sht.addSignature(d3);
		sht.addSignature(d4);
		sht.addSignature(d5);
		sht.addSignature(d6);
		sht.addSignature(d7);
		sht.addSignature(d8);
		sht.addSignature(d9);

	
/*		
		List<FileSignatureMatch> retList = sht.searchSignature(d0, 6, 2);
		System.out.println("***** Search Result *****");
		
		for(Iterator<FileSignatureMatch> iter = retList.iterator(); iter.hasNext();){
			FileSignatureMatch fsm = iter.next();
			SigManager.printByteArray(fsm.getFSig());
			System.out.println("fsm.getNumOfMatch() = "+fsm.getNumOfMatch());
		}
/*		
		
		/*
		
		int[] rr1 = new int[2];
		int[] rr2 = new int[2];
		rr1 = SigManager.findCoordinate(d1.getFileSignature());
		System.out.printf("num of bit count of byte array = (%d, %d)\n",
				rr1[0], rr1[1]);

		rr2 = SigManager.findCoordinate(d2.getFileSignature());
		System.out.printf("num of bit count of byte array = (%d, %d)\n",
				rr2[0], rr2[1]);
		
		System.out.println("SuperPeer Num = "+SigManager.findSuperPeer(d2, 8));
		
		System.out.println("find number of ones = "+SigManager.andByteArrays(d1, d2));
		*/
		
		/*
		System.out.println("-> HashSpace Num = "+SigManager.findHashSpace(d1, 4));
		
		System.out.println("-> HashSpace Num = "+SigManager.findHashSpace(d2, 4));
		
		System.out.println("-> HashSpace Num = "+SigManager.findHashSpace(d3, 4));
		System.out.println("-> HashSpace Num = "+SigManager.findHashSpace(d4, 4));
		*/
		
		
		
	}

	@Override
    public String toString() {
        // it is a good practice to create toString() method on message classes.
        return "0";
    }

	@Override
	public int compareTo(FileSignature fSig) {
		// TODO Auto-generated method stub
		
		byte[] current = this.getFileSignature();
		byte[] input = fSig.getFileSignature();
		
		int CompareResult = 0;
		
		for(int i=0; i<current.length; i++){
			if(current[i] != input[i])
				CompareResult++;
		}
		
		if (CompareResult == 0){
			return 1;
		}else{
			return -1;
		}
	}
	
	@Override
	public boolean equals(Object obj) {

		FileSignature fSig = (FileSignature)obj;
		
		byte[] current = this.getFileSignature();
		byte[] input = fSig.getFileSignature();
		
		int CompareResult = 0;
		
		for(int i=0; i<current.length; i++){
			if(current[i] != input[i])
				CompareResult++;
		}
		
		if (CompareResult == 0){
			return true;
		}else{
			return false;
		}
	}
	
	
	public int hashCode() {
		String fileSignatureString = new String(fileSignature);  
		return fileSignatureString.hashCode();
	}
	
	
}

