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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;

import net.peerless.message.ResultSignatureMessage;

class MyDwnTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private boolean DEBUG = false;

	private String search;
	private JTextArea statusTextArea;
	private JFrame frame;
	private List DownloadFileList = Collections.synchronizedList(new ArrayList());
	private List DownloadFileIPList = Collections.synchronizedList(new ArrayList());
	private List DownloadFilePvtIPList = Collections.synchronizedList(new ArrayList());
	
	public MyDwnTableModel(String search, JTextArea statusTextArea, JFrame frame) {
		this.search = search;
		this.statusTextArea = statusTextArea;
		this.frame = frame;
	}

	
	
	private String[] DwncolumnNames = { "Serial #", "FILENAME",
			"PUBLIC IP ADDRESS", "PVT IP ADDRESS"};
	
	public Object[][] datadwn = {
			{ "1", " ", " ", " "},
			{ "2", " ", " ", " "},
			{ "3", " ", " ", " "},
			{ "4", " ", " ", " "},
			{ "5", " ", " ", " "},
			{ "6", " ", " ", " "},
			{ "7", " ", " ", " "},
			{ "8", " ", " ", " "},
			{ "9", " ", " ", " "},
			{ "10", " ", " ", " "},
			{ "11", " ", " ", " "},
			{ "12", " ", " ", " "},
			{ "13", " ", " ", " "},
			{ "14", " ", " ", " "},
			{ "15", " ", " ", " "},
			{ "16", " ", " ", " "},
			{ "17", " ", " ", " "},
			{ "18", " ", " ", " "},
			{ "19", " ", " ", " "},
			{ "20", " ", " ", " "}
			};

	
	
	public void FileList(String Filename, int Index, String IpAddress, String PvtIpAddress){
		datadwn[Index][1] = Filename;  
		datadwn[Index][2] = IpAddress;
		datadwn[Index][3] = PvtIpAddress;
		frame.repaint();
        frame.setVisible(true);
	}

	public void search(JFrame frame) {
		// JPanel discontent = new JPanel(new BorderLayout());
		System.out.println("I was here for Search!!");
		datadwn[0][1] = "test.c";
		datadwn[0][2] = "C Code Here";
		datadwn[0][3] = "192.21.34.23";
		// final JTable table = new JTable(data, columnNames);
		// table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		// table.setFillsViewportHeight(true);
		// discontent.add(table);

		// Add the text area to the content pane.
		// contentPane.add(scrollPane, BorderLayout.CENTER);
		// JScrollPane scrollPane = new JScrollPane(table);
		// discontent.add(scrollPane,BorderLayout.CENTER);
		// frame.add(discontent);
		statusTextArea
				.append("SUCCESS: YOUR FILE HAS BEEN FOUND!! CHECK TABLE FOR LOCATION"
						+ "\n");
		frame.repaint();
		frame.setVisible(true);
	}
	
	/*public List getdownloadfiles() {
		int j = 0;
		DownloadFileList.clear();
		System.out.println("This is the length of the Object :" + data.length);
		System.out.println("This is value of the check box: " + data[0][5]);
		
		for (int i=0; i<datadwn.length; i++){
			if (data[i][5].equals(true)) {
				DownloadFileList.add(data[i][1]);	
				j = i;
			}
			
		}
		System.out.println("Files to be Downloaded: " + j);
		return DownloadFileList;
	}
	
	public List getFileIPAdress() {
		int j = 0;
		DownloadFileIPList.clear();
		for (int i=0; i<data.length; i++){
			if (data[i][5].equals(true)) {
				DownloadFileIPList.add(data[i][3]);
				j = i;
			}
			
		}
		System.out.println("Files to be Downloaded: " + j);
		return DownloadFileIPList;
	}
	
	public List getFilePvtIPAdress() {
		int j = 0;
		DownloadFilePvtIPList.clear();
		for (int i=0; i<data.length; i++){
			if (data[i][5].equals(true)) {
				DownloadFilePvtIPList.add(data[i][4]);
				j = i;
			}
			
		}
		System.out.println("Files to be Downloaded: " + j);
		return DownloadFilePvtIPList;
	}*/
	

	public int getColumnCount() {
		return DwncolumnNames.length;
	}

	public int getRowCount() {
		return datadwn.length;
	}

	public String getColumnName(int col) {
		return DwncolumnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return datadwn[row][col];
	}

	/*
	 * JTable uses this method to determine the default renderer/ editor for
	 * each cell. If we didn't implement this method, then the last column would
	 * contain text ("true"/"false"), rather than a check box.
	 */
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		if (col < 2) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	public void setValueAt(Object value, int row, int col) {
		if (DEBUG) {
			System.out.println("Setting value at " + row + "," + col + " to "
					+ value + " (an instance of " + value.getClass() + ")");
		}

		datadwn[row][col] = value;
		fireTableCellUpdated(row, col);

		if (DEBUG) {
			System.out.println("New value of data:");
			printDebugData();
		}
	}

	private void printDebugData() {
		int numRows = getRowCount();
		int numCols = getColumnCount();

		for (int i = 0; i < numRows; i++) {
			System.out.print("    row " + i + ":");
			for (int j = 0; j < numCols; j++) {
				System.out.print("  " + datadwn[i][j]);
			}
			System.out.println();
		}
		System.out.println("--------------------------");
	}

}
