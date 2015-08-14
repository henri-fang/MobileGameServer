package com.core.message;

import org.apache.mina.core.buffer.IoBuffer;

public interface ResponseMessage extends GameMessage{

	public void encodeBody(IoBuffer buffer);

}
