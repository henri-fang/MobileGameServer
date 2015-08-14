package com.core.message;

public abstract class AbstractMessage implements GameMessage {

	private int cmdId;

	public AbstractMessage(int cmdId) {
		this.cmdId = cmdId;
	}

	public int getCmdId() {
		return cmdId;
	}

}
