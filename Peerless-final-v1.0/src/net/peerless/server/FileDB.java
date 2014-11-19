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

package net.peerless.server;

public class FileDB {

	String fileName;
	String fileAbstract;
	String fileSize;
	String fileIpaddress;
	String fileDigest;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileAbstract() {
		return fileAbstract;
	}

	public void setFileAbstract(String fileAbstract) {
		this.fileAbstract = fileAbstract;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileIpaddress() {
		return fileIpaddress;
	}

	public void setFileIpaddress(String fileIpaddress) {
		this.fileIpaddress = fileIpaddress;
	}

	public String getFileDigest() {
		return fileDigest;
	}

	public void setFileDigest(String fileDigest) {
		this.fileDigest = fileDigest;
	}

}
