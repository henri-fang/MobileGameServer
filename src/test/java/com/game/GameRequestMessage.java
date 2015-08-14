package com.game;

import org.apache.mina.core.buffer.IoBuffer;

import com.core.message.AbstractRequestMessage;

public class GameRequestMessage extends AbstractRequestMessage{

	private int id;
	
	public GameRequestMessage(int cmdId) {
		super(cmdId);
	}

	@Override
	public void decodeBody(IoBuffer buffer) {
		putInt(buffer, id);
	}


}
