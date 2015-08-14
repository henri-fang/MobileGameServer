package com.core.message;

import org.apache.mina.core.buffer.IoBuffer;

public interface GameOutput {

	public void put(IoBuffer buffer, byte b);

	public void putBoolean(IoBuffer buffer, boolean bool);

	public void putChar(IoBuffer buffer, char c);

	public void putShort(IoBuffer buffer, short s);

	public void putInt(IoBuffer buffer, int i);

	public void putLong(IoBuffer buffer, long l);
}
