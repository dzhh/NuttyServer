package com.fly.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ScheduledFuture;

import com.fly.netty.common.Header;
import com.fly.netty.common.MessageType;
import com.fly.netty.common.NettyMessage;
import com.fly.netty.util.JsonUtil;

/**
 * 
 * @author fly
 *
 */
public class HeartBeatRespHandler extends SimpleChannelInboundHandler<String> {

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
		// 回复心跳
		if (nettyMessage.getHeader() != null && nettyMessage.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {
		    NettyMessage heatBeat = buildHeatBeat();
		    System.out.println("Server send heart beat messsage to client : ---> " + heatBeat);
		    ctx.writeAndFlush(JsonUtil.beanToJson(heatBeat));
		} else {
			ctx.fireChannelRead(msg);
		}
	}
	
	private NettyMessage buildHeatBeat() {
	    NettyMessage message = new NettyMessage();
	    Header header = new Header();
	    header.setType(MessageType.HEARTBEAT_RESP.value());
	    message.setHeader(header);
	    return message;
	}
}
