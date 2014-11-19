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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version $Rev$, $Date$
 *
 */
public class LoginDialog extends JDialog {
    private static final long serialVersionUID = 2009384520250666216L;

    private String serverAddress;

    private String username;

    private boolean useSsl;

    private boolean cancelled = false;

    public LoginDialog(Frame owner) throws HeadlessException {
        super(owner, "Connect", true);

        serverAddress = "localhost:1234";
        username = "user" + Math.round(Math.random() * 10);

        final JTextField serverAddressField = new JTextField(serverAddress);
        final JTextField usernameField = new JTextField(username);
        final JCheckBox useSslCheckBox = new JCheckBox("Use SSL", false);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        content.add(new JLabel("Server address"));
        content.add(serverAddressField);
        content.add(new JLabel("Username"));
        content.add(usernameField);
        content.add(useSslCheckBox);

        JButton okButton = new JButton();
        okButton.setAction(new AbstractAction("OK") {
            private static final long serialVersionUID = -2292183622613960604L;

            public void actionPerformed(ActionEvent e) {
                serverAddress = serverAddressField.getText();
                username = usernameField.getText();
                useSsl = useSslCheckBox.isSelected();
                LoginDialog.this.dispose();
            }
        });

        JButton cancelButton = new JButton();
        cancelButton.setAction(new AbstractAction("Cancel") {
            private static final long serialVersionUID = 6122393546173723305L;

            public void actionPerformed(ActionEvent e) {
                cancelled = true;
                LoginDialog.this.dispose();
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

    public String getServerAddress() {
        return serverAddress;
    }

    public String getUsername() {
        return username;
    }

    public boolean isUseSsl() {
        return useSsl;
    }
}
