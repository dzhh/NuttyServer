package com.fly.netty.common;

/**
 *  常量设置：
 * @author fly
 *
 */
public class Constants {
	
    private static String clientId;
    
    public static String getClientId() {
        return clientId;
    }
    
    public static void setClientId(String clientId) {
        Constants.clientId = clientId;
    }
}