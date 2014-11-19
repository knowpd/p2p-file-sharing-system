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

import java.util.Map;

import net.peerless.hashtable.FileAbstract;
import net.peerless.hashtable.FileSignature;

public class ResultSignatureMessage extends AbstractMessage{
	
    private static final long serialVersionUID = 7371211248110219946L;
    private String fileName;
	private FileSignature fileSignature;
	private String ipAddress;
	//private FileAbstract fileAbstract; //@deepak changed
	private String fileAbstract;
	int signatureNumber;
	private String pvtIpAddress;

	public void setSignatureNumber(int num){
		this.signatureNumber=num;
	}
	public int getSignatureNumber(){
		return signatureNumber;
	}	
	
	public void setIpAddress(String ipAddress){
		this.ipAddress=ipAddress;
	}
	
	public void setPvtIpAddress(String pvtIpAddress){
		this.pvtIpAddress=pvtIpAddress;
	}	
	public void setFileAbstract(String fileAbstract){
		this.fileAbstract=fileAbstract;
	}	

	public void setFileName(String fileName){
		this.fileName=fileName;
	}
	
	public void setFileSignature (FileSignature fileSignature){
		this.fileSignature= fileSignature;
	}
	
	public String getPvtIpAddress(){
		return pvtIpAddress;
	}
	
	public String getIpAddress(){
		return ipAddress;
	}
	
	public String getFileAbstract(){
		return fileAbstract;
	}
	
	public FileSignature getFileSignature(){
		return fileSignature;
	}
	
	public String getFileName(){
		return fileName;
	}	
	
	public ResultSignatureMessage(){
	
	}
	//Map<FileSignature, ArrayList> map;
	
	@Override
    public String toString() {
        // it is a good practice to create toString() method on message classes.
		return "0";
    }
	
}



/*
public class ResultSignatureMessage<FileSignature, ArrayList> {
	Map<FileSignature, ArrayList> map;

	public Map<FileSignature, ArrayList> getMap() {
		return map;
	}

	public void setMap(Map<FileSignature, ArrayList> map) {
		this.map = map;
	}
	
}
*/
