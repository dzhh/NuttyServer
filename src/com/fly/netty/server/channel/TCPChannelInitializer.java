package com.fly.netty.server.channel;

import com.fly.netty.codec.NettyMessageDecoder;
import com.fly.netty.codec.NettyMessageEncoder;
import com.fly.netty.server.handler.HeartBeatRespHandler;
import com.fly.netty.server.handler.LoginAuthRespHandler;
import com.fly.netty.server.handler.NettyServerHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class TCPChannelInitializer <C extends Channel> extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
        p.addLast(new ObjectEncoder());
        p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
        p.addLast(new NettyServerHandler());
		
		
//		ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
//		ch.pipeline().addLast(new NettyMessageEncoder());
//		ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
//		ch.pipeline().addLast(new LoginAuthRespHandler());
//		ch.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler());
	}

}
