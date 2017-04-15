package com.fly.netty.common;

/**
 * 令牌
 * @author fly
 *
 */
public class TokenMsg extends BaseMsg {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TokenMsg() {
        super();
        setType(MsgType.TOKEN);
    }
    
    private String auth;
    
    public String getAuth() {
        return auth;
    }
 
    public void setAuth(String auth) {
        this.auth = auth;
    }
    
}
