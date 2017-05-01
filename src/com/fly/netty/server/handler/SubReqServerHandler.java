package com.fly.netty.server.handler;

import com.fly.netty.codec.protobuf.MsgClient2Server;
import com.fly.netty.codec.protobuf.MsgServer2Client;
import com.fly.netty.server.MsgReqMap;
import com.fly.netty.server.NettyChannelMap;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

/**
 * 具体处理消息
 * @author fly
 *
 */
public class SubReqServerHandler extends SimpleChannelInboundHandler { 

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	closeConnect(ctx);
    }
    
	@Override
	/**
	 * 接受请求 处理请求
	 */
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(msg);
		MsgClient2Server.Msg msgReq = (MsgClient2Server.Msg) msg;
		// 消息类型
		MsgClient2Server.MsgType msgType =  msgReq.getMsgType();
		
		// 初始化 init
		if(msgType.equals(MsgClient2Server.MsgType.init)) {
			initMsg(ctx, msgReq);
		} else if(msgType.equals(MsgClient2Server.MsgType.open)) {
			
		} else if(msgType.equals(MsgClient2Server.MsgType.lock)) {
			
		} else if(msgType.equals(MsgClient2Server.MsgType.heat)) {
			
		} else if(msgType.equals(MsgClient2Server.MsgType.update)) {
			
		} else if(msgType.equals(MsgClient2Server.MsgType.error)) {
			
		}
		
		
		
	}
	
	/**
	 * 初始化信息
	 * @param ctx
	 * @param msgReq
	 * @throws Exception
	 */
	private void initMsg(ChannelHandlerContext ctx, MsgClient2Server.Msg msg) throws Exception {
		//存储连接  存储机器状态
		NettyChannelMap.add(msg.getSessionID(), (SocketChannel)ctx.channel());
		MsgReqMap.add(msg.getSessionID(), msg);
		
		//返回 ok
		MsgServer2Client.Msg.Builder builder = MsgServer2Client.Msg.newBuilder();
		builder.setMsgType(MsgServer2Client.MsgType.qita);
		builder.setMsgInfo("ok");
		MsgServer2Client.Msg msgResp = builder.build();
		ctx.writeAndFlush(msgResp);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		System.out.println("exceptionCaught " + cause.getMessage());
		closeConnect(ctx);
	}
	
	/**
     * 关闭连接
     * 清除数据
     * @param ctx
     */
    public void closeConnect(ChannelHandlerContext ctx) {
    	//channel失效，从Map中移除
    	String clientId = NettyChannelMap.getClientId((SocketChannel)ctx.channel());
    	if(clientId != null) {
    		NettyChannelMap.remove(clientId);
    		MsgReqMap.remove(clientId);
            //关闭连接
        	ctx.close();
    		System.out.println("close clientId = " + clientId + " conn = " + ctx.channel().toString());
    	}
    }
	
	
}
