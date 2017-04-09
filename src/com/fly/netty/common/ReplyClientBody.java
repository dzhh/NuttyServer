package com.fly.netty.common;

public class ReplyClientBody extends ReplyBody {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String clientInfo;
 
    public ReplyClientBody(String clientInfo) {
        this.clientInfo = clientInfo;
    }
 
    public String getClientInfo() {
        return clientInfo;
    }
 
    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }
}