package com.fly.netty.client.channel;

import com.fly.netty.client.handle.HeartBeatReqHandler;
import com.fly.netty.client.handle.LoginAuthReqHandler;
import com.fly.netty.client.handle.NettyClientHandler;
import com.fly.netty.client.handle.SubReqClientHandler;
import com.fly.netty.codec.protobuf.MsgRespProtobuf;
import com.fly.netty.server.handler.StringNettyServerHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
//import io.netty.handler.codec.serialization.ClassResolvers;
//import io.netty.handler.codec.serialization.ObjectDecoder;
//import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class ClientTCPChannelInitializer <C extends Channel> extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		/**
		 * 这个类可以对三种类型的心跳检测
		 * 1）readerIdleTime：为读超时时间（即测试端一定时间内未接受到被测试端消息）
		   2）writerIdleTime：为写超时时间（即测试端一定时间内向被测试端发送消息）
		   3）allIdleTime：所有类型的超时时间
		 */
//		pipeline.addLast(new IdleStateHandler(20, 10, 0));
////    pipeline.addLast(new ObjectEncoder());
////    pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
//	    pipeline.addLast("decoder", new StringDecoder());
//	    pipeline.addLast("encoder", new StringEncoder());
//	    
//	    pipeline.addLast(new LoginAuthReqHandler());
//	    pipeline.addLast(new HeartBeatReqHandler());
//	    pipeline.addLast(new NettyClientHandler());
		
	    pipeline.addLast(new ProtobufVarint32FrameDecoder());
	    pipeline.addLast(new ProtobufDecoder(MsgRespProtobuf.MsgResp.getDefaultInstance()));
	    pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
	    pipeline.addLast(new ProtobufEncoder());
	    pipeline.addLast(new SubReqClientHandler());
	}

}
