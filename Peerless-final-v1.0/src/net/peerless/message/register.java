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

public class register extends AbstractMessage{
	
	private static final long serialVersionUID = -940833727168119141L;

    private String command;
    private String userId;
    private String password;
    private String Question;
    private String passwordReminder;

    public register() {
    }

    public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getpassword() {
		return password;
	}

	public void setpassword(String password) {
		this.password = password;
	}
	
	public String getQuestion() {
		return Question;
	}

	public void setQuestion(String Question) {
		this.Question = Question;
	}
	
	public String getpasswordReminder() {
		return passwordReminder;
	}

	public void setpasswordReminder(String passwordReminder) {
		this.passwordReminder = passwordReminder;
	}

	@Override
    public String toString() {
        // it is a good practice to create toString() method on message classes.
        return command + userId + password + Question + passwordReminder;
    }
}


