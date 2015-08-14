package com.core.message;


public interface GameMessage{

	public static final byte[] HEAD = { 'X', 'D' };

	public int getCmdId() ;
}
