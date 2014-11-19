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

package net.peerless.filetransfer;

import net.peerless.message.AbstractMessage;



public class UdpMessage  extends AbstractMessage{

	private static final long serialVersionUID = -940833727168119141L;
		
		
		private String fileSignature;
		
		public String getFileSignature() {
			return fileSignature;
		}

		public void setFileSignature(String fileSignature) {
			this.fileSignature = fileSignature;
		}
		
		public UdpMessage() {
	    }

		@Override
	    public String toString() {
	        // it is a good practice to create toString() method on message classes.
	        return fileSignature;
	}
	


}
