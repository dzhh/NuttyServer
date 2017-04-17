package com.fly.netty.common;

public final class NettyMessage {

    private Header header;

    public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}
	
	private Body body;
	
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

    @Override
    public String toString() {
    	return "NettyMessage [header=" + header + "]";
    }
}
