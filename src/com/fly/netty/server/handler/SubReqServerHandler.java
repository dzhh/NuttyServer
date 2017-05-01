package com.fly.netty.server.handler;

import com.fly.netty.codec.protobuf.MsgReqProtobuf;
import com.fly.netty.codec.protobuf.MsgReqProtobuf.MsgReq;
import com.fly.netty.codec.protobuf.MsgRespProtobuf;
import com.fly.netty.codec.protobuf.MsgRespProtobuf.MsgResp;
import com.fly.netty.codec.protobuf.MsgRespProtobuf.MsgType;
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
		MsgReq msgReq = (MsgReqProtobuf.MsgReq) msg;
		// 消息类型
		com.fly.netty.codec.protobuf.MsgReqProtobuf.MsgType msgType =  msgReq.getMsgType();
		
		// 初始化 init
		if(msgType.equals(MsgReqProtobuf.MsgType.init)) {
			initMsg(ctx, msgReq);
		} else if(msgType.equals(MsgReqProtobuf.MsgType.open)) {
			
		} else if(msgType.equals(MsgReqProtobuf.MsgType.lock)) {
			
		} else if(msgType.equals(MsgReqProtobuf.MsgType.heat)) {
			
		} else if(msgType.equals(MsgReqProtobuf.MsgType.update)) {
			
		} else if(msgType.equals(MsgReqProtobuf.MsgType.error)) {
			
		}
		
		
		
	}
	
	/**
	 * 初始化信息
	 * @param ctx
	 * @param msgReq
	 * @throws Exception
	 */
	private void initMsg(ChannelHandlerContext ctx, MsgReq msgReq) throws Exception {
		//存储连接  存储机器状态
		NettyChannelMap.add(msgReq.getSessionID(), (SocketChannel)ctx.channel());
		MsgReqMap.add(msgReq.getSessionID(), msgReq);
		
		//返回 ok
		MsgRespProtobuf.MsgResp.Builder builder = MsgRespProtobuf.MsgResp.newBuilder();
		builder.setMsgType(MsgType.qita);
		builder.setMsgInfo("ok");
		MsgResp msgResp = builder.build();
		ctx.writeAndFlush(msgResp);
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
