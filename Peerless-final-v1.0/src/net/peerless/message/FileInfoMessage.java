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

import net.peerless.hashtable.FileAbstract;
import net.peerless.hashtable.FileSignature;

public class FileInfoMessage extends AbstractMessage{

	private static final long serialVersionUID = -940833727168119141L;
	private String command;
	private FileSignature fileSignature;
	private FileAbstract fileAbstract;
	private String PvtIPAdd;
	private String Filename;
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	
	public String getFilename() {
		return Filename;
	}

	public void setFilename(String Filename) {
		this.Filename = Filename;
	}
	
	public String getPvtIPAdd() {
		return PvtIPAdd;
	}

	public void setPvtIPAdd(String PvtIPAdd) {
		this.PvtIPAdd = PvtIPAdd;
	}
	
	public FileSignature getFileSignature() {
		return fileSignature;
	}
	public void setFileSignature(FileSignature fileSignature) {
		this.fileSignature = fileSignature;
	}
	public FileAbstract getFileAbstract() {
		return fileAbstract;
	}
	public void setFileAbstract(FileAbstract fileAbstract) {
		this.fileAbstract = fileAbstract;
	}
	
	@Override
    public String toString() {
        // it is a good practice to create toString() method on message classes.
        return command + fileSignature + fileAbstract;
    }
}