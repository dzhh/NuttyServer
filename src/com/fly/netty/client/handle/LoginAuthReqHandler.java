package com.fly.netty.client.handle;

import com.fly.netty.common.MessageType;
import com.fly.netty.common.NettyMessage;
import com.fly.netty.util.JsonUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class LoginAuthReqHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	ctx.fireExceptionCaught(cause);
    }

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		NettyMessage nettyMessage = JsonUtil.jsonToBean(msg, NettyMessage.class);
		// 如果是握手应答消息，需要判断是否认证成功
		if (nettyMessage.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
		    byte loginResult = (byte) nettyMessage.getBody().getLoginResult();
		    if (loginResult != (byte) 0) {
				// 握手失败，关闭连接
				ctx.close();
		    } else {
				System.out.println("Login is ok : " + nettyMessage);
				ctx.fireChannelRead(msg);
		    }
		} else {
		    ctx.fireChannelRead(msg);
		}
	}
}
