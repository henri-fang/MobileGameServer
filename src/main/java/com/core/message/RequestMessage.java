package com.core.message;

import org.apache.mina.core.buffer.IoBuffer;

public interface RequestMessage extends GameMessage,GameOutput{

	public void decodeBody(IoBuffer buffer);

}
