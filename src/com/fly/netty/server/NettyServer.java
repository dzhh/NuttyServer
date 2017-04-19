package com.fly.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.TimeUnit;

import com.fly.netty.common.Header;
import com.fly.netty.common.MessageType;
import com.fly.netty.common.NettyConstant;
import com.fly.netty.common.NettyMessage;
import com.fly.netty.server.channel.ServerTCPChannelInitializer;
import com.fly.netty.util.JsonUtil;

/**
 * 主要包含对SocketChannel引用的Map,ChannelHandler的实现和Bootstrap
 * @author fly
 *
 */
public class NettyServer {

	/**
	 * 启动
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
    	new NettyServer().bind("0.0.0.0", NettyConstant.PORT);
    	
    	while (true){
            SocketChannel channel = (SocketChannel)NettyChannelMap.getSocketChannel("001");
            if(channel != null){
        		NettyMessage nettyMessage = new NettyMessage();
            	Header header = new Header();
            	header.setType(MessageType.SENDMSG_REQ.value());
            	nettyMessage.setHeader(header);
                
                channel.writeAndFlush(JsonUtil.beanToJson(nettyMessage));
            }
            TimeUnit.SECONDS.sleep(5);
        }
    }
	
    public void bind(String ip, int port) throws Exception {
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
        bootstrap.childHandler(new ServerTCPChannelInitializer<SocketChannel>());


		// 绑定端口，同步等待成功
        ChannelFuture f= bootstrap.bind(ip, port).sync();
//        ChannelFuture f= bootstrap.bind("0.0.0.0", NettyConstant.PORT).sync();
//        bootstrap.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
        if(f.isSuccess()) {
    		System.out.println("Netty server start ok : " + ("0.0.0.0" + " : " + NettyConstant.PORT));
        }
    }

}
