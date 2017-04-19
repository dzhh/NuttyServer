package com.fly.netty.common;

import java.io.Serializable;

public class Body implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userName;
	
    private String password;
    
    public String getUserName() {
        return userName;
    }
 
    public void setUserName(String userName) {
        this.userName = userName;
    }
 
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
	
    private byte loginResult;
    
	public byte getLoginResult() {
		return loginResult;
	}

	public void setLoginResult(byte loginResult) {
		this.loginResult = loginResult;
	}
	
	private String msgBody;

	public String getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}
}
