package com.fly.netty.common;

import java.io.Serializable;

/**
 * 响应类型body对像
 * @author fly
 *
 */
public class ReplyBody implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String serverInfo;

	public String getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(String serverInfo) {
		this.serverInfo = serverInfo;
	}
}
