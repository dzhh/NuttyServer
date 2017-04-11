package com.fly.netty.client;

import com.fly.netty.common.BaseMsg;
import com.fly.netty.common.LoginMsg;
import com.fly.netty.common.MsgType;
import com.fly.netty.common.PingMsg;
import com.fly.netty.common.ReplyClientBody;
import com.fly.netty.common.ReplyMsg;
import com.fly.netty.common.ReplyServerBody;
import com.fly.netty.util.JsonUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class NettyClientHandler extends SimpleChannelInboundHandler< String> {
    //利用写空闲发送心跳检测消息
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                    PingMsg pingMsg=new PingMsg();
                    ctx.writeAndFlush(pingMsg);
                    System.out.println("send ping to server----------");
                    break;
                default:
                    break;
            }
        }
    }
//    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, String baseMsgStr) throws Exception {
    	BaseMsg baseMsg = JsonUtil.jsonToBean(baseMsgStr, BaseMsg.class);
    	MsgType msgType=baseMsg.getType();
        switch (msgType){
            case LOGIN:{
                //向服务器发起登录
                LoginMsg loginMsg=new LoginMsg();
                loginMsg.setPassword("yao");
                loginMsg.setUserName("robin");
//                channelHandlerContext.writeAndFlush(loginMsg);
                String json = JsonUtil.beanToJson(loginMsg);
                channelHandlerContext.writeAndFlush(json);
            }break;
            case PING:{
                System.out.println("receive ping from server----------");
            }break;
            case ASK:{
                ReplyClientBody replyClientBody=new ReplyClientBody("client info **** !!!");
                ReplyMsg replyMsg=new ReplyMsg();
                replyMsg.setBody(replyClientBody);
//                channelHandlerContext.writeAndFlush(replyMsg);
                String json = JsonUtil.beanToJson(replyMsg);
                channelHandlerContext.writeAndFlush(json);
            }break;
            case REPLY:{
                ReplyMsg replyMsg=(ReplyMsg)baseMsg;
                ReplyServerBody replyServerBody=(ReplyServerBody)replyMsg.getBody();
                System.out.println("receive client msg: "+replyServerBody.getServerInfo());
            }
            default:break;
        }
        ReferenceCountUtil.release(msgType);
    }
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, String baseMsgStr) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(baseMsgStr);
    	BaseMsg baseMsg = JsonUtil.jsonToBean(baseMsgStr, BaseMsg.class);
		MsgType msgType = baseMsg.getType();
        switch (msgType){
            case LOGIN:{
                //向服务器发起登录
                LoginMsg loginMsg=new LoginMsg();
                loginMsg.setPassword("yao");
                loginMsg.setUserName("robin");
//                channelHandlerContext.writeAndFlush(loginMsg);
                String json = JsonUtil.beanToJson(loginMsg);
                channelHandlerContext.writeAndFlush(json);
            }break;
            case PING:{
                System.out.println("receive ping from server----------");
            }break;
            case ASK:{
                ReplyClientBody replyClientBody=new ReplyClientBody("client info **** !!!");
                ReplyMsg replyMsg=new ReplyMsg();
                replyMsg.setBody(replyClientBody);
//                channelHandlerContext.writeAndFlush(replyMsg);
                String json = JsonUtil.beanToJson(replyMsg);
                channelHandlerContext.writeAndFlush(json);
            }break;
            case REPLY:{
//                ReplyMsg replyMsg=(ReplyMsg)baseMsg;
            	ReplyMsg replyMsg = JsonUtil.jsonToBean(baseMsgStr, ReplyMsg.class);
//                ReplyServerBody replyServerBody = (ReplyServerBody)replyMsg.getBody();
//                System.out.println("receive client msg: "+replyServerBody.getServerInfo());
            	System.out.println("receive client msg: " + replyMsg.getBody().getServerInfo());
            	
            }
            default:break;
        }
        ReferenceCountUtil.release(msgType);
	}
}