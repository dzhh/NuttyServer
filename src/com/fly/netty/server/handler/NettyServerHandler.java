package com.fly.netty.server.handler;

import com.fly.netty.common.AskMsg;
import com.fly.netty.common.BaseMsg;
import com.fly.netty.common.LoginMsg;
import com.fly.netty.common.MsgType;
import com.fly.netty.common.PingMsg;
import com.fly.netty.common.ReplyClientBody;
import com.fly.netty.common.ReplyMsg;
import com.fly.netty.common.ReplyServerBody;
import com.fly.netty.server.NettyChannelMap;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;

/**
 * 
 * @author fly
 *
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<BaseMsg> {
	
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	closeConnect(ctx);
    }
    
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, BaseMsg msg) throws Exception {
		// 判断是否登录
    	String clientId = NettyChannelMap.getClientId((SocketChannel)ctx.channel());
		if(clientId == null) {
			if(MsgType.LOGIN.equals(msg.getType())) {
	            LoginMsg loginMsg=(LoginMsg)msg;
	            if("robin".equals(loginMsg.getUserName())&&"yao".equals(loginMsg.getPassword())){
	                //登录成功,把channel存到服务端的map中
	                NettyChannelMap.add(loginMsg.getClientId(),(SocketChannel)ctx.channel());
	                System.out.println("client"+loginMsg.getClientId()+" 登录成功");
	            }
	        } else {
	            if(NettyChannelMap.getSocketChannel(msg.getClientId())==null){
	                //说明未登录，或者连接断了，服务器向客户端发起登录请求，让客户端重新登录
	                LoginMsg loginMsg = new LoginMsg();
	                ctx.channel().writeAndFlush(loginMsg);
	            }
	        }
		}
		
        switch (msg.getType()){
            case PING:{
                PingMsg pingMsg=(PingMsg)msg;
                PingMsg replyPing=new PingMsg();
                NettyChannelMap.getSocketChannel(pingMsg.getClientId()).writeAndFlush(replyPing);
            }break;
            case ASK:{
                //收到客户端的请求
                AskMsg askMsg=(AskMsg)msg;
                if("authToken".equals(askMsg.getParams().getAuth())){
                    ReplyServerBody replyBody = new ReplyServerBody("server info $$$$ !!!");
                    ReplyMsg replyMsg = new ReplyMsg();
                    replyMsg.setBody(replyBody);
                    NettyChannelMap.getSocketChannel(askMsg.getClientId()).writeAndFlush(replyMsg);
                }
            }break;
            case REPLY:{
                //收到客户端回复
                ReplyMsg replyMsg=(ReplyMsg)msg;
                ReplyClientBody clientBody=(ReplyClientBody)replyMsg.getBody();
                System.out.println("receive client msg: "+clientBody.getClientInfo());
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
