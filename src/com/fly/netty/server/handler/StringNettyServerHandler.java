package com.fly.netty.server.handler;

import com.fly.netty.common.TokenMsg;
import com.fly.netty.common.BaseMsg;
import com.fly.netty.common.LoginMsg;
import com.fly.netty.common.MsgType;
import com.fly.netty.common.PingMsg;
import com.fly.netty.common.ReplyMsg;
import com.fly.netty.server.NettyChannelMap;
import com.fly.netty.util.JsonUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;

/**
 * 
 * @author fly
 *
 */
public class StringNettyServerHandler extends SimpleChannelInboundHandler<String> {
	
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	closeConnect(ctx);
    }
    
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msgStr) throws Exception {
		System.out.println(msgStr);
		BaseMsg msg = JsonUtil.jsonToBean(msgStr, BaseMsg.class);
		
		// 判断是否登录
    	String clientId = NettyChannelMap.getClientId((SocketChannel)ctx.channel());
		if(clientId == null) {
			if(MsgType.LOGIN.equals(msg.getType())) {
				LoginMsg loginMsg = JsonUtil.jsonToBean(msgStr, LoginMsg.class);
//	            LoginMsg loginMsg = (LoginMsg)msg;
	            if("fly".equals(loginMsg.getUserName()) && "fly".equals(loginMsg.getPassword())){
	                //登录成功,把channel存到服务端的map中
	                NettyChannelMap.add(loginMsg.getClientId(),(SocketChannel)ctx.channel());
	                System.out.println("client"+loginMsg.getClientId()+" 登录成功");
	            }
	        } else {
	            if(NettyChannelMap.getSocketChannel(msg.getClientId())==null){
	                //说明未登录，或者连接断了，服务器向客户端发起登录请求，让客户端重新登录
	                LoginMsg loginMsg = new LoginMsg();
//	                ctx.channel().writeAndFlush(loginMsg);
	                String json = JsonUtil.beanToJson(loginMsg);
	                ctx.channel().write(json);

	            }
	        }
		}
		
        switch (msg.getType()){
            case PING:{
//                PingMsg pingMsg=(PingMsg)msg;
                PingMsg pingMsg = JsonUtil.jsonToBean(msgStr, PingMsg.class);
                PingMsg replyPing = new PingMsg();
//                NettyChannelMap.getSocketChannel(pingMsg.getClientId()).writeAndFlush(replyPing);
//                NettyChannelMap.getSocketChannel(pingMsg.getClientId()).write(replyPing);
                String json = JsonUtil.beanToJson(replyPing);
                NettyChannelMap.getSocketChannel(pingMsg.getClientId()).write(json);
            }break;
            case TOKEN:{
                //收到客户端的请求
//                AskMsg askMsg=(AskMsg)msg;
            	TokenMsg askMsg = JsonUtil.jsonToBean(msgStr, TokenMsg.class);
                if("authToken".equals(askMsg.getAuth())){
                    ReplyMsg replyMsg = new ReplyMsg();
                    replyMsg.setServerInfo("server info $$$$ !!!");
//                    NettyChannelMap.getSocketChannel(askMsg.getClientId()).write(replyMsg);
                    String json = JsonUtil.beanToJson(replyMsg);
                  NettyChannelMap.getSocketChannel(askMsg.getClientId()).write(json);
                }
            }break;
            case REPLY:{
                //收到客户端回复
//                ReplyMsg replyMsg = (ReplyMsg)msg;
            	ReplyMsg replyMsg = JsonUtil.jsonToBean(msgStr, ReplyMsg.class);
                System.out.println("receive client msg: " + replyMsg.getClientInfo());
            }break;
            default:break;
        }
        ReferenceCountUtil.release(msg);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		System.out.println("exceptionCaught " + cause.getMessage());
		closeConnect(ctx);
	}
	
	/**
     * 关闭连接
     * @param ctx
     */
    public void closeConnect(ChannelHandlerContext ctx) {
    	//channel失效，从Map中移除
    	String clientId = NettyChannelMap.getClientId((SocketChannel)ctx.channel());
    	if(clientId != null) {
    		NettyChannelMap.remove(clientId);
            //关闭连接
        	ctx.close();
    		System.out.println("close clientId = " + clientId + " conn = " + ctx.channel().toString());
    	}
    }
}
