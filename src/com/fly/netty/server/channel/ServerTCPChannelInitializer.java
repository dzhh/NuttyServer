package com.fly.netty.server.channel;

import com.fly.netty.codec.protobuf.MsgClient2Server;
import com.fly.netty.server.handler.HeartBeatRespHandler;
import com.fly.netty.server.handler.LoginAuthRespHandler;
import com.fly.netty.server.handler.StringNettyServerHandler;
import com.fly.netty.server.handler.SubReqServerHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerTCPChannelInitializer <C extends Channel> extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
        //JDK 提供了 ObjectOutputStream 和 ObjectInputStream 通过网络将原始数据类型和 POJO 进行序列化和反序列化。
		//API并不复杂,可以应用到任何对象,支持 java.io.Serializable 接口。但它也不是非常高效的。
//		p.addLast(new ObjectEncoder());
//        p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
		
//		pipeline.addLast("decoder", new StringDecoder());
//		pipeline.addLast("encoder", new StringEncoder());
//		
//		pipeline.addLast(new LoginAuthRespHandler());
//		pipeline.addLast(new HeartBeatRespHandler());
//		pipeline.addLast(new StringNettyServerHandler());
		
	 // protobufDecoder仅仅负责编码，并不支持读半包，所以在之前，一定要有读半包的处理器。
	 // 有三种方式可以选择：
	 // 使用netty提供ProtobufVarint32FrameDecoder
	 // 继承netty提供的通用半包处理器 LengthFieldBasedFrameDecoder
	 // 继承ByteToMessageDecoder类，自己处理半包

		// 半包的处理
		ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
		// 需要解码的目标类
		ch.pipeline().addLast(new ProtobufDecoder(MsgClient2Server.Msg.getDefaultInstance()));
		ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
		ch.pipeline().addLast(new ProtobufEncoder());

		ch.pipeline().addLast(new SubReqServerHandler());
	}

}
