package com.fly.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fly.netty.common.AskMsg;
import com.fly.netty.common.AskParams;
import com.fly.netty.common.Constants;
import com.fly.netty.common.LoginMsg;
import com.fly.netty.common.NettyConstant;


public class NettyClient {
	
	public static void main(String[]args) {
    	Constants.setClientId("001");
//      NettyClientBootstrap bootstrap=new NettyClientBootstrap(9999,"localhost");
	  	NettyClient nettyClient = new NettyClient();
	  	nettyClient.connectServer(nettyClient);
	  	while (true){
	  		try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	  		System.out.println("nettyClient.socketChannel.isActive() = " + nettyClient.socketChannel.isActive());
	  		if(nettyClient.socketChannel.isActive()) {
	  			AskMsg askMsg = new AskMsg();
		  		AskParams askParams=new AskParams();
		  		askParams.setAuth("authToken");
		  		askMsg.setParams(askParams);
		  		nettyClient.socketChannel.writeAndFlush(askMsg);
	  		} else {
	  		  	nettyClient.connectServer(nettyClient);
	  		}
	  	}
    }
	
	public void connectServer(NettyClient nettyClient) {
		try {
			nettyClient.connect(NettyConstant.PORT, NettyConstant.REMOTEIP);
		} catch (Exception e) {
			e.printStackTrace();
		}
	  	LoginMsg loginMsg=new LoginMsg();
	  	loginMsg.setPassword("fly");
	  	loginMsg.setUserName("fly");
	  	nettyClient.socketChannel.writeAndFlush(loginMsg);
	}

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    EventLoopGroup group = new NioEventLoopGroup();
    private SocketChannel socketChannel;


    public void connect(int port, String host) throws Exception {
		// 配置客户端NIO线程组
	    Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.remoteAddress(host,port);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new IdleStateHandler(20,10,0));
                socketChannel.pipeline().addLast(new ObjectEncoder());
                socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                socketChannel.pipeline().addLast(new NettyClientHandler());
            }
        });
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port),
			    new InetSocketAddress(NettyConstant.LOCALIP,
					    NettyConstant.LOCAL_PORT)).sync();
        if (future.isSuccess()) {
            socketChannel = (SocketChannel)future.channel();
            System.out.println("connect server  成功---------");
        }
	        
    }

    /**
     * @param args
     * @throws Exception
     */
//    public static void main(String[] args) throws Exception {
//    	new NettyClient().connect(NettyConstant.PORT, NettyConstant.REMOTEIP);
//    }

}
