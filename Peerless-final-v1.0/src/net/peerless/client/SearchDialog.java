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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.text.StyledDocument;

import org.apache.mina.core.session.IoSession;

import net.peerless.client.MyTableModel;
import net.peerless.dist.SuperPeerManager;

import net.peerless.filetransfer.PeerlessSendUdpMessageSupport.FileSent;
import net.peerless.message.ResultSignatureMessage;

public class SearchDialog implements FileSent{
    private static final long serialVersionUID = 2009384520250666216L;
    
    private String Search;

    
    
    public JMenuBar menuBar;
    public JMenu menu, submenu;
    public JMenuItem menuItem;
    public JRadioButtonMenuItem rbMenuItem;
    public JCheckBoxMenuItem cbMenuItem;
    public int check = 1;
    public JFrame frame;
    public JPanel discontent;
    public JTextArea area;
    public IoSession session;
    
    private String search;

	private List DownloadFileList = Collections.synchronizedList(new ArrayList());
	private List DownloadFileIPList = Collections.synchronizedList(new ArrayList());
	private List DownloadFilePvtIPList = Collections.synchronizedList(new ArrayList());
	private JTextArea statusTextArea;
	private JTextField searchTextField;
	private MyTableModel searchTableModel;
	private MyDwnTableModel searchDwnTable;
	private MyTableModel downloadStatusTableModel;
	private JTable jTable;
	private JTable downloadStatusTable;
	

	private SuperPeerManager superPeerManager;
	PeerlessClientCommSupport clientCommSupport;
    
    
	
	public void FileList(ResultSignatureMessage signatureResultList){
		searchTableModel.FileList(signatureResultList);
	}
	
	public void NoResult()
	{
		JOptionPane.showMessageDialog(menuItem, "SORRY, NO RESULT FOUND FOR YOUR SEARCH!!");
	}
	
	public void search () {
		//JPanel discontent = new JPanel(new BorderLayout());
		
		//data[0][1] = Search; data[0][2] = "C Code Here"; data[0][3] = "192.21.34.23";
		//final JTable table = new JTable(data, columnNames);
        //table.setPreferredScrollableViewportSize(new Dimension(500, 70));
       // table.setFillsViewportHeight(true);
      //  discontent.add(table);
        
        //Add the text area to the content pane.
        //contentPane.add(scrollPane, BorderLayout.CENTER);
      //  JScrollPane scrollPane = new JScrollPane(table);
      //  discontent.add(scrollPane,BorderLayout.CENTER);
        //frame.add(discontent);
		statusTextArea.append("SUCCESS: YOUR FILE HAS BEEN FOUND!! CHECK TABLE FOR LOCATION" + "\n");
        frame.repaint();
        frame.setVisible(true);
	}
	
	public void append (String message){
		
		statusTextArea.append(message + "\n");
	}
	
//	public SearchDialog () {
//		
//	}

    public SearchDialog(JFrame owner, SuperPeerManager superPeerManager, IoSession session) throws HeadlessException {
    	
    	this.session = session;
    	this.superPeerManager = superPeerManager;
    	JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setLayout(new BorderLayout());
		frame = owner;

		searchTextField = new JTextField(40);
		searchTextField.setBorder(BorderFactory.createLineBorder(Color.black));
		searchTextField.setEditable(true);

		JButton searchButton = new JButton();
		JButton downloadButton = new JButton();
		JButton publishButton = new JButton();

		JPanel searchPanel = new JPanel();
		searchPanel.add(searchTextField);
		searchPanel.add(searchButton);
		searchPanel.add(downloadButton);
		searchPanel.add(publishButton);
		
		statusTextArea = new JTextArea(10, 50);
		//resultTextArea.setLineWrap(true);
		statusTextArea.setEditable(false);
		statusTextArea.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JPanel resultTextPanel = new JPanel(new BorderLayout());
		resultTextPanel.add(statusTextArea, BorderLayout.CENTER);
		JScrollPane scroll = new JScrollPane(statusTextArea);
		
		searchTableModel = new MyTableModel(search, statusTextArea, frame);
		jTable = new JTable(searchTableModel);
		jTable.setBorder(BorderFactory.createLineBorder(Color.black));
		JScrollPane searchTableScrollPane = new JScrollPane(jTable);
		
		searchDwnTable = new MyDwnTableModel(search, statusTextArea, frame);
		downloadStatusTable = new JTable(searchDwnTable);
		downloadStatusTable.setBorder(BorderFactory.createLineBorder(Color.black));
		JScrollPane downloadStatusTableScrollPane = new JScrollPane(downloadStatusTable);

		clientCommSupport = new PeerlessClientCommSupport(session, superPeerManager);
        
		downloadButton.setAction(new AbstractAction("DOWNLOAD") {
        	private static final long serialVersionUID = -2292183622613960604L;
        	
        	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				DownloadFileList = searchTableModel.getdownloadfiles();
				DownloadFileIPList = searchTableModel.getFileIPAdress();
				DownloadFilePvtIPList = searchTableModel.getFilePvtIPAdress();
				for (int i = 0; i < DownloadFileList.size(); i++){
					Object data = DownloadFileList.get(i);
					Object IP = DownloadFileIPList.get(i);
					Object PvtIP = DownloadFilePvtIPList.get(i);
					System.out.println("The Public IP address from GUI is: " + IP.toString());
					System.out.println("The Pvt IP address from GUI is: " + PvtIP.toString());
					System.out.println("DOWNLOADING " + data + "...");
					statusTextArea.append("DOWNLOADING " + data + "..." + "\n");
					clientCommSupport.requestFile(PvtIP.toString(),data.toString());
					searchDwnTable.FileList(data.toString(), i, IP.toString(), PvtIP.toString());
				}
				
			}
        });
        
		publishButton.setAction(new AbstractAction("PUBLISH") {
        	private static final long serialVersionUID = -2292183622613960604L;
        	
        	
			@Override
			public void actionPerformed(ActionEvent arg0) {
			clientCommSupport.publishFileAll();	
				
			
		}
    });
        
		searchButton.setAction(new AbstractAction("SEARCH") {
        	private static final long serialVersionUID = -2292183622613960604L;
        	
        	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Search = searchTextField.getText();
				if (Search.isEmpty()) JOptionPane.showMessageDialog(menuItem, "YOU NEED TO ENTER A FILENAME!!");
				ClientConfig.count = 0;	
				if (!Search.isEmpty()){
				System.out.println("Searching For File: " + Search);
				statusTextArea.append("Searching For File: " + Search + "\n");
				
				try {
					clientCommSupport.searchFile(Search, SearchDialog.this);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//search();
				
				}
				//SearchDialog.this.dispose();
			}
        });
        
        
        
        JProgressBar progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);


		contentPane.add(searchPanel, BorderLayout.PAGE_START);
		contentPane.add(searchTableScrollPane, BorderLayout.CENTER);
		//contentPane.add(statusTextArea, BorderLayout.PAGE_END);
		contentPane.add(scroll, BorderLayout.PAGE_END);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Search", null, contentPane, "Does nothing");
		tabbedPane.addTab("Download Status", null, downloadStatusTableScrollPane, "Does nothing");
		
		owner.setContentPane(tabbedPane);
		//owner.setContentPane(contentPane);


		// owner.setSize(250, 100);
		owner.setVisible(true);
            }
}