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
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import net.peerless.common.CommonConfig;
import net.peerless.message.AbstractMessage;

public class FileAbstract extends AbstractMessage{
	private String fileName;
	private char[] fileAbstract;

	public FileAbstract(File fileIn) throws IOException{
		fileAbstract = new char[CommonConfig.fileAbstractSize];
		fileName = fileIn.getName();
		BufferedReader bufReader = new BufferedReader(new FileReader(fileIn));
		bufReader.read(fileAbstract, 0, CommonConfig.fileAbstractSize);
	}
	
	public char[] getFileAbstract() {
		return fileAbstract;
	}
	
	public String getfileName() {
		return fileName;
	}
	
	public static void main(String[] args) throws IOException{
		FileAbstract fa = new FileAbstract(new File("ftphome//test.txt"));
		printFileAbstract(fa.fileAbstract);
	}
	
	public static void printFileAbstract(char[] charArray) throws IOException{
		int c;
		CharArrayReader charArrayReader = new CharArrayReader(charArray);
		BufferedReader bufReader = new BufferedReader(charArrayReader);
		while( (c = bufReader.read()) != -1){
			System.out.print((char)c);
		}
	}
	
	
}
