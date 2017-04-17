package com.he.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthenticatorBean extends Authenticator {

	private String userName;
	private String password;
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return   new PasswordAuthentication(userName,password);  
	}
}
