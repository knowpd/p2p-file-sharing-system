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
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.peerless.common.CommonConfig;

/**
 *
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version $Rev$, $Date$
 *
 */
public class RegisterDialog extends JDialog {
    private static final long serialVersionUID = 3009384520250666216L;

    //private String serverAddress;

    private String userName;
    
    private String userId;
    
    private String userPassword;
    
    private String userQuestion;
    
    private String userPasswordReminder;

    private boolean useSsl;

    private boolean cancelled = false;
    
    private char[] pass;

    public RegisterDialog(Frame owner) throws HeadlessException {
        super(owner, "Register", true);

        //serverAddress = ClientConfig.peerlessServerAddress;
        userName = "user" + Math.round(Math.random() * 10);
        userId = "id" + Math.round(Math.random() * 10);
        userQuestion = CommonConfig.question1String;

        final JTextField userNameField = new JTextField(userName);
        final JTextField userIdField = new JTextField(userId);
        final JPasswordField userPasswordField = new JPasswordField(userPassword);
        final JRadioButton question1Button = new JRadioButton(CommonConfig.question1String);
        question1Button.setSelected(true);
        final JRadioButton question2Button = new JRadioButton(CommonConfig.question2String);
        final JRadioButton question3Button = new JRadioButton(CommonConfig.question3String);
        final JTextField userPasswordReminderField = new JTextField(userPasswordReminder);
        final JCheckBox useSslCheckBox = new JCheckBox("Use SSL", false);

        ButtonGroup group = new ButtonGroup();
        group.add(question1Button);
        group.add(question2Button);
        group.add(question3Button);

        question1Button.setAction(new AbstractAction(CommonConfig.question1String) {
            private static final long serialVersionUID = -5292183622613960604L;

            public void actionPerformed(ActionEvent e) {
            	userQuestion = CommonConfig.question1String;
            }
        });
        
        question2Button.setAction(new AbstractAction(CommonConfig.question2String) {
            private static final long serialVersionUID = -6292183622613960604L;

            public void actionPerformed(ActionEvent e) {
            	userQuestion = CommonConfig.question2String;
            }
        });
        
        question3Button.setAction(new AbstractAction(CommonConfig.question3String) {
            private static final long serialVersionUID = -7292183622613960604L;

            public void actionPerformed(ActionEvent e) {
            	userQuestion = CommonConfig.question3String;
            }
        });
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        content.add(new JLabel("Name"));
        content.add(userNameField);
        content.add(new JLabel("ID (=e-mail)"));
        content.add(userIdField);
        content.add(new JLabel("Password"));
        content.add(userPasswordField);
        content.add(new JLabel("Question"));
        content.add(question1Button);
        content.add(question2Button);
        content.add(question3Button);
        content.add(new JLabel("Answer"));
        content.add(userPasswordReminderField);
        //content.add(useSslCheckBox);

        JButton okButton = new JButton();
        okButton.setAction(new AbstractAction("OK") {
            private static final long serialVersionUID = -3292183622613960604L;

            public void actionPerformed(ActionEvent e) {
            	userName = userNameField.getText();
                userId = userIdField.getText();
                //userPassword = userPasswordField.getText();
                pass = userPasswordField.getPassword();
                String s = new String(pass);
                userPassword = s;
                userPasswordReminder = userPasswordReminderField.getText();
                useSsl = useSslCheckBox.isSelected();
                RegisterDialog.this.dispose();
            }
        });

        JButton cancelButton = new JButton();
        cancelButton.setAction(new AbstractAction("Cancel") {
            private static final long serialVersionUID = 7122393546173723305L;

            public void actionPerformed(ActionEvent e) {
                cancelled = true;
                RegisterDialog.this.dispose();
            }
        });

        JPanel buttons = new JPanel();
        buttons.add(okButton);
        buttons.add(cancelButton);

        getContentPane().add(content, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
    }
    

    public boolean isCancelled() {
        return cancelled;
    }

    
    
    public String getUserName() {
		return userName;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public String getUserQuestion() {
		return userQuestion;
	}

	public String getUserPasswordReminder() {
		return userPasswordReminder;
	}

	public boolean isUseSsl() {
        return useSsl;
    }
}
