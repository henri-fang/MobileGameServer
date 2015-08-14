package com.core.server;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * �������ӿ�
 */
public interface IServer {

	/**
	 * ������Ϣ
	 * 
	 * @param session
	 * @param buf
	 */
	public void doCommand(IoSession session, IoBuffer buf);

	/**
	 * Session����
	 * 
	 * @param session
	 */
	public void sessionCreate(IoSession session);

	/**
	 * Session��
	 * 
	 * @param session
	 */
	public void sessionOpened(IoSession session);

	/**
	 * Session�ر�
	 * 
	 * @param session
	 */
	public void sessionClosed(IoSession session);

	/**
	 * Session����
	 * 
	 * @param session
	 */
	public void exceptionCaught(IoSession session, Throwable cause);

	/**
	 * Session Idle����
	 * 
	 * @param session
	 */
	public void sessionIdle(IoSession session, IdleStatus idlestatus);
}
