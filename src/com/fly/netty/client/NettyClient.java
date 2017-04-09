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

import com.fly.netty.codec.NettyMessageDecoder;
import com.fly.netty.codec.NettyMessageEncoder;
import com.fly.netty.common.AskMsg;
import com.fly.netty.common.AskParams;
import com.fly.netty.common.Constants;
import com.fly.netty.common.LoginMsg;
import com.fly.netty.common.NettyConstant;


public class NettyClient {
	
	public static void main(String[]args) throws Exception {
    	Constants.setClientId("001");
//      NettyClientBootstrap bootstrap=new NettyClientBootstrap(9999,"localhost");
	  	NettyClient nettyClient = new NettyClient();
	  	nettyClient.connect(NettyConstant.PORT, NettyConstant.REMOTEIP);
	  	LoginMsg loginMsg=new LoginMsg();
	  	loginMsg.setPassword("yao");
	  	loginMsg.setUserName("robin");
	  	nettyClient.socketChannel.writeAndFlush(loginMsg);
	  	while (true){
	  		TimeUnit.SECONDS.sleep(3);
	  		AskMsg askMsg=new AskMsg();
	  		AskParams askParams=new AskParams();
	  		askParams.setAuth("authToken");
	  		askMsg.setParams(askParams);
	  		nettyClient.socketChannel.writeAndFlush(askMsg);
	  	}
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
	        
	        
//		    bootstrap.group(group).channel(NioSocketChannel.class)
//			    .option(ChannelOption.TCP_NODELAY, true)
//			    .handler(new ChannelInitializer<SocketChannel>() {
//				@Override
//				public void initChannel(SocketChannel ch)
//					throws Exception {
//				    ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
//				    ch.pipeline().addLast("MessageEncoder", new NettyMessageEncoder());
//				    ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
//				    ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());
//				    ch.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler());
//				}
//			    });
		    // 发起异步连接操作
//		    ChannelFuture future = b.connect(new InetSocketAddress(host, port),
//			    new InetSocketAddress(NettyConstant.LOCALIP,
//				    NettyConstant.LOCAL_PORT)).sync();
//		    future.channel().closeFuture().sync();
//		} finally {
//		    // 所有资源释放完成之后，清空资源，再次发起重连操作
//		    executor.execute(new Runnable() {
//				@Override
//				public void run() {
//				    try {
//					TimeUnit.SECONDS.sleep(1);
//					try {
//					    connect(NettyConstant.PORT, NettyConstant.REMOTEIP);// 发起重连操作
//					} catch (Exception e) {
//					    e.printStackTrace();
//					}
//				    } catch (InterruptedException e) {
//					e.printStackTrace();
//				    }
//				}
//		    });
//		}
    }

    /**
     * @param args
     * @throws Exception
     */
//    public static void main(String[] args) throws Exception {
//    	new NettyClient().connect(NettyConstant.PORT, NettyConstant.REMOTEIP);
//    }

}
