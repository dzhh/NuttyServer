package com.fly.netty.server.handler;

import com.fly.netty.codec.protobuf.MsgReqProtobuf;
import com.fly.netty.codec.protobuf.MsgRespProtobuf;
import com.fly.netty.codec.protobuf.MsgRespProtobuf.MsgType;
import com.fly.netty.server.NettyChannelMap;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

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
		MsgReqProtobuf.MsgReq req = (MsgReqProtobuf.MsgReq) msg;
		if(req.getSessionID() != null) {
			System.out.println(msg);
			ctx.writeAndFlush(resp(req.getSessionID()));
		}
	}
	
	/**
	 * 构建返回值
	 * @param sessionID
	 * @return
	 */
	private MsgRespProtobuf.MsgResp resp(String sessionID) {
		MsgRespProtobuf.MsgResp.Builder builder = MsgRespProtobuf.MsgResp.newBuilder();
		builder.setMsgType(MsgType.qita);
		builder.setMsgInfo("resp " + sessionID);
		
		return builder.build();
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
