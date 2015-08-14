package com.core.message;

import org.apache.mina.core.buffer.IoBuffer;

public abstract class AbstractRequestMessage extends AbstractMessage implements RequestMessage, GameOutput {

	public AbstractRequestMessage(int cmdId) {
		super(cmdId);
	}

	@Override
	public void put(IoBuffer buffer, byte b) {
		buffer.put(b);
	}

	@Override
	public void putBoolean(IoBuffer buffer, boolean bool) {
		buffer.put(bool ? (byte) 0 : (byte) 1);
	}

	@Override
	public void putChar(IoBuffer buffer, char c) {
		buffer.putChar(c);
	}

	@Override
	public void putShort(IoBuffer buffer, short s) {
		buffer.putShort(s);
	}

	@Override
	public void putInt(IoBuffer buffer, int i) {
		buffer.putInt(i);
	}

	@Override
	public void putLong(IoBuffer buffer, long l) {
		buffer.putLong(l);
	}

}
