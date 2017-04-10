package com.fly.netty.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

/**
 * 存储客户端与服务器端的连接
 * @author fly
 *
 */
public class NettyChannelMap {
    
	private static Map<String, SocketChannel> clientIdSocketChannelMap = new ConcurrentHashMap<String, SocketChannel>();
	private static Map<SocketChannel ,String> socketChannelClientIdMap = new ConcurrentHashMap<SocketChannel ,String>();

	
    public static void add(String clientId, SocketChannel socketChannel){
    	clientIdSocketChannelMap.put(clientId, socketChannel);
    	socketChannelClientIdMap.put(socketChannel, clientId);
    }
    
    public static Channel getSocketChannel(String clientId){
       return clientIdSocketChannelMap.get(clientId);
    }
    
    public static String getClientId(SocketChannel socketChannel){
        return socketChannelClientIdMap.get(socketChannel);
     }
     
    
    public static void remove(SocketChannel socketChannel){
    	String clientId = socketChannelClientIdMap.get(socketChannel);
    	clientIdSocketChannelMap.remove(clientId);
    	socketChannelClientIdMap.remove(socketChannel);
    }
    
    
    public static void remove(String clientId){
    	SocketChannel socketChannel = clientIdSocketChannelMap.get(clientId);
    	clientIdSocketChannelMap.remove(clientId);
    	socketChannelClientIdMap.remove(socketChannel);
    }

}
