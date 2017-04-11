package com.fly.netty.common;

import java.io.Serializable;

public class BaseMsg implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	private MsgType type;
    
	//必须唯一，否者会出现channel调用混乱
    private String clientId;
 
    //初始化客户端id
    public BaseMsg() {
        this.clientId = Constants.getClientId();
    }
 
    public String getClientId() {
        return clientId;
    }
 
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
 
    public MsgType getType() {
        return type;
    }
 
    public void setType(MsgType type) {
        this.type = type;
    }
    
    
    private ReplyBody body;
    
    public ReplyBody getBody() {
        return body;
    }
 
    public void setBody(ReplyBody body) {
        this.body = body;
    }
    
    private AskParams params;
    
    public AskParams getParams() {
        return params;
    }
 
    public void setParams(AskParams params) {
        this.params = params;
    }
}