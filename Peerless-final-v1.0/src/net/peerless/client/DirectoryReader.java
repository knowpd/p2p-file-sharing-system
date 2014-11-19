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

package net.peerless.client;

import java.io.File;

public class DirectoryReader {

	public static File[] findFiles(){
		
	    File folder = new File(ClientConfig.sharedFolder);
	    File[] listOfFiles = folder.listFiles();
	    
	    return folder.listFiles();
		
	}
	
	public static void printFilesOfSharedFolder(){
		File[] listOfFiles = DirectoryReader.findFiles();
		
	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        //System.out.println("File " + listOfFiles[i].getName());
	    	  System.out.println("File " + listOfFiles[i].getAbsolutePath());
	      } else if (listOfFiles[i].isDirectory()) {
	        //System.out.println("Directory " + listOfFiles[i].getName());
	      }
	    }
	}
	
	
	public static void main(String[] args) {

		File[] listOfFiles = DirectoryReader.findFiles();
		
	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        //System.out.println("File " + listOfFiles[i].getName());
	    	  System.out.println("File " + listOfFiles[i].getAbsolutePath());
	      } else if (listOfFiles[i].isDirectory()) {
	        System.out.println("Directory " + listOfFiles[i].getName());
	      }
	    }

	}
	
}
