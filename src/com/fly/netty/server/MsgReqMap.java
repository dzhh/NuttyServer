package com.fly.netty.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fly.netty.codec.protobuf.MsgClient2Server;


/**
 * 存储消息
 * @author fly
 *
 */
public class MsgReqMap {

	private static Map<String, MsgClient2Server.Msg> clientIdMsgReqMap = new ConcurrentHashMap<String, MsgClient2Server.Msg>();
	
	public static void add(String clientId, MsgClient2Server.Msg msgReq) {
		clientIdMsgReqMap.put(clientId, msgReq);
	}
	
	public static MsgClient2Server.Msg get(String clientId) {
		return clientIdMsgReqMap.get(clientId);
	}
	
	public static void remove(String clientId){
		clientIdMsgReqMap.remove(clientId);
	}
}
