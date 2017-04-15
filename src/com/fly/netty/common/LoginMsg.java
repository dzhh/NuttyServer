package com.fly.netty.common;

/**
 *  登录类型消息
 * @author fly
 *
 */
public class LoginMsg extends BaseMsg {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userName;
    private String password;
    public LoginMsg() {
        super();
        setType(MsgType.LOGIN);
    }
 
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
}