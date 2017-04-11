package com.fly.netty.common;

/**
 *  响应类型消息：
 * @author fly
 *
 */
public class ReplyMsg extends BaseMsg {
    public ReplyMsg() {
        super();
        setType(MsgType.REPLY);
    }
    
//    private ReplyBody body;
// 
//    public ReplyBody getBody() {
//        return body;
//    }
// 
//    public void setBody(ReplyBody body) {
//        this.body = body;
//    }
}
