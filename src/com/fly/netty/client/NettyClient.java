package com.fly.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fly.netty.client.channel.ClientTCPChannelInitializer;
import com.fly.netty.common.Body;
import com.fly.netty.common.Header;
import com.fly.netty.common.MessageType;
import com.fly.netty.common.NettyConstant;
import com.fly.netty.common.NettyMessage;
import com.fly.netty.util.JsonUtil;


public class NettyClient {
	
	
	public static void main(String[]args) {
//      NettyClientBootstrap bootstrap=new NettyClientBootstrap(9999,"localhost");
	  	NettyClient nettyClient = new NettyClient();
	  	nettyClient.connectServer(nettyClient);
	  	int retry = 0;
	  	while (true){
	  		try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	  		System.out.println("nettyClient.socketChannel.isActive() = " + nettyClient.socketChannel.isActive());
	  		if(nettyClient.socketChannel.isActive()) {
	  			retry = 0;
	  			NettyMessage nettyMessageResp = new NettyMessage();
            	Header header = new Header();
            	header.setType(MessageType.HEARTBEAT_REQ.value());
            	nettyMessageResp.setHeader(header);
                
		  		nettyClient.socketChannel.writeAndFlush(JsonUtil.beanToJson(nettyMessageResp));
	  		} else {
	  			System.out.println("retry = " + retry++);
	  			if(retry < 5) {
		  		  	nettyClient.connectServer(nettyClient);
	  			} else {
	  				break;
	  			}
	  		}
	  	}
    }
	
	public void connectServer(NettyClient nettyClient) {
		try {
//			nettyClient.connect(NettyConstant.PORT, NettyConstant.REMOTEIP);
			nettyClient.connect(10080, "139.196.172.139");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		NettyMessage nettyMessageResp = new NettyMessage();
    	Header header = new Header();
    	header.setType(MessageType.LOGIN_REQ.value());
    	header.setSessionID("001");
    	nettyMessageResp.setHeader(header);
		Body body = new Body();
	  	body.setPassword("fly");
	  	body.setUserName("fly");
	  	nettyMessageResp.setBody(body);
	  	String json = JsonUtil.beanToJson(nettyMessageResp);
	  	nettyClient.socketChannel.writeAndFlush(json);
//	  	nettyClient.socketChannel.writeAndFlush(loginMsg);
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
        bootstrap.handler(new ClientTCPChannelInitializer<SocketChannel>());
        
//        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port),
//			    new InetSocketAddress(NettyConstant.LOCALIP, NettyConstant.LOCAL_PORT)).sync();
      ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port),
			    new InetSocketAddress("0.0.0.0", NettyConstant.LOCAL_PORT)).sync();
        if (future.isSuccess()) {
            socketChannel = (SocketChannel)future.channel();
            System.out.println("connect server  成功---------");
        }
	        
    }


}
