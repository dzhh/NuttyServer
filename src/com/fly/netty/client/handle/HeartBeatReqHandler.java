package com.fly.netty.client.handle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ScheduledFuture;

import com.fly.netty.common.Header;
import com.fly.netty.common.MessageType;
import com.fly.netty.common.NettyMessage;
import com.fly.netty.util.JsonUtil;


public class HeartBeatReqHandler extends SimpleChannelInboundHandler<String> {

    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	    throws Exception {
		cause.printStackTrace();
		if (heartBeat != null) {
		    heartBeat.cancel(true);
		    heartBeat = null;
		}
		ctx.fireExceptionCaught(cause);
    }

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//		NettyMessage message = (NettyMessage) msg;
		NettyMessage nettyMessage = JsonUtil.jsonToBean(msg, NettyMessage.class);
		// 握手成功，主动发送心跳消息
		if (nettyMessage.getHeader() != null && nettyMessage.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
			NettyMessage heatBeat = buildHeatBeat();
		    System.out.println("Client send heart beat messsage to server : ---> " + heatBeat);
		    ctx.writeAndFlush(JsonUtil.beanToJson(heatBeat));
		} else if (nettyMessage.getHeader() != null && nettyMessage.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()) {
		    System.out.println("Client receive server heart beat message : ---> " + msg);
		} else
		    ctx.fireChannelRead(msg);
	}
	
	private NettyMessage buildHeatBeat() {
	    NettyMessage message = new NettyMessage();
	    Header header = new Header();
	    header.setType(MessageType.HEARTBEAT_REQ.value());
	    message.setHeader(header);
	    return message;
	}
}
