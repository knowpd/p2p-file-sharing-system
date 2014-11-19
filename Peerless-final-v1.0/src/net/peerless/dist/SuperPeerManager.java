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

package net.peerless.dist;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.peerless.common.CommonConfig;
import net.peerless.hashtable.FileSignature;
import net.peerless.hashtable.SigManager;


public class SuperPeerManager {

	private List<PeerIpAddress> ipAddrList;
	
	public SuperPeerManager() {
		super();
		this.ipAddrList = Collections.synchronizedList(new ArrayList());
	}
	
	public void EmptyIPList() {
		ipAddrList.clear();
	}
	
	public void AddIPtoList(PeerIpAddress PeerIP) {
		ipAddrList.add(PeerIP);
	
	}

	public PeerIpAddress[] findHashSpaceAddress(FileSignature fSig){
		int hashSpaceIndex;
		int numOfSuperPeers = ipAddrList.size();
		hashSpaceIndex = findHashSpace(fSig, numOfSuperPeers);
		
		PeerIpAddress[] retAddrArray = new PeerIpAddress[numOfSuperPeers];
		for(int i = hashSpaceIndex, j=0; i < numOfSuperPeers; i++, j++){
			retAddrArray[j] = ipAddrList.get(i);
			//System.out.printf("retAddrArray[i].getPrivateIpAddress() = "+retAddrArray[j].getPrivateIpAddress());
			//System.out.printf("retAddrArray[i].getPublicIpAddress() = "+retAddrArray[j].getPublicIpAddress());
		}
		
		return retAddrArray;
	}
	
	public int findHashSpace(FileSignature fSig, int numOfHashSpaces) {
		int[] coordi;

		coordi = SigManager.findCoordinate(fSig);
		
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


	
	
	public static void main(String[] args) throws Exception{
		
		String publicIpAddr1 = "211.168.0.0";
		String privateIpAddr1 = "192.168.0.0";
		PeerIpAddress ipAddress1 = new PeerIpAddress(publicIpAddr1, privateIpAddr1);
		
		String publicIpAddr2 = "133.168.0.1";
		String privateIpAddr2 = "10.168.0.1";
		PeerIpAddress ipAddress2 = new PeerIpAddress(publicIpAddr1, privateIpAddr1);
		
		String publicIpAddr3 = "133.168.0.2";
		String privateIpAddr3 = "10.168.0.2";
		PeerIpAddress ipAddress3 = new PeerIpAddress(publicIpAddr1, privateIpAddr1);
		
		String publicIpAddr4 = "133.168.0.3";
		String privateIpAddr4 = "10.168.0.3";
		PeerIpAddress ipAddress4 = new PeerIpAddress(publicIpAddr1, privateIpAddr1);
		

		SuperPeerManager spl = new SuperPeerManager();
		
		spl.ipAddrList.add(ipAddress1);
		spl.ipAddrList.add(ipAddress2);
		spl.ipAddrList.add(ipAddress3);
		spl.ipAddrList.add(ipAddress4);
		
		FileSignature d1=new FileSignature(new File("ftphome//test.txt"));
		SigManager.printByteArray(d1);
		
		PeerIpAddress[] pia = spl.findHashSpaceAddress(d1);
		
		System.out.println("pia.length = "+pia.length);
		for(int i=0; i < pia.length ; i++){
			System.out.println("i = "+i);
			System.out.println("publicIpAddress = "+pia[i].getPublicIpAddress());
			System.out.println("privateIpAddress = "+ pia[i].getPrivateIpAddress());
//			System.out.println("publicIpAddress = "+spl.findHashSpaceAddress(d1)[i].getPrivateIpAddress());
//			System.out.println("privateIpAddress = "+ spl.findHashSpaceAddress(d1)[i].getPublicIpAddress());

		}
	}
	
	
	
	
}
