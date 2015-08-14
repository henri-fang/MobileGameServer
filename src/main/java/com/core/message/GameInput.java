package com.core.message;

import org.apache.mina.core.buffer.IoBuffer;

public interface GameInput {

	public boolean getBoolean(IoBuffer buffer);

	public byte get(IoBuffer buffer);

	public char getChar(IoBuffer buffer);

	public short getShort(IoBuffer buffer);

	public int getInt(IoBuffer buffer);

	public long getLong(IoBuffer buffer);
}
