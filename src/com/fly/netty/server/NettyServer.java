package com.fly.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.TimeUnit;

import com.fly.netty.common.TokenMsg;
import com.fly.netty.common.NettyConstant;
import com.fly.netty.server.channel.TCPChannelInitializer;

/**
 * 主要包含对SocketChannel引用的Map,ChannelHandler的实现和Bootstrap
 * @author fly
 *
 */
public class NettyServer {

	public static void main(String[] args) throws Exception {
    	new NettyServer().bind();
    	while (true){
            SocketChannel channel = (SocketChannel)NettyChannelMap.getSocketChannel("001");
            if(channel != null){
                TokenMsg askMsg=new TokenMsg();
                channel.writeAndFlush(askMsg);
            }
            TimeUnit.SECONDS.sleep(5);
        }
    }
	
    public void bind() throws Exception {
		// 配置服务端的NIO线程组  NioEventLoopGroup是个线程组
    	// 一个用于服务器端接受客户端的连接
    	// 一个用于进行SocketChannel的读写
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		
		bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
        //通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        //保持长连接状态
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.childHandler(new TCPChannelInitializer<SocketChannel>());


//		bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
//			.option(ChannelOption.SO_BACKLOG, 100)
//			.handler(new LoggingHandler(LogLevel.INFO))
//			//绑定IO事件的处理类
//			.childHandler(new TCPChannelInitializer<SocketChannel>());
	
		// 绑定端口，同步等待成功
        ChannelFuture f= bootstrap.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
//        bootstrap.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
        if(f.isSuccess()) {
    		System.out.println("Netty server start ok : " + (NettyConstant.REMOTEIP + " : " + NettyConstant.PORT));
        }
    }
    
}
