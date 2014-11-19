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

class MyTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private boolean DEBUG = false;

	private String search;
	private JTextArea statusTextArea;
	private JFrame frame;
	private int count = 0;
	private List DownloadFileList = Collections.synchronizedList(new ArrayList());
	private List DownloadFileIPList = Collections.synchronizedList(new ArrayList());
	private List DownloadFilePvtIPList = Collections.synchronizedList(new ArrayList());
	
	public MyTableModel(String search, JTextArea statusTextArea, JFrame frame) {
		this.search = search;
		this.statusTextArea = statusTextArea;
		this.frame = frame;
	}

	private String[] columnNames = { "Serial #", "FILENAME", "ABSTRACT",
			"PUBLIC IP ADDRESS", "PVT IP ADDRESS", "DOWNLOAD SELECTION" };
	
	private String[] DwncolumnNames = { "Serial #", "FILENAME", "ABSTRACT",
			"PUBLIC IP ADDRESS", "PVT IP ADDRESS"};
	
	public Object[][] datadwn = {
			{ "1", "NULL", "NULL", "NULL", "NULL"},
			{ "2", "NULL", "NULL", "NULL", "NULL"},
			{ "3", "NULL", "NULL", "NULL", " "},
			{ "4", " ", " ", " ", "123.244.32.121"},
			{ "5", " ", " ", " ", " "},
			{ "6", " ", " ", " ", " "} 
			};

	public Object[][] data =  {
			{ "1", " ", " ", " ", " ",new Boolean(false) },
			{ "2", " ", " ", " ", " ",new Boolean(false) },
			{ "3", " ", " ", " ", " ",new Boolean(false) },
			{ "4", " ", " ", " ", " ",new Boolean(false) },
			{ "5", " ", " ", " ", " ",new Boolean(false) },
			{ "6", " ", " ", " ", " ",new Boolean(false) },
			{ "7", " ", " ", " ", " ",new Boolean(false) },
			{ "8", " ", " ", " ", " ",new Boolean(false) },
			{ "9", " ", " ", " ", " ",new Boolean(false) },
			{ "10", " ", " ", " ", " ",new Boolean(false) },	
			{ "11", " ", " ", " ", " ",new Boolean(false) },
			{ "12", " ", " ", " ", " ",new Boolean(false) },
			{ "13", " ", " ", " ", " ",new Boolean(false) },
			{ "14", " ", " ", " ", " ",new Boolean(false) },
			{ "15", " ", " ", " ", " ",new Boolean(false) },
			{ "16", " ", " ", " ", " ",new Boolean(false) },
			{ "17", " ", " ", " ", " ",new Boolean(false) },
			{ "18", " ", " ", " ", " ",new Boolean(false) },
			{ "19", " ", " ", " ", " ",new Boolean(false) },
			{ "20", " ", " ", " ", " ",new Boolean(false) }
	};
	
	public void FileList(ResultSignatureMessage signatureResultList){
		data[ClientConfig.count][1] = signatureResultList.getFileName(); 
		data[ClientConfig.count][2] = signatureResultList.getFileAbstract(); 
		data[ClientConfig.count][3] = signatureResultList.getIpAddress();
		data[ClientConfig.count][4] = signatureResultList.getPvtIpAddress();
		frame.repaint();
        frame.setVisible(true);
        ClientConfig.count++;
        if (ClientConfig.count == 20) ClientConfig.count = 0;
	}

	public void search(JFrame frame) {
		// JPanel discontent = new JPanel(new BorderLayout());
		System.out.println("I was here for Search!!");
		data[0][1] = "test.c";
		data[0][2] = "C Code Here";
		data[0][3] = "192.21.34.23";
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
	
	public List getdownloadfiles() {
		int j = 0;
		DownloadFileList.clear();
		System.out.println("This is the length of the Object :" + data.length);
		System.out.println("This is value of the check box: " + data[0][5]);
		
		for (int i=0; i<data.length; i++){
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
	}
	

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
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

		data[row][col] = value;
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
				System.out.print("  " + data[i][j]);
			}
			System.out.println();
		}
		System.out.println("--------------------------");
	}

}