package com.core.server;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * 服务器接口
 */
public interface IServer {

	/**
	 * 处理消息
	 * 
	 * @param session
	 * @param buf
	 */
	public void doCommand(IoSession session, IoBuffer buf);

	/**
	 * Session创建
	 * 
	 * @param session
	 */
	public void sessionCreate(IoSession session);

	/**
	 * Session打开
	 * 
	 * @param session
	 */
	public void sessionOpened(IoSession session);

	/**
	 * Session关闭
	 * 
	 * @param session
	 */
	public void sessionClosed(IoSession session);

	/**
	 * Session出错
	 * 
	 * @param session
	 */
	public void exceptionCaught(IoSession session, Throwable cause);

	/**
	 * Session Idle出错
	 * 
	 * @param session
	 */
	public void sessionIdle(IoSession session, IdleStatus idlestatus);
}
