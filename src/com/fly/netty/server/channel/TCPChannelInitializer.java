package com.fly.netty.server.channel;

import com.fly.netty.server.handler.StringNettyServerHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TCPChannelInitializer <C extends Channel> extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
        //JDK 提供了 ObjectOutputStream 和 ObjectInputStream 通过网络将原始数据类型和 POJO 进行序列化和反序列化。
		//API并不复杂,可以应用到任何对象,支持 java.io.Serializable 接口。但它也不是非常高效的。
//		p.addLast(new ObjectEncoder());
//        p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
		
		
		pipeline.addLast("decoder", new StringDecoder());
		pipeline.addLast("encoder", new StringEncoder());
		pipeline.addLast(new StringNettyServerHandler());
	}

}
