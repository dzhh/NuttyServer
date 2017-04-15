package com.fly.netty.common;

/**
 *  响应类型消息：
 * @author fly
 *
 */
public class ReplyMsg extends BaseMsg {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReplyMsg() {
        super();
        setType(MsgType.REPLY);
    }
    
    private String serverInfo;

	public String getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(String serverInfo) {
		this.serverInfo = serverInfo;
	}
	
	private String clientInfo;
	 
    public String getClientInfo() {
        return clientInfo;
    }
 
    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }
}
