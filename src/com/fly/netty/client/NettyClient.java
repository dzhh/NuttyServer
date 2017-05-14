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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fly.netty.client.channel.ClientTCPChannelInitializer;
import com.fly.netty.codec.protobuf.MsgClient2Server;
import com.fly.netty.common.Body;
import com.fly.netty.common.Header;
import com.fly.netty.common.MessageType;
import com.fly.netty.common.NettyConstant;
import com.fly.netty.common.NettyMessage;
import com.fly.netty.util.JsonUtil;

/**
 * 
 * @author fly
 *
 */
public class NettyClient {
	
	
	public static void main(String[]args) {
	  	NettyClient nettyClient = new NettyClient();
	  	nettyClient.connectServer(nettyClient, 10080, "127.0.0.1");
//	  	nettyClient.connectServer(nettyClient, 10080, "192.168.253.4");
    }
	
	
	/**
	 * 连接服务器
	 * @param nettyClient
	 * @param port
	 * @param ip
	 */
	public void connectServer(NettyClient nettyClient, int port, String ip) {
		try {
			nettyClient.connect(port, ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sendMsg(nettyClient);
	}
	
	/**
	 * 发送消息
	 * @param nettyClient
	 */
	public void sendMsg(NettyClient nettyClient) {
		MsgClient2Server.Msg msgReq = subReq();
		System.out.println(msgReq);
	  	nettyClient.socketChannel.writeAndFlush(msgReq);
	}
	
    private MsgClient2Server.Msg subReq() {
    	MsgClient2Server.Machine.Builder machineBuilder = MsgClient2Server.Machine.newBuilder();
    	for(int i=0; i<6; i++) {
    		machineBuilder.addCabin(getCabinBuilder(i));
    	}
    	
    	
    	machineBuilder.setMId("001");
    	machineBuilder.setWifi("wife");
    	machineBuilder.setMobile("4g");
    	
    	MsgClient2Server.Msg.Builder msgReqbuilder = MsgClient2Server.Msg.newBuilder();
    	msgReqbuilder.setSessionID("001");
    	msgReqbuilder.setMsgType(MsgClient2Server.MsgType.init);
    	msgReqbuilder.setMachine(machineBuilder);
		return msgReqbuilder.build();
    }
    
    private MsgClient2Server.Cabin.Builder getCabinBuilder(int i) {
    	MsgClient2Server.Cabin.Builder cabinBuilder = MsgClient2Server.Cabin.newBuilder();
    	String a = "_" + i;
    	cabinBuilder.setCId("a" + a);
    	cabinBuilder.setPId("p" + a);
    	cabinBuilder.setPLock(true);
    	cabinBuilder.setPCount(10);
    	cabinBuilder.setPQuantity(60);
    	return cabinBuilder;
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
        
      ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port),
			    new InetSocketAddress("0.0.0.0", NettyConstant.LOCAL_PORT)).sync();
        if (future.isSuccess()) {
            socketChannel = (SocketChannel)future.channel();
            System.out.println("connect server  成功---------");
        }
	        
    }

    
    public void heartBeat(NettyClient nettyClient) {
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
		  		  	nettyClient.connectServer(nettyClient, 10080, "139.196.172.139");
	  			} else {
	  				break;
	  			}
	  		}
	  	}
	}

}
