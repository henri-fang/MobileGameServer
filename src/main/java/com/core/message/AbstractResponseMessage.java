package com.core.message;

import org.apache.mina.core.buffer.IoBuffer;

public abstract class AbstractResponseMessage extends AbstractMessage implements ResponseMessage, GameInput {

	public AbstractResponseMessage(int cmdId) {
		super(cmdId);
	}

	@Override
	public boolean getBoolean(IoBuffer buffer) {
		byte bool = buffer.get();
		return bool == 0;
	}

	@Override
	public byte get(IoBuffer buffer) {
		return buffer.get();
	}

	@Override
	public char getChar(IoBuffer buffer) {
		return buffer.getChar();
	}

	@Override
	public short getShort(IoBuffer buffer) {
		return buffer.getShort();
	}

	@Override
	public int getInt(IoBuffer buffer) {
		return buffer.getInt();
	}

	@Override
	public long getLong(IoBuffer buffer) {
		return buffer.getLong();
	}

}
