package com.fly.netty.common;

public class PingMsg extends BaseMsg {
	
    public PingMsg() {
        super();
        setType(MsgType.PING);
    }
}
