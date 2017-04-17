package com.fly.netty.server.handler;

import com.fly.netty.common.Header;
import com.fly.netty.common.MessageType;
import com.fly.netty.common.NettyMessage;
import com.fly.netty.server.NettyChannelMap;
import com.fly.netty.util.JsonUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

/**
 * 处理登录请求
 * @author fly
 *
 */
public class LoginAuthRespHandler extends SimpleChannelInboundHandler<String> {

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
		if (nettyMessage.getHeader().getType() == MessageType.LOGIN_REQ.value()) {
		    if("fly".equals(nettyMessage.getBody().getUserName()) && "fly".equals(nettyMessage.getBody().getPassword())){
                //登录成功,把channel存到服务端的map中
                NettyChannelMap.add(nettyMessage.getHeader().getSessionID(), (SocketChannel)ctx.channel());
                System.out.println("client" + nettyMessage.getHeader().getSessionID() + " 登录成功");
                ctx.fireChannelRead(msg);
            } else {
            	String json = "";
            	NettyMessage nettyMessageResp = new NettyMessage();
            	Header header = new Header();
            	
            	nettyMessageResp.setHeader(header);
            	ctx.channel().write(json);
            	ctx.close();
            }
		} else {
		    ctx.fireChannelRead(msg);
		}
	}
}
