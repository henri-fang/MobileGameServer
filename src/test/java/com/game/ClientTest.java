package com.game;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.core.codec.ServerProtocolCodecFactory;
import com.core.message.RequestMessage;

public class ClientTest {
	SessionHandler handler = new SessionHandler();
	static IoSession session ;
	
	public static void main(String[] args){
		InetSocketAddress address = new InetSocketAddress("192.168.0.105", 12005);
		NioSocketConnector connector = new NioSocketConnector();
		connector.getFilterChain().addLast("logging", new LoggingFilter());
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ServerProtocolCodecFactory()));
		ClientTest ct = new ClientTest();
		connector.setHandler(ct.handler);
		ConnectFuture future = connector.connect(address);
		future.awaitUninterruptibly();
		session = future.getSession();
		
		RequestMessage message = new GameRequestMessage(10002);
		session.write(message);
		
	}
	
	
	class SessionHandler implements IoHandler{

		@Override
		public void sessionCreated(IoSession session) throws Exception {
			System.err.println("创建session");
		}

		@Override
		public void sessionOpened(IoSession session) throws Exception {
			System.err.println("打开session");
		}

		@Override
		public void sessionClosed(IoSession session) throws Exception {
			 session.close(true);
		}

		@Override
		public void sessionIdle(IoSession session, IdleStatus status)
				throws Exception {
			
		}

		@Override
		public void exceptionCaught(IoSession session, Throwable cause)
				throws Exception {
			cause.printStackTrace();
		}

		@Override
		public void messageReceived(IoSession session, Object message)
				throws Exception {
			System.out.println("收到返回了");
		}

		@Override
		public void messageSent(IoSession session, Object message)
				throws Exception {
		}

		@Override
		public void inputClosed(IoSession arg0) throws Exception {
			
		}
		
	}
}
