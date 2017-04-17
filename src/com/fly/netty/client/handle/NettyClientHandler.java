package com.fly.netty.client.handle;

import com.fly.netty.common.NettyMessage;
import com.fly.netty.util.JsonUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

public class NettyClientHandler extends SimpleChannelInboundHandler<String> {
    //利用写空闲发送心跳检测消息
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    }
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(msg);
		NettyMessage nettyMessage = JsonUtil.jsonToBean(msg, NettyMessage.class);
        ReferenceCountUtil.release(msg);
	}
}