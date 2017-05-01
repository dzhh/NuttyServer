package com.fly.netty.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fly.netty.codec.protobuf.MsgReqProtobuf.MsgReq;

/**
 * 存储消息
 * @author fly
 *
 */
public class MsgReqMap {

	private static Map<String, MsgReq> clientIdMsgReqMap = new ConcurrentHashMap<String, MsgReq>();
	
	public static void add(String clientId, MsgReq msgReq) {
		clientIdMsgReqMap.put(clientId, msgReq);
	}
	
	public static MsgReq get(String clientId) {
		return clientIdMsgReqMap.get(clientId);
	}
	
	public static void remove(String clientId){
		clientIdMsgReqMap.remove(clientId);
	}
}
