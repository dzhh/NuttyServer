package com.fly.netty.server.handler;

import com.fly.netty.common.Header;
import com.fly.netty.common.MessageType;
import com.fly.netty.common.NettyMessage;
import com.fly.netty.server.NettyChannelMap;
import com.fly.netty.util.JsonUtil;

import io.netty.channel.Channel;
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
		NettyMessage nettyMessage = JsonUtil.jsonToBean(msgStr, NettyMessage.class);
    	String clientId = NettyChannelMap.getClientId((SocketChannel)ctx.channel());
    	Channel channel = NettyChannelMap.getSocketChannel(clientId);
    	
    	if(nettyMessage.getHeader().getType() == MessageType.SERVICE_REQ.value()) {
    		NettyMessage nettyMessageResp = new NettyMessage();
        	Header header = new Header();
        	header.setType(MessageType.SERVICE_RESP.value());
        	nettyMessageResp.setHeader(header);
//            PingMsg pingMsg=(PingMsg)msg;
//            NettyChannelMap.getSocketChannel(pingMsg.getClientId()).writeAndFlush(replyPing);
//            NettyChannelMap.getSocketChannel(pingMsg.getClientId()).write(replyPing);
            String json = JsonUtil.beanToJson(nettyMessageResp);
            channel.write(json);
    	}
        ReferenceCountUtil.release(msgStr);
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
